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
class Extension(
    x: Float,
    y: Float,
    container: Container,
    private val portcountinput: Int,
    private val res: Resources
) : SimElement(container) {
    
    private var defValue: Int? = null
    
    init {
        initiailizeElement(BitmapFactory.decodeResource(res, R.drawable.connectorf), x, y)
        addInputPort(InputPort(-50f, 0f, this))
        addOutputPort(OutputPort(15f, 60f, this))
        addOutputPort(OutputPort(15f, -60f, this))
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
            for (ip in inputPorts) {
                var invalue = ip.popData() as Int?
                if (invalue == null) {
                    invalue = 0
                }
                
                defValue = invalue
            }
        }
        
        // Push the same value to both output ports
        if (outputPorts[0].pushData(defValue) && outputPorts[1].pushData(defValue)) {
            defValue = null
        }
    }
    
    override fun createNew(): ElementInterface {
        return Extension(posX, posY, container, portcountinput, res)
    }
} 