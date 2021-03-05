package com.electrosim;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class ViewThread extends Thread {
    private Container mPanel;
    private SurfaceHolder mHolder;
    private boolean mRun = false;
    
    public ViewThread(Container panel) {
        mPanel = panel;
        mHolder = mPanel.getHolder();
    }
    
    public void setRunning(boolean run) {
        mRun = run;
    }
    
    @Override
    public void run() {
        Canvas canvas = null;
       while (mRun) {
            canvas = mHolder.lockCanvas();
            if (canvas != null) {
            	//canvas.scale(2, 2);
                mPanel.doDraw(canvas);
                mHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}