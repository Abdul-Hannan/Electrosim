/**
 * 
 */
package com.electrosim.simulation

import com.electrosim.ElementInterface
import com.electrosim.LinkPoint

/**
 * @author Abdul Hannan
 *
 */
open class SimPort : LinkPoint {
    private var data: Any? = null
    
    companion object {
        const val DEFAULT_RADIUS = 30.0f
    }
    
    /**
     * @param x the horizontal value where Port is to be created
     * @param y the vertical value where port is to be created
     * @param radius radios of the port
     * @param container the base on which this port will be created
     */
    constructor(x: Float, y: Float, radius: Float, container: ElementInterface) : super(x, y, radius, container)
    
    constructor(x: Float, y: Float, container: ElementInterface) : super(x, y, DEFAULT_RADIUS, container)
    
    /**
     * @param idata the data coming into the port
     * @return confirms that the data of the element has been updated
     */
    fun pushData(idata: Any?): Boolean {
        if (data == null) {
            data = idata
            return true
        }
        return false
    }
    
    /**
     * @return the data which will be forwarded to next element in series
     */
    fun popData(): Any? {
        val idata = data
        data = null
        return idata
    }
    
    open fun prepareToStart() {
        data = null
    }
    
    open fun prepareToStop() {
        data = null
    }
} 