package com.electrosim;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface ElementInterface {

	public boolean insideRectCheck(float x,float y);
	
	public void doDraw(Canvas canvas);
	
	public void mouseMove(MotionEvent event);
	
	public void mouseOut(MotionEvent event);
	
	public LinkPointInterface select(MotionEvent event);

	public void deSelect(MotionEvent event);
	public void translate( float deltaX,float deltaY);
	
	public float getX();
	public float getY();
	
	ElementInterface createNew();
}
