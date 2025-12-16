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

    private val breakpointPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val breakpointStrokePaint = Paint().apply {
        color = Color.rgb(100, 149, 237) // Cornflower blue
        style = Paint.Style.STROKE
        strokeWidth = 2f
        isAntiAlias = true
    }

    private val _breakpoints: MutableList<LineBreakpoint> = ArrayList()

    override fun getBreakpoints(): List<LineBreakpoint> = _breakpoints
    
    override fun addLinkBreakpoint(point: LineBreakpoint) {
        _breakpoints.add(point)
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

            for (lb in _breakpoints) {
                canvas.drawLine(currentlb.x, currentlb.y, lb.x, lb.y, paint)
                currentlb = lb
            }

            canvas.drawLine(currentlb.x, currentlb.y, localPointB.getX(), localPointB.getY(), paint)

            // Draw visual circles at breakpoints
            for (lb in _breakpoints) {
                canvas.drawCircle(lb.x, lb.y, BREAKPOINT_RADIUS, breakpointPaint)
                canvas.drawCircle(lb.x, lb.y, BREAKPOINT_RADIUS, breakpointStrokePaint)
            }
        }
    }
    
    override fun pointOnLine(currentX: Float, currentY: Float): Boolean {
        val localPointA = pointA
        val localPointB = pointB
        if (localPointA != null && localPointB != null) {
            var currentlb = LineBreakpoint(localPointA.getX(), localPointA.getY(), this)

            // Check each line segment
            for (lb in _breakpoints) {
                if (distanceToLineSegment(currentX, currentY, currentlb.x, currentlb.y, lb.x, lb.y) <= TAP_TOLERANCE) {
                    return true
                }
                currentlb = lb
            }

            // Check final segment to pointB
            if (distanceToLineSegment(currentX, currentY, currentlb.x, currentlb.y, localPointB.getX(), localPointB.getY()) <= TAP_TOLERANCE) {
                return true
            }
        }

        return false
    }

    companion object {
        private const val TAP_TOLERANCE = 30f // Pixels tolerance for tapping on a line
        private const val BREAKPOINT_RADIUS = 12f // Visual radius of breakpoint circles
    }

    private fun distanceToLineSegment(px: Float, py: Float, x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val dx = x2 - x1
        val dy = y2 - y1
        val lengthSquared = dx * dx + dy * dy

        if (lengthSquared == 0f) {
            // Line segment is a point
            return kotlin.math.sqrt((px - x1) * (px - x1) + (py - y1) * (py - y1))
        }

        // Calculate projection of point onto line segment
        var t = ((px - x1) * dx + (py - y1) * dy) / lengthSquared
        t = maxOf(0f, minOf(1f, t)) // Clamp to [0, 1] to stay within segment

        // Find closest point on segment
        val closestX = x1 + t * dx
        val closestY = y1 + t * dy

        // Return distance to closest point
        return kotlin.math.sqrt((px - closestX) * (px - closestX) + (py - closestY) * (py - closestY))
    }
    
    override fun draw(canvas: Canvas, currentX: Float, currentY: Float) {
        val localPointA = pointA
        if (localPointA != null) {
            var currentlb = LineBreakpoint(localPointA.getX(), localPointA.getY(), this)

            for (lb in _breakpoints) {
                canvas.drawLine(currentlb.x, currentlb.y, lb.x, lb.y, paint)
                currentlb = lb
            }

            canvas.drawLine(currentlb.x, currentlb.y, currentX, currentY, paint)

            // Draw visual circles at breakpoints during wire creation
            for (lb in _breakpoints) {
                canvas.drawCircle(lb.x, lb.y, BREAKPOINT_RADIUS, breakpointPaint)
                canvas.drawCircle(lb.x, lb.y, BREAKPOINT_RADIUS, breakpointStrokePaint)
            }
        }
    }
    
    override fun getLinkPointA(): LinkPointInterface {
        return pointA ?: throw IllegalStateException("pointA is not initialized")
    }

    override fun getLinkPointB(): LinkPointInterface {
        return pointB ?: throw IllegalStateException("pointB is not initialized")
    }

    override fun drawGlow(canvas: Canvas, glowPaint: Paint) {
        val localPointA = pointA
        val localPointB = pointB
        if (localPointA != null && localPointB != null) {
            var currentlb = LineBreakpoint(localPointA.getX(), localPointA.getY(), this)

            for (lb in _breakpoints) {
                canvas.drawLine(currentlb.x, currentlb.y, lb.x, lb.y, glowPaint)
                currentlb = lb
            }

            canvas.drawLine(currentlb.x, currentlb.y, localPointB.getX(), localPointB.getY(), glowPaint)
        }
    }
} 