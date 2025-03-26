/**
 * 
 */
package com.electrosim.simulation

import com.electrosim.ElementInterface

/**
 * @author HANNAN
 *
 */
class InputPort : SimPort {
    /**
     * @param x
     * @param y
     * @param radius
     * @param container
     */
    constructor(x: Float, y: Float, radius: Float, container: ElementInterface) : super(x, y, radius, container)

    constructor(x: Float, y: Float, container: ElementInterface) : super(x, y, container)
} 