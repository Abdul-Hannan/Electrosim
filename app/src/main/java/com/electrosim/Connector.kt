package com.electrosim

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Connector : ConnectorInterface {
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
        pointA?.let { pointA ->
            pointB?.let { pointB ->
                draw(canvas, paint)
            }
        }
    }

    protected fun draw(canvas: Canvas, paint: Paint) {
        pointA?.let { pointA ->
            pointB?.let { pointB ->
                var currentlb = LineBreakpoint(pointA.getX(), pointA.getY(), this)
                
                for (lb in breakpoints) {
                    canvas.drawLine(currentlb.x, currentlb.y, lb.x, lb.y, paint)
                    currentlb = lb
                }
                
                canvas.drawLine(currentlb.x, currentlb.y, pointB.getX(), pointB.getY(), paint)
            }
        }
    }
    
    override fun pointOnLine(currentX: Float, currentY: Float): Boolean {
        pointA?.let { pointA ->
            pointB?.let { pointB ->
                var currentlb = LineBreakpoint(pointA.getX(), pointA.getY(), this)
                
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
                
                val deltaX = pointB.getX() - currentlb.x
                if (deltaX != 0f) {
                    val m = (pointB.getY() - currentlb.y) / deltaX
                    val b = currentlb.y - (m * currentlb.x)
                    
                    if (currentY == ((m * currentX) + b)) {
                        return true
                    }
                } else if (currentX == currentlb.x && 
                           currentY >= minOf(currentlb.y, pointB.getY()) && 
                           currentY <= maxOf(currentlb.y, pointB.getY())) {
                    return true
                }
            }
        }
        
        return false
    }
    
    override fun draw(canvas: Canvas, currentX: Float, currentY: Float) {
        pointA?.let { pointA ->
            var currentlb = LineBreakpoint(pointA.getX(), pointA.getY(), this)
            
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