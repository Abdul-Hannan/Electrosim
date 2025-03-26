/**
 * 
 */
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

/**
 * @author Abdul Hannan
 *
 */
class OrGate(
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
                initiailizeElement(BitmapFactory.decodeResource(res, R.drawable.ugate3), x, y)
                addInputPort(InputPort(-200f, -50f, this))
                addInputPort(InputPort(-200f, 50f, this))
                addOutputPort(OutputPort(200f, 0f, this))
            }
            4 -> {
                initiailizeElement(BitmapFactory.decodeResource(res, R.drawable.ufourgate3), x, y)
                addInputPort(InputPort(-70f, -24f, this))
                addInputPort(InputPort(-70f, -10f, this))
                addInputPort(InputPort(-70f, 12f, this))
                addInputPort(InputPort(-70f, 24f, this))
                addOutputPort(OutputPort(64f, 0f, this))
            }
        }
    }
    
    override fun doDraw(canvas: Canvas) {
        super.doDraw(canvas)
    }
    
    override fun prepareToStart() {
        super.prepareToStart()
    }
    
    override fun simulate() {
        if (defValue == null) {
            defValue = 0 // Initial value for OR operation
            for (ip in inputPorts) {
                var invalue = ip.popData() as Int?
                if (invalue == null) {
                    invalue = 0
                }
                
                defValue = defValue!! or invalue
            }
        }
        
        if (outputPorts[0].pushData(defValue)) {
            defValue = null
        }
    }
    
    override fun createNew(): ElementInterface {
        return OrGate(x, y, container, portcountinput, res)
    }
} 