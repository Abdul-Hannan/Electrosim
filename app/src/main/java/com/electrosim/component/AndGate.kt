package com.electrosim.component

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Canvas
import com.electrosim.Container
import com.electrosim.ElementInterface
import com.electrosim.R
import com.electrosim.simulation.InputPort
import com.electrosim.simulation.OutputPort
import com.electrosim.simulation.SimElement

class AndGate(
    x: Float,
    y: Float,
    container: Container,
    private val portcountinput: Int,
    private val res: Resources
) : SimElement(container) {
    
    private var defValue: Int? = null
    
    init {
        when (portcountinput) {
            2 -> {
                initiailizeElement(BitmapFactory.decodeResource(res, R.drawable.ugate5), x, y)
                addInputPort(InputPort(-200f, -50f, this))
                addInputPort(InputPort(-200f, 50f, this))
                addOutputPort(OutputPort(200f, 0f, this))
            }
            4 -> {
                initiailizeElement(BitmapFactory.decodeResource(res, R.drawable.ufourgate5), x, y)
                addInputPort(InputPort(-100f, -24f, this))
                addInputPort(InputPort(-100f, -10f, this))
                addInputPort(InputPort(-100f, 12f, this))
                addInputPort(InputPort(-100f, 24f, this))
                addOutputPort(OutputPort(100f, 0f, this))
            }
        }
    }
    
    override fun doDraw(canvas: Canvas) {
        super.doDraw(canvas)
    }
    
    override fun prepareToStop() {
        defValue = null
        super.prepareToStop()
    }
    
    override fun simulate() {
        if (defValue == null) {
            defValue = 1
            for (ip in inputPorts) {
                var invalue = ip.popData() as Int?
                if (invalue == null)
                    invalue = 0
                
                defValue = defValue!! and invalue
            }
        }
        
        if (outputPorts[0].pushData(defValue)) {
            defValue = null
        }
    }
    
    override fun createNew(): ElementInterface {
        return AndGate(x, y, container, portcountinput, res)
    }
} 