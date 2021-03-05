package com.electrosim;

public class LineBreakpoint {

	float x,y;
	ConnectorInterface connector;
	public LineBreakpoint(float x, float y, ConnectorInterface connector) {
		super();
		this.x = x;
		this.y = y;
		this.connector = connector;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	

	
	
}
