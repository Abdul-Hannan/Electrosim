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
            if (el is SimElement) {
                el.prepareToStart()
            }
        }
        
        for (cn in mConnectors) {
            if (cn is SimConnector) {
                cn.prepareToStart()
            }
        }

        while (checkthread) {
            synchronized(mElements) {
                for (ele in mElements) {
                    if (ele is SimElement) {
                        ele.simulate()
                    }
                }

                for (con in mConnectors) {
                    if (con is SimConnector) {
                        con.simulate()
                    }
                }
            }
        }

        for (el in mElements) {
            if (el is SimElement) {
                el.prepareToStop()
            }
        }
        
        for (cn in mConnectors) {
            if (cn is SimConnector) {
                cn.prepareToStop()
            }
        }
    }

    fun insertElementSim(element: ElementInterface) {
        if (!checkthread)
            super.insertElement(element)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        this.stop()
        super.surfaceDestroyed(holder)
    }
} 