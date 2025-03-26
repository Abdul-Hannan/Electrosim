package com.electrosim

import android.graphics.Canvas
import android.view.SurfaceHolder

class ViewThread(private val mPanel: Container) : Thread() {
    private val mHolder: SurfaceHolder = mPanel.holder
    private var mRun = false
    
    fun setRunning(run: Boolean) {
        mRun = run
    }
    
    override fun run() {
        var canvas: Canvas?
        while (mRun) {
            canvas = mHolder.lockCanvas()
            if (canvas != null) {
                mPanel.doDraw(canvas)
                mHolder.unlockCanvasAndPost(canvas)
            }
        }
    }
} 