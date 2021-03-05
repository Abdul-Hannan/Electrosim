package com.electrosim;

import android.graphics.Canvas;
import android.view.MotionEvent;

public class LinkPoint implements LinkPointInterface {

	float x,y,radius;
	ElementInterface container;
	ConnectorInterface connector;
	
	public LinkPoint(float x, float y, float radius, ElementInterface container) {
		super();
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.container=container;
	}


	@Override
	public boolean pointHitTest(MotionEvent event) {
		if(connector == null){
			float myWorldX = container.getX() + x;
			float myWorldY = container.getY() + y;
			float eventX= event.getX();
			float eventY= event.getY();
			float dist = (float) Math.sqrt(Math.pow((eventX - myWorldX), 2)
					+Math.pow(eventY-myWorldY, 2)) ;
			
			
			if(dist<=radius)
				return true;
			
		}
		return false;
		
	}
	
	@Override
	public void setConnector(ConnectorInterface connector) {
		this.connector = connector;
		
	}


	@Override
	public float getY() {
		
		return container.getY() + y;
	}


	@Override
	public float getX() {
		
		return container.getX() + x;
	}


	@Override
	public void draw(Canvas canvas) {
		
		
	}

}
