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
class NotGate(
    x: Float,
    y: Float,
    container: Container,
    private val portcountinput: Int,
    private val res: Resources
) : SimElement(container) {
    
    private var defValue: Int? = null
    
    init {
        initiailizeElement(BitmapFactory.decodeResource(res, R.drawable.ugate7), x, y)
        addInputPort(InputPort(-200f, 0f, this))
        addOutputPort(OutputPort(200f, 0f, this))
    }
    
    override fun doDraw(canvas: Canvas) {
        super.doDraw(canvas)
    }
    
    override fun prepareToStart() {
        super.prepareToStart()
    }
    
    override fun simulate() {
        if (defValue == null) {
            for (ip in inputPorts) {
                var invalue = ip.popData() as Int?
                if (invalue == null) {
                    invalue = 1
                }
                
                // NOT operation - invert the input
                defValue = if (invalue == 0) 1 else 0
            }
        }
        
        if (outputPorts[0].pushData(defValue)) {
            defValue = null
        }
    }
    
    override fun createNew(): ElementInterface {
        return NotGate(x, y, container, portcountinput, res)
    }
} 