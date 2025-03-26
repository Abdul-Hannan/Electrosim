package com.electrosim

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

open class Connector : ConnectorInterface {
    protected var pointA: LinkPointInterface? = null
    protected var pointB: LinkPointInterface? = null
    var point1: LineBreakpoint? = null
    
    val paint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 2f
        setShadowLayer(2f, -1f, -1f, Color.BLUE)
        isAntiAlias = true
    }

    val breakpoints: MutableList<LineBreakpoint> = ArrayList()
    
    override fun addLinkBreakpoint(point: LineBreakpoint) {
        breakpoints.add(point)
    }

    override fun setLinkPointA(lp: LinkPointInterface): Boolean {
        this.pointA = lp
        return true
    }

    override fun setLinkPointB(lp: LinkPointInterface): Boolean {
        this.pointB = lp
        return true
    }

    override fun draw(canvas: Canvas) {
        val localPointA = pointA
        val localPointB = pointB
        if (localPointA != null && localPointB != null) {
            draw(canvas, paint)
        }
    }

    open fun draw(canvas: Canvas, paint: Paint) {
        val localPointA = pointA
        val localPointB = pointB
        if (localPointA != null && localPointB != null) {
            var currentlb = LineBreakpoint(localPointA.getX(), localPointA.getY(), this)
            
            for (lb in breakpoints) {
                canvas.drawLine(currentlb.x, currentlb.y, lb.x, lb.y, paint)
                currentlb = lb
            }
            
            canvas.drawLine(currentlb.x, currentlb.y, localPointB.getX(), localPointB.getY(), paint)
        }
    }
    
    override fun pointOnLine(currentX: Float, currentY: Float): Boolean {
        val localPointA = pointA
        val localPointB = pointB
        if (localPointA != null && localPointB != null) {
            var currentlb = LineBreakpoint(localPointA.getX(), localPointA.getY(), this)
            
            for (lb in breakpoints) {
                val deltaX = lb.x - currentlb.x
                if (deltaX != 0f) {
                    val m = (lb.y - currentlb.y) / deltaX
                    val b = currentlb.y - (m * currentlb.x)
                    
                    if (currentY == ((m * currentX) + b)) {
                        return true
                    }
                } else if (currentX == currentlb.x && 
                           currentY >= minOf(currentlb.y, lb.y) && 
                           currentY <= maxOf(currentlb.y, lb.y)) {
                    return true
                }
                
                currentlb = lb
            }
            
            val deltaX = localPointB.getX() - currentlb.x
            if (deltaX != 0f) {
                val m = (localPointB.getY() - currentlb.y) / deltaX
                val b = currentlb.y - (m * currentlb.x)
                
                if (currentY == ((m * currentX) + b)) {
                    return true
                }
            } else if (currentX == currentlb.x && 
                       currentY >= minOf(currentlb.y, localPointB.getY()) && 
                       currentY <= maxOf(currentlb.y, localPointB.getY())) {
                return true
            }
        }
        
        return false
    }
    
    override fun draw(canvas: Canvas, currentX: Float, currentY: Float) {
        val localPointA = pointA
        if (localPointA != null) {
            var currentlb = LineBreakpoint(localPointA.getX(), localPointA.getY(), this)
            
            for (lb in breakpoints) {
                canvas.drawLine(currentlb.x, currentlb.y, lb.x, lb.y, paint)
                currentlb = lb
            }
            
            canvas.drawLine(currentlb.x, currentlb.y, currentX, currentY, paint)
        }
    }
    
    override fun getLinkPointA(): LinkPointInterface {
        return pointA ?: throw IllegalStateException("pointA is not initialized")
    }
    
    override fun getLinkPointB(): LinkPointInterface {
        return pointB ?: throw IllegalStateException("pointB is not initialized")
    }
} 