package com.electrosim

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.electrosim.simulation.InputPort
import com.electrosim.simulation.OutputPort
import com.electrosim.simulation.SimElement
import java.util.ArrayList
import kotlin.math.pow
import kotlin.math.sqrt

open class Container : SurfaceView, SurfaceHolder.Callback {
    private var mThread: ViewThread
    protected val mElements: ArrayList<ElementInterface> = ArrayList()
    private var currentElement: ElementInterface? = null
    private var currentPointerX: Float = 0f
    private var currentPointerY: Float = 0f
    private var tempLinkPoint: LinkPointInterface? = null
    private var newConnector: ConnectorInterface? = null
    var connectorFactory: ConnectorFactory? = null
    protected val mConnectors: MutableList<ConnectorInterface> = ArrayList()
    private val paint: Paint = Paint()
    
    private var timetracker1: Long = 0
    private var timetracker2: Long = 0
    var delflag: Boolean = false

    // Connection mode state
    private var connectionModeElement: SimElement? = null
    private val connectButtonPaint: Paint = Paint()
    private val portHighlightPaint: Paint = Paint()
    private val portHighlightFillPaint: Paint = Paint()
    private val inputPortHighlightPaint: Paint = Paint()
    private val inputPortHighlightFillPaint: Paint = Paint()
    private val textPaint: Paint = Paint()

    // Long press detection for showing connect button
    private var longPressElement: ElementInterface? = null
    private var showConnectButton: Boolean = false
    private var longPressStartX: Float = 0f
    private var longPressStartY: Float = 0f
    private val longPressHandler = Handler(Looper.getMainLooper())
    private val longPressRunnable = Runnable {
        if (longPressElement != null && !showConnectButton) {
            showConnectButton = true
            currentElement?.deSelect(MotionEvent.obtain(0, 0, MotionEvent.ACTION_CANCEL, 0f, 0f, 0))
            currentElement = null
        }
    }

    // Long press detection for creating breakpoints during wire drawing
    private var breakpointLongPressX: Float = 0f
    private var breakpointLongPressY: Float = 0f
    private var breakpointLongPressTriggered: Boolean = false
    private val breakpointLongPressRunnable = Runnable {
        if (newConnector != null && !breakpointLongPressTriggered) {
            breakpointLongPressTriggered = true
            newConnector?.addLinkBreakpoint(LineBreakpoint(breakpointLongPressX, breakpointLongPressY, newConnector!!))
        }
    }

    // Breakpoint dragging state
    private var draggingBreakpoint: LineBreakpoint? = null

    // Delete mode glow animation
    private var glowAnimationTime: Long = 0
    private val deleteGlowPaint: Paint = Paint()

    companion object {
        private const val CONNECT_BUTTON_RADIUS = 40f
        private const val CONNECT_BUTTON_OFFSET_X = 60f
        private const val CONNECT_BUTTON_OFFSET_Y = -60f
        private const val PORT_HIGHLIGHT_RADIUS = 25f
        private const val LONG_PRESS_THRESHOLD_MS = 1000L
        private const val LONG_PRESS_MOVE_TOLERANCE = 20f
        private const val GLOW_ANIMATION_PERIOD_MS = 1000L
        private const val BREAKPOINT_RADIUS = 12f
        private const val BREAKPOINT_TAP_RADIUS = 25f
    }
    
    constructor(context: Context) : super(context) {
        holder.addCallback(this)
        mThread = ViewThread(this)
        initPaints()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        holder.addCallback(this)
        mThread = ViewThread(this)
        initPaints()
    }

    private fun initPaints() {
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE

        // Connect button paint (green filled circle)
        connectButtonPaint.color = Color.rgb(76, 175, 80) // Green
        connectButtonPaint.style = Paint.Style.FILL

        // Port highlight stroke paint (green outline)
        portHighlightPaint.color = Color.rgb(76, 175, 80)
        portHighlightPaint.style = Paint.Style.STROKE
        portHighlightPaint.strokeWidth = 4f

        // Port highlight fill paint (semi-transparent green)
        portHighlightFillPaint.color = Color.argb(80, 76, 175, 80)
        portHighlightFillPaint.style = Paint.Style.FILL

        // Input port highlight stroke paint (orange outline)
        inputPortHighlightPaint.color = Color.rgb(255, 152, 0) // Orange
        inputPortHighlightPaint.style = Paint.Style.STROKE
        inputPortHighlightPaint.strokeWidth = 4f

        // Input port highlight fill paint (semi-transparent orange)
        inputPortHighlightFillPaint.color = Color.argb(80, 255, 152, 0)
        inputPortHighlightFillPaint.style = Paint.Style.FILL

        // Text paint for button label
        textPaint.color = Color.WHITE
        textPaint.textSize = 36f
        textPaint.textAlign = Paint.Align.CENTER

        // Delete mode glow paint
        deleteGlowPaint.color = Color.rgb(244, 67, 54) // Red
        deleteGlowPaint.style = Paint.Style.STROKE
        deleteGlowPaint.strokeWidth = 6f
    }
    
    fun insertElement(element: ElementInterface) {
        synchronized(mElements) {
            mElements.add(element)
        }
    }
    
    fun doDraw(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)
        synchronized(mElements) {
            // Calculate glow alpha for delete mode animation (pulsing effect)
            val glowAlpha = if (delflag) {
                val elapsed = System.currentTimeMillis() - glowAnimationTime
                val phase = (elapsed % GLOW_ANIMATION_PERIOD_MS).toFloat() / GLOW_ANIMATION_PERIOD_MS
                // Sine wave for smooth pulsing: 100 to 255
                (100 + 155 * kotlin.math.sin(phase * 2 * Math.PI)).toInt()
            } else {
                0
            }

            for (connector in mConnectors) {
                connector.draw(canvas)
                // Draw glow effect around connectors in delete mode
                if (delflag) {
                    deleteGlowPaint.alpha = glowAlpha
                    connector.drawGlow(canvas, deleteGlowPaint)
                }
            }

            for (element in mElements) {
                element.doDraw(canvas)
                // Draw glow effect around elements in delete mode
                if (delflag) {
                    deleteGlowPaint.alpha = glowAlpha
                    drawElementGlow(canvas, element, deleteGlowPaint)
                }
            }

            tempLinkPoint?.let {
                canvas.drawCircle(it.getX(), it.getY(), 20f, paint)
            }

            newConnector?.let {
                it.draw(canvas, currentPointerX, currentPointerY)

                // Draw highlighted InputPorts on all other elements while wire is being drawn
                for (element in mElements) {
                    if (element is SimElement) {
                        val availableInputPorts = element.getAvailableInputPorts()
                        for (port in availableInputPorts) {
                            val portX = port.getX()
                            val portY = port.getY()
                            // Draw filled circle
                            canvas.drawCircle(portX, portY, PORT_HIGHLIGHT_RADIUS, inputPortHighlightFillPaint)
                            // Draw outline
                            canvas.drawCircle(portX, portY, PORT_HIGHLIGHT_RADIUS, inputPortHighlightPaint)
                        }
                    }
                }
            }

            // Draw Connect button only after long press on a component with available output ports
            if (!delflag && connectionModeElement == null && newConnector == null && showConnectButton) {
                longPressElement?.let { element ->
                    if (element is SimElement && element.getAvailableOutputPorts().isNotEmpty()) {
                        val buttonX = element.getX() + CONNECT_BUTTON_OFFSET_X
                        val buttonY = element.getY() + CONNECT_BUTTON_OFFSET_Y
                        // Draw button background
                        canvas.drawCircle(buttonX, buttonY, CONNECT_BUTTON_RADIUS, connectButtonPaint)
                        // Draw "C" label
                        canvas.drawText("C", buttonX, buttonY + 12f, textPaint)
                    }
                }
            }

            // Draw highlighted OutputPorts when in connection mode
            connectionModeElement?.let { element ->
                val availablePorts = element.getAvailableOutputPorts()
                for (port in availablePorts) {
                    val portX = port.getX()
                    val portY = port.getY()
                    // Draw filled circle
                    canvas.drawCircle(portX, portY, PORT_HIGHLIGHT_RADIUS, portHighlightFillPaint)
                    // Draw outline
                    canvas.drawCircle(portX, portY, PORT_HIGHLIGHT_RADIUS, portHighlightPaint)
                }
            }
        }
    }

    private fun drawElementGlow(canvas: Canvas, element: ElementInterface, glowPaint: Paint) {
        // Draw a glowing rectangle around the element
        val padding = 8f
        val x = element.getX()
        val y = element.getY()
        // Approximate element bounds (centered at x,y)
        canvas.drawRect(
            x - 100 - padding, y - 60 - padding,
            x + 100 + padding, y + 60 + padding,
            glowPaint
        )
    }
    
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // Empty implementation
    }
    
    override fun surfaceCreated(holder: SurfaceHolder) {
        if (!mThread.isAlive) {
            mThread = ViewThread(this)
            mThread.setRunning(true)
            mThread.start()
        }
    }
    
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        if (mThread.isAlive) {
            mThread.setRunning(false)
        }
    }
    
    private fun getCurrentElement(x: Float, y: Float): ElementInterface? {
        if (currentElement?.insideRectCheck(x, y) == true) {
            return currentElement
        }
        
        for (ele in mElements) {
            if (ele.insideRectCheck(x, y)) {
                return ele
            }
        }
        return null
    }
    
    private fun getCurrentConnector(x: Float, y: Float): ConnectorInterface? {
        for (c in mConnectors) {
            if (c.pointOnLine(x, y)) {
                return c
            }
        }
        return null
    }

    private fun isConnectButtonTapped(x: Float, y: Float): Boolean {
        if (!showConnectButton) return false
        val element = longPressElement ?: return false
        if (element !is SimElement) return false
        if (element.getAvailableOutputPorts().isEmpty()) return false

        val buttonX = element.getX() + CONNECT_BUTTON_OFFSET_X
        val buttonY = element.getY() + CONNECT_BUTTON_OFFSET_Y
        val dist = sqrt((x - buttonX).pow(2) + (y - buttonY).pow(2))
        return dist <= CONNECT_BUTTON_RADIUS
    }

    private fun getHighlightedPortAt(x: Float, y: Float): OutputPort? {
        val element = connectionModeElement ?: return null
        val availablePorts = element.getAvailableOutputPorts()

        for (port in availablePorts) {
            val portX = port.getX()
            val portY = port.getY()
            val dist = sqrt((x - portX).pow(2) + (y - portY).pow(2))
            if (dist <= PORT_HIGHLIGHT_RADIUS) {
                return port
            }
        }
        return null
    }

    private fun getBreakpointAt(x: Float, y: Float): LineBreakpoint? {
        for (connector in mConnectors) {
            val breakpoints = connector.getBreakpoints()
            for (breakpoint in breakpoints) {
                val dist = sqrt((x - breakpoint.x).pow(2) + (y - breakpoint.y).pow(2))
                if (dist <= BREAKPOINT_TAP_RADIUS) {
                    return breakpoint
                }
            }
        }
        return null
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d("TEST1", "E - $event :: ${event.edgeFlags}")

        synchronized(mElements) {
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    if (!delflag) {
                        // Handle breakpoint dragging
                        if (draggingBreakpoint != null) {
                            draggingBreakpoint?.x = event.x
                            draggingBreakpoint?.y = event.y
                            currentPointerX = event.x
                            currentPointerY = event.y
                            return@synchronized
                        }

                        // Check if moved too far for long press detection (element)
                        if (longPressElement != null && !showConnectButton) {
                            val moveDistance = sqrt(
                                (event.x - longPressStartX).pow(2) +
                                (event.y - longPressStartY).pow(2)
                            )
                            if (moveDistance > LONG_PRESS_MOVE_TOLERANCE) {
                                // Moved too far, cancel long press detection
                                longPressHandler.removeCallbacks(longPressRunnable)
                                longPressElement = null
                            }
                        }

                        // Check if moved too far for breakpoint long press detection
                        if (newConnector != null && !breakpointLongPressTriggered) {
                            val moveDistance = sqrt(
                                (event.x - breakpointLongPressX).pow(2) +
                                (event.y - breakpointLongPressY).pow(2)
                            )
                            if (moveDistance > LONG_PRESS_MOVE_TOLERANCE) {
                                // Moved too far, cancel breakpoint long press and update position
                                longPressHandler.removeCallbacks(breakpointLongPressRunnable)
                                breakpointLongPressX = event.x
                                breakpointLongPressY = event.y
                                // Restart long press timer at new position
                                longPressHandler.postDelayed(breakpointLongPressRunnable, LONG_PRESS_THRESHOLD_MS)
                            }
                        }

                        if (currentElement != null) {
                            val deltaX = event.x - currentPointerX
                            val deltaY = event.y - currentPointerY
                            currentElement?.translate(deltaX, deltaY)
                        } else if (newConnector != null) {
                            val e = getCurrentElement(event.x, event.y)
                            tempLinkPoint = if (e != null) {
                                e.select(event)
                            } else {
                                null
                            }
                        }

                        currentPointerX = event.x
                        currentPointerY = event.y
                    }
                }

                MotionEvent.ACTION_DOWN -> {
                    // Check if a breakpoint is being tapped for dragging
                    if (!delflag && newConnector == null) {
                        val breakpoint = getBreakpointAt(event.x, event.y)
                        if (breakpoint != null) {
                            draggingBreakpoint = breakpoint
                            currentPointerX = event.x
                            currentPointerY = event.y
                            return@synchronized
                        }
                    }

                    // Check if we're in connection mode and a highlighted port was tapped
                    val highlightedPort = getHighlightedPortAt(event.x, event.y)
                    if (highlightedPort != null) {
                        // Start wire drawing from the selected output port
                        timetracker1 = System.currentTimeMillis()
                        connectorFactory?.let { factory ->
                            newConnector = factory.createConnector()
                            newConnector?.let { connector ->
                                if (connector.setLinkPointA(highlightedPort)) {
                                    highlightedPort.setConnector(connector)
                                    connectionModeElement = null
                                    showConnectButton = false
                                    longPressElement = null
                                    // Start breakpoint long press detection
                                    breakpointLongPressX = event.x
                                    breakpointLongPressY = event.y
                                    breakpointLongPressTriggered = false
                                    longPressHandler.postDelayed(breakpointLongPressRunnable, LONG_PRESS_THRESHOLD_MS)
                                } else {
                                    newConnector = null
                                }
                            }
                        }
                        currentPointerX = event.x
                        currentPointerY = event.y
                        return@synchronized
                    }

                    // Check if Connect button was tapped
                    if (!delflag && connectionModeElement == null && isConnectButtonTapped(event.x, event.y)) {
                        val element = longPressElement
                        if (element is SimElement) {
                            connectionModeElement = element
                            showConnectButton = false
                            longPressElement = null
                        }
                        currentPointerX = event.x
                        currentPointerY = event.y
                        return@synchronized
                    }

                    // Clear connection mode and connect button if tapping elsewhere
                    if (connectionModeElement != null) {
                        connectionModeElement = null
                    }
                    if (showConnectButton) {
                        showConnectButton = false
                        longPressHandler.removeCallbacks(longPressRunnable)
                        longPressElement = null
                    }

                    val e = getCurrentElement(event.x, event.y)

                    if (delflag) {
                        // Delete mode: delete element with its connections, or delete connector
                        if (e != null) {
                            // Delete element and all its connections
                            deleteElementWithConnections(e)
                            delflag = false // Exit delete mode after deletion
                        } else {
                            val c = getCurrentConnector(event.x, event.y)
                            if (c != null) {
                                // Delete just the connector
                                c.getLinkPointA().setConnector(null)
                                c.getLinkPointB().setConnector(null)
                                mConnectors.remove(c)
                                delflag = false // Exit delete mode after deletion
                            }
                        }
                    } else {
                        // Start long press detection if touching an element
                        if (e != null && e is SimElement) {
                            longPressElement = e
                            longPressStartX = event.x
                            longPressStartY = event.y
                            // Schedule long press callback
                            longPressHandler.postDelayed(longPressRunnable, LONG_PRESS_THRESHOLD_MS)
                        }

                        // Element selection (without starting connections from link points)
                        if (e != currentElement && currentElement != null) {
                            currentElement?.deSelect(event)
                            if (e != null) {
                                currentElement = e
                            } else {
                                currentElement = null
                            }
                        } else if (currentElement == null && e != null) {
                            currentElement = e
                        }
                    }

                    currentPointerX = event.x
                    currentPointerY = event.y
                }

                MotionEvent.ACTION_UP -> {
                    // Release breakpoint dragging
                    if (draggingBreakpoint != null) {
                        draggingBreakpoint = null
                        return@synchronized
                    }

                    // Cancel long press detection if finger lifted before threshold
                    if (longPressElement != null && !showConnectButton) {
                        longPressHandler.removeCallbacks(longPressRunnable)
                        longPressElement = null
                    }

                    // Cancel breakpoint long press detection
                    longPressHandler.removeCallbacks(breakpointLongPressRunnable)
                    breakpointLongPressTriggered = false

                    if (!delflag) {
                        if (tempLinkPoint != null && newConnector != null) {
                            val lp = tempLinkPoint
                            val connector = newConnector
                            if (lp != null && connector != null && connector.setLinkPointB(lp)) {
                                lp.setConnector(connector)
                                mConnectors.add(connector)
                                newConnector = null
                                // Cancel breakpoint long press since connection is complete
                                longPressHandler.removeCallbacks(breakpointLongPressRunnable)
                                breakpointLongPressTriggered = false
                            }
                            tempLinkPoint = null
                        } else if (newConnector != null) {
                            val connector = newConnector
                            if (connector != null) {
                                timetracker2 = System.currentTimeMillis()

                                if ((timetracker2 - timetracker1) > 5000) {
                                    connector.getLinkPointA().setConnector(null)
                                    newConnector = null
                                }
                                // Breakpoints are now created via long press, not tap
                                // Just reset the timer for connection timeout
                                timetracker1 = timetracker2
                            }
                        }
                    }
                }
            }
        }

        return true
    }

    private fun deleteElementWithConnections(element: ElementInterface) {
        // Find and remove all connectors connected to this element
        val connectorsToRemove = mutableListOf<ConnectorInterface>()

        if (element is SimElement) {
            // Check all input and output ports for connections
            for (port in element.inputPorts) {
                if (port.hasConnector()) {
                    // Find the connector that has this port
                    for (connector in mConnectors) {
                        try {
                            if (connector.getLinkPointA() == port || connector.getLinkPointB() == port) {
                                connectorsToRemove.add(connector)
                            }
                        } catch (e: IllegalStateException) {
                            // Port not initialized, skip
                        }
                    }
                }
            }
            for (port in element.outputPorts) {
                if (port.hasConnector()) {
                    // Find the connector that has this port
                    for (connector in mConnectors) {
                        try {
                            if (connector.getLinkPointA() == port || connector.getLinkPointB() == port) {
                                connectorsToRemove.add(connector)
                            }
                        } catch (e: IllegalStateException) {
                            // Port not initialized, skip
                        }
                    }
                }
            }
        }

        // Remove the connectors
        for (connector in connectorsToRemove) {
            try {
                connector.getLinkPointA().setConnector(null)
                connector.getLinkPointB().setConnector(null)
            } catch (e: IllegalStateException) {
                // Port not initialized, skip
            }
            mConnectors.remove(connector)
        }

        // Remove the element
        mElements.remove(element)
    }

    fun setDeleteMode(enabled: Boolean) {
        delflag = enabled
        if (enabled) {
            glowAnimationTime = System.currentTimeMillis()
            synchronized(mElements) {
                // Cancel any in-progress connection
                newConnector?.let { connector ->
                    try {
                        connector.getLinkPointA().setConnector(null)
                    } catch (_: IllegalStateException) {
                        // pointA may not be initialized yet
                    }
                    newConnector = null
                }
                tempLinkPoint = null
                // Clear other modes when entering delete mode
                connectionModeElement = null
                showConnectButton = false
                longPressElement = null
                draggingBreakpoint = null
                breakpointLongPressTriggered = false
                currentElement?.deSelect(MotionEvent.obtain(0, 0, MotionEvent.ACTION_UP, 0f, 0f, 0))
                currentElement = null
            }
            longPressHandler.removeCallbacks(longPressRunnable)
            longPressHandler.removeCallbacks(breakpointLongPressRunnable)
        }
    }

    // Methods for circuit serialization
    fun getElements(): List<ElementInterface> = synchronized(mElements) { mElements.toList() }
    fun getConnectors(): List<ConnectorInterface> = synchronized(mElements) { mConnectors.toList() }

    /**
     * Capture the current circuit as a bitmap for thumbnail generation
     */
    fun captureCircuitBitmap(): Bitmap? {
        return try {
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            // Draw circuit without UI elements (delete mode glow, etc.)
            val savedDelFlag = delflag
            delflag = false
            doDraw(canvas)
            delflag = savedDelFlag

            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Clear all elements and connectors from the canvas
     */
    fun clearCircuit() {
        synchronized(mElements) {
            // Clear all connector references from ports
            for (connector in mConnectors) {
                try {
                    connector.getLinkPointA().setConnector(null)
                    connector.getLinkPointB().setConnector(null)
                } catch (_: IllegalStateException) {
                    // Port not initialized, skip
                }
            }
            mConnectors.clear()
            mElements.clear()

            // Reset UI state
            currentElement = null
            tempLinkPoint = null
            newConnector = null
            connectionModeElement = null
            showConnectButton = false
            longPressElement = null
            draggingBreakpoint = null
        }
    }

    /**
     * Load elements and connectors into the canvas
     */
    fun loadCircuit(elements: List<ElementInterface>, connectors: List<ConnectorInterface>) {
        synchronized(mElements) {
            clearCircuit()
            mElements.addAll(elements)
            mConnectors.addAll(connectors)
        }
    }
} 