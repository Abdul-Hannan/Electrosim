package com.electrosim

import android.graphics.Canvas
import android.view.MotionEvent

interface LinkPointInterface {
    fun pointHitTest(event: MotionEvent): Boolean
    fun setConnector(connector: ConnectorInterface)
    fun getY(): Float
    fun draw(canvas: Canvas)
    fun getX(): Float
} 