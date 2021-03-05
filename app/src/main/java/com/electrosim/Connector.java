package com.electrosim;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Connector implements ConnectorInterface {

	protected LinkPointInterface pointA, pointB;
	LineBreakpoint point1;
	
	Paint paint = new Paint();

	List<LineBreakpoint> breakpoints = new ArrayList<LineBreakpoint>();
	
	public Connector() {
		super();
		
		paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setShadowLayer(2, -1, -1, Color.BLUE);
        paint.setAntiAlias(true);
	
	}
	public void addLinkBreakpoint(LineBreakpoint point)
	{
		
		breakpoints.add(point);
		
	}

	@Override
	public boolean setLinkPointA(LinkPointInterface lp) {
		
		this.pointA=lp;
	
	return true;
	}

	@Override
	public boolean setLinkPointB(LinkPointInterface lp) {
		this.pointB=lp;
		return true;
		
	}

	@Override
	public void draw(Canvas canvas) {

		draw(canvas,paint);
	
	}

	protected void draw(Canvas canvas, Paint paint)
	{
		
		LineBreakpoint currentlb = new LineBreakpoint(pointA.getX(), pointA.getY(), this);;
		for(LineBreakpoint lb : breakpoints )
			
		{
			canvas.drawLine(currentlb.getX(), currentlb.getY(), lb.getX(), lb.getY(), paint);
			
			 currentlb = lb; 

			
		}
		canvas.drawLine(currentlb.getX(), currentlb.getY(), pointB.getX(), pointB.getY(), paint);		
		
	}
	public boolean pointOnLine(float currentX, float currentY)
	
	{
		
		LineBreakpoint currentlb = new LineBreakpoint(pointA.getX(), pointA.getY(), this);;
		for(LineBreakpoint lb : breakpoints )
			
		{
			float m = (lb.y-currentlb.y)/(lb.x-currentlb.x);
			float b = currentlb.y-(m*currentlb.x);
			
			if(currentY==((m*currentX)+b)){
				return true;
			}
			 currentlb = lb; 

			
		}
		
		float m = (pointB.getY()-currentlb.y)/(pointB.getX()-currentlb.x);
		float b = currentlb.y-(m*currentlb.x);
		
		if(currentY==((m*currentX)+b)){
			return true;
		}else
			return false;	
	}
	public void draw(Canvas canvas,float currentX, float currentY)
	
	{
		
		LineBreakpoint currentlb = new LineBreakpoint(pointA.getX(), pointA.getY(), this);;
		for(LineBreakpoint lb : breakpoints )
			
		{
			canvas.drawLine(currentlb.getX(), currentlb.getY(), lb.getX(), lb.getY(), paint);
			
			 currentlb = lb; 

			
		}
		
		
		canvas.drawLine(currentlb.getX(), currentlb.getY(), currentX, currentY, paint);
		
		
	}
	@Override
	public LinkPointInterface getLinkPointA() {
		// TODO Auto-generated method stub
		return this.pointA;
	}
	@Override
	public LinkPointInterface getLinkPointB() {
		// TODO Auto-generated method stub
		return this.pointB;
	}
	
	
}
