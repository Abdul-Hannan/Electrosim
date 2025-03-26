package com.electrosim

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.util.ArrayList

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
    
    constructor(context: Context) : super(context) {
        holder.addCallback(this)
        mThread = ViewThread(this)
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
    }
    
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        holder.addCallback(this)
        mThread = ViewThread(this)
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
    }
    
    fun insertElement(element: ElementInterface) {
        synchronized(mElements) {
            mElements.add(element)
        }
    }
    
    fun doDraw(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)
        synchronized(mElements) {
            for (connector in mConnectors) {
                connector.draw(canvas)
            }
            
            for (element in mElements) {
                element.doDraw(canvas)
            }
            
            tempLinkPoint?.let {
                canvas.drawCircle(it.getX(), it.getY(), 20f, paint)
            }
            
            newConnector?.let {
                it.draw(canvas, currentPointerX, currentPointerY)
            }
        }
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
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d("TEST1", "E - $event :: ${event.edgeFlags}")
        
        synchronized(mElements) {
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    if (!delflag) {
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
                    val e = getCurrentElement(event.x, event.y)
                    
                    if (delflag) {
                        if (e == null) {
                            val c = getCurrentConnector(event.x, event.y)
                            c?.let {
                                it.getLinkPointA().setConnector(null)
                                it.getLinkPointB().setConnector(null)
                                mConnectors.remove(it)
                            }
                        }
                    } else {
                        if (e != currentElement && currentElement != null) {
                            currentElement?.deSelect(event)
                            if (e != null) {
                                currentElement = e
                                tempLinkPoint = currentElement?.select(event)
                            } else {
                                currentElement = null
                            }
                        } else if (currentElement == null && e != null) {
                            currentElement = e
                            tempLinkPoint = currentElement?.select(event)
                        }
                        
                        tempLinkPoint?.let {
                            currentElement = null
                            if (newConnector == null) {
                                timetracker1 = System.currentTimeMillis()
                                
                                connectorFactory?.let { factory ->
                                    newConnector = factory.createConnector()
                                    
                                    newConnector?.let { connector ->
                                        if (connector.setLinkPointA(it)) {
                                            it.setConnector(connector)
                                            tempLinkPoint = null
                                        } else {
                                            newConnector = null
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    currentPointerX = event.x
                    currentPointerY = event.y
                }
                
                MotionEvent.ACTION_UP -> {
                    if (!delflag) {
                        if (tempLinkPoint != null && newConnector != null) {
                            val lp = tempLinkPoint
                            val connector = newConnector
                            if (lp != null && connector != null && connector.setLinkPointB(lp)) {
                                lp.setConnector(connector)
                                mConnectors.add(connector)
                                newConnector = null
                            }
                            tempLinkPoint = null
                        } else if (newConnector != null) {
                            val connector = newConnector
                            if (connector != null) {
                                timetracker2 = System.currentTimeMillis()
                                
                                if ((timetracker2 - timetracker1) > 5000) {
                                    connector.getLinkPointA().setConnector(null)
                                    newConnector = null
                                } else {
                                    timetracker1 = timetracker2
                                    connector.addLinkBreakpoint(LineBreakpoint(event.x, event.y, connector))
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return true
    }
} 