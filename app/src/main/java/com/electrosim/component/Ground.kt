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
class Ground(
    x: Float,
    y: Float,
    container: Container,
    private val portcountinput: Int,
    private val res: Resources
) : SimElement(container) {
    
    init {
        initiailizeElement(BitmapFactory.decodeResource(res, R.drawable.groundf), x, y)
        addOutputPort(OutputPort(0f, 50f, this))
    }
    
    override fun doDraw(canvas: Canvas) {
        super.doDraw(canvas)
    }
    
    override fun prepareToStart() {
        super.prepareToStart()
    }
    
    override fun simulate() {
        // Always pushes a low signal (0)
        outputPorts[0].pushData(0)
    }
    
    override fun createNew(): ElementInterface {
        return Ground(x, y, container, portcountinput, res)
    }
} 