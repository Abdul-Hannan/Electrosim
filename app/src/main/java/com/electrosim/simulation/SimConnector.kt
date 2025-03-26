/**
 * 
 */
package com.electrosim.simulation

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.electrosim.Connector
import com.electrosim.LinkPointInterface

/**
 * @author Abdul Hannan
 * 
 */
open class SimConnector : Connector() {
    private lateinit var portA: OutputPort
    private lateinit var portB: InputPort
    private var data: Any? = null
    private var flaghigh: Boolean = false

    private val simPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 3f
        setShadowLayer(2f, -1f, -1f, Color.BLUE)
        isAntiAlias = true
    }

    override fun setLinkPointA(lp: LinkPointInterface): Boolean {
        if (lp is OutputPort) {
            this.portA = lp
            return super.setLinkPointA(lp)
        }
        return false
    }

    override fun setLinkPointB(lp: LinkPointInterface): Boolean {
        if (lp is InputPort) {
            this.portB = lp
            return super.setLinkPointB(lp)
        }
        return false
    }

    fun simulate() {
        if (data == null) {
            data = portA.popData()
        }

        if (data != null) {
            flaghigh = (data as? Int) == 1
            
            if (portB.pushData(data)) {
                data = null
            }
        } else {
            flaghigh = false
        }
    }

    fun prepareToStart() {
        data = null
    }

    fun prepareToStop() {
        data = null
        flaghigh = false
    }

    override fun draw(canvas: Canvas) {
        if (flaghigh) {
            super.draw(canvas, simPaint)
        } else {
            super.draw(canvas)
        }
    }
} 