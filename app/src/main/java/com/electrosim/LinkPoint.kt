package com.electrosim

import android.graphics.Canvas
import android.view.MotionEvent
import kotlin.math.pow
import kotlin.math.sqrt

open class LinkPoint(
    private val x: Float,
    private val y: Float,
    private val radius: Float,
    private val container: ElementInterface
) : LinkPointInterface {
    
    private var connector: ConnectorInterface? = null
    
    override fun pointHitTest(event: MotionEvent): Boolean {
        if (connector == null) {
            val myWorldX = container.getX() + x
            val myWorldY = container.getY() + y
            val eventX = event.x
            val eventY = event.y
            val dist = sqrt((eventX - myWorldX).pow(2) + (eventY - myWorldY).pow(2))
            
            if (dist <= radius)
                return true
        }
        return false
    }
    
    override fun setConnector(connector: ConnectorInterface?) {
        this.connector = connector
    }

    override fun getY(): Float {
        return container.getY() + y
    }

    override fun getX(): Float {
        return container.getX() + x
    }

    override fun draw(canvas: Canvas) {
        // Empty implementation
    }
} 