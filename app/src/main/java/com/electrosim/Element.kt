package com.electrosim

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent

abstract class Element(protected val container: Container) : ElementInterface {
    protected var mX: Float = 0f
    private val linkPoints: MutableList<LinkPointInterface> = ArrayList()
    protected var mY: Float = 0f
    private var mWidth: Float = 0f
    private var mHeight: Float = 0f
    private var hover: Boolean = false
    private lateinit var mBitmap: Bitmap
    protected val paint: Paint = Paint()
    protected var posX: Float = 0f
    protected var posY: Float = 0f

    protected fun initiailizeElement(bitmap: Bitmap, x: Float, y: Float): ElementInterface {
        mBitmap = bitmap
        mX = x - mBitmap.width / 2
        mY = y - mBitmap.height / 2
        mWidth = mBitmap.width.toFloat()
        mHeight = mBitmap.height.toFloat()
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE

        this.posX = x
        this.posY = y
        return this
    }

    override fun insideRectCheck(x: Float, y: Float): Boolean {
        return (x > mX && x < (mX + mWidth) && y > mY && y < (mY + mHeight))
    }

    override fun doDraw(canvas: Canvas) {
        if (hover) {
            canvas.drawRect(mX - 2, mY - 2, mX + mWidth + 2, mY + mHeight + 2, paint)
            canvas.drawText("$mX,$mY", mX, mY + mHeight + 14, paint)
        }

        canvas.drawBitmap(mBitmap, mX, mY, paint)
    }

    protected fun addLinkPoint(lp: LinkPointInterface) {
        linkPoints.add(lp)
    }
    
    override fun mouseMove(event: MotionEvent) {
        hover = true
    }

    override fun mouseOut(event: MotionEvent) {
        hover = false
    }

    override fun select(event: MotionEvent): LinkPointInterface? {
        for (lpoint in linkPoints) {
            if (lpoint.pointHitTest(event))
                return lpoint
        }

        hover = true
        return null
    }

    override fun deSelect(event: MotionEvent) {
        hover = false
    }

    override fun translate(deltaX: Float, deltaY: Float) {
        this.posX += deltaX
        this.posY += deltaY
        
        mX = posX - mWidth / 2
        mY = posY - mHeight / 2
    }

    override fun getX(): Float {
        return posX
    }

    override fun getY(): Float {
        return posY
    }
} 