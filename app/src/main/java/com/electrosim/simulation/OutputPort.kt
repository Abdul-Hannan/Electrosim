/**
 * 
 */
package com.electrosim.simulation

import com.electrosim.ElementInterface

/**
 * @author Abdul Hannan
 *
 */
class OutputPort : SimPort {
    /**
     * @param x
     * @param y
     * @param radius
     * @param container
     */
    constructor(x: Float, y: Float, radius: Float, container: ElementInterface) : super(x, y, radius, container)
    
    constructor(x: Float, y: Float, container: ElementInterface) : super(x, y, container)
} 