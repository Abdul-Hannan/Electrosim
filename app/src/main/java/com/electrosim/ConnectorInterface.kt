/**
 * 
 */
package com.electrosim

import android.graphics.Canvas

/**
 * @author Abdul Hannan
 *
 */
interface ConnectorInterface {
    fun setLinkPointA(lp: LinkPointInterface): Boolean
    fun setLinkPointB(lp: LinkPointInterface): Boolean
    fun getLinkPointA(): LinkPointInterface
    fun getLinkPointB(): LinkPointInterface
    fun draw(canvas: Canvas)
    fun draw(canvas: Canvas, currentX: Float, currentY: Float)
    fun addLinkBreakpoint(point: LineBreakpoint)
    fun pointOnLine(currentX: Float, currentY: Float): Boolean
} 