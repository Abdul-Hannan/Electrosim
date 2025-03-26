/**
 * 
 */
package com.electrosim.component

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import com.electrosim.Container
import com.electrosim.ElementInterface
import com.electrosim.R
import com.electrosim.simulation.InputPort
import com.electrosim.simulation.SimElement

/**
 * @author Abdul Hannan
 *
 */
class Led(
    x: Float,
    y: Float,
    container: Container,
    private val portcountinput: Int,
    private val res: Resources
) : SimElement(container) {
    
    private var defValue: Int? = null
    private val glowingLedBitmap: Bitmap
    private var glowLed: Boolean = false
    
    init {
        initiailizeElement(BitmapFactory.decodeResource(res, R.drawable.ledo), x, y)
        addInputPort(InputPort(-90f, 0f, this))
        
        glowingLedBitmap = BitmapFactory.decodeResource(res, R.drawable.ledonn)
    }
    
    override fun doDraw(canvas: Canvas) {
        if (glowLed) {
            canvas.drawBitmap(glowingLedBitmap, mX, mY, paint)
        } else {
            super.doDraw(canvas)
        }
    }
    
    override fun prepareToStop() {
        glowLed = false
        super.prepareToStop()
    }
    
    override fun simulate() {
        for (ip in inputPorts) {
            var invalue = ip.popData() as Int?
            if (invalue == null) {
                invalue = 0
            }
            
            glowLed = invalue != 0
        }
    }
    
    override fun createNew(): ElementInterface {
        return Led(x, y, container, portcountinput, res)
    }
} 