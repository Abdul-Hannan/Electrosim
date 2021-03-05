package com.electrosim;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface LinkPointInterface {


	public boolean pointHitTest(MotionEvent event);
	public void setConnector(ConnectorInterface connector);
	
	public float getY();
	
	public void draw(Canvas canvas);
	
	public float getX();
}
