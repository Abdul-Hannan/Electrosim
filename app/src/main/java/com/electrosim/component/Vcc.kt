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
import com.electrosim.simulation.OutputPort
import com.electrosim.simulation.SimElement

/**
 * @author Abdul Hannan
 *
 */
class Vcc(
    x: Float,
    y: Float,
    container: Container,
    private val portcountinput: Int = 1,
    private val res: Resources
) : SimElement(container) {
    
    init {
        initiailizeElement(BitmapFactory.decodeResource(res, R.drawable.vccf), x, y)
        addOutputPort(OutputPort(0f, -67f, this))
    }
    
    override fun doDraw(canvas: Canvas) {
        super.doDraw(canvas)
    }
    
    override fun prepareToStart() {
        super.prepareToStart()
    }
    
    override fun simulate() {
        // Always pushes a high signal (1)
        outputPorts[0].pushData(1)
    }
    
    override fun createNew(): ElementInterface {
        return Vcc(posX, posY, container, portcountinput, res)
    }
} 