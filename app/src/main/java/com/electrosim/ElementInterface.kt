package com.electrosim

import android.graphics.Canvas
import android.view.MotionEvent

interface ElementInterface {
    fun insideRectCheck(x: Float, y: Float): Boolean
    fun doDraw(canvas: Canvas)
    fun mouseMove(event: MotionEvent)
    fun mouseOut(event: MotionEvent)
    fun select(event: MotionEvent): LinkPointInterface?
    fun deSelect(event: MotionEvent)
    fun translate(deltaX: Float, deltaY: Float)
    fun getX(): Float
    fun getY(): Float
    fun createNew(): ElementInterface
} 