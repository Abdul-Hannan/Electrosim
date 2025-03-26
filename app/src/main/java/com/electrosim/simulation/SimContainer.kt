/**
 * 
 */
package com.electrosim.simulation

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceHolder
import com.electrosim.ConnectorInterface
import com.electrosim.Container
import com.electrosim.ElementInterface

/**
 * @author Abdul Hannan
 *
 */
class SimContainer : Container, Runnable {
    private var runningThread: Thread? = null
    private var checkthread: Boolean = false

    /**
     * @param context
     */
    constructor(context: Context) : super(context)

    /**
     * @param context
     * @param attrs
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun isRunning(): Boolean {
        return checkthread
    }

    @Synchronized
    fun start() {
        if (!checkthread) {
            checkthread = true
            runningThread = Thread(this)
            runningThread?.start()
        }
    }

    @Synchronized
    fun stop() {
        if (checkthread) {
            checkthread = false
            while (true) {
                try {
                    runningThread?.join()
                    runningThread = null
                    break
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun run() {
        for (el in mElements) {
            (el as SimElement).prepareToStart()
        }
        
        for (cn in mConnectors) {
            (cn as SimConnector).prepareToStart()
        }

        while (checkthread) {
            synchronized(mElements) {
                for (ele in mElements) {
                    (ele as SimElement).simulate()
                }

                for (con in mConnectors) {
                    (con as SimConnector).simulate()
                }
            }
        }

        for (el in mElements) {
            (el as SimElement).prepareToStop()
        }
        
        for (cn in mConnectors) {
            (cn as SimConnector).prepareToStop()
        }
    }

    override fun insertElement(element: ElementInterface) {
        if (!checkthread)
            super.insertElement(element)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        this.stop()
        super.surfaceDestroyed(holder)
    }
} 