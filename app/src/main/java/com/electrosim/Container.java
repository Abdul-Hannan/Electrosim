package com.electrosim;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

public class Container extends SurfaceView implements SurfaceHolder.Callback {

	private ViewThread mThread;
	protected ArrayList<ElementInterface> mElements = new ArrayList<ElementInterface>();
	ElementInterface currentElement = null;
	float currentPointerX, currentPointerY;
	LinkPointInterface tempLinkPoint;
	ConnectorInterface newConnector;
	ConnectorFactory connectorFactory;
	protected List<ConnectorInterface> mConnectors = new ArrayList<ConnectorInterface>();
	Paint paint = new Paint();
	
	long timetracker1;
	long timetracker2;
	boolean delflag;
	
	
	
	

	public Container(Context context) {
		super(context);
		getHolder().addCallback(this);
		mThread = new ViewThread(this);
		paint.setColor(Color.RED);
		paint.setStyle(Paint.Style.STROKE);
	}

	public Container(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
		mThread = new ViewThread(this);
		paint.setColor(Color.RED);
		paint.setStyle(Paint.Style.STROKE);

	}

	public void insertElement(ElementInterface element){
		
		synchronized (mElements) {
			mElements.add(element);
		}
	}
	
	public void doDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		synchronized (mElements) {
			for (ConnectorInterface connector : mConnectors) {
				connector.draw(canvas);
			}

			for (ElementInterface element : mElements) {
				element.doDraw(canvas);
			}

			if (tempLinkPoint != null)
				canvas.drawCircle(tempLinkPoint.getX(), tempLinkPoint.getY(),
						20, paint);

			if (newConnector != null) {
				newConnector.draw(canvas, currentPointerX, currentPointerY);
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!mThread.isAlive()) {
			// mElements.add(new Element(getResources(), 30, 10));
			mThread = new ViewThread(this);
			mThread.setRunning(true);
			mThread.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mThread.isAlive()) {
			mThread.setRunning(false);
		}
	}

	private final ElementInterface getCurrentElement(float x, float y) {
		if (currentElement != null && currentElement.insideRectCheck(x, y))
			return currentElement;

		for (ElementInterface ele : mElements) {
			if (ele.insideRectCheck(x, y)) {
				return ele;
			}
		}
		return null;
	}
	
	private final ConnectorInterface getCurrentConnector(float x, float y) {
	
		for (ConnectorInterface c : mConnectors) {
			if (c.pointOnLine(x, y)) {
				return c;
			}
		}
		return null;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d("TEST1",
				"E - " + event.toString() + " :: " + event.getEdgeFlags());
		
		
		
		synchronized (mElements) {

			
			// if(event.getAction()==MotionEvent.ACTION_DOWN &&
			// insideRectCheck(event.getX(), event.getY())){
			// mousedown=true;
			// }else if(event.getAction()==MotionEvent.ACTION_UP && mousedown){
			// mousedown=false;
			// }else
			if (event.getAction() == MotionEvent.ACTION_MOVE &&  !isDelflag() ) {
				// Element e = getCurrentElement(event.getX(), event.getY());
				// if(e!=currentElement && currentElement!=null){
				// currentElement.mouseOut(event);
				// }
				// currentElement=e;
				if (currentElement != null) {

					float deltaX = event.getX() - currentPointerX;
					float deltaY = event.getY() - currentPointerY;
					currentElement.translate(deltaX, deltaY);

				} else if (newConnector != null) {
					ElementInterface e = getCurrentElement(event.getX(),
							event.getY());
					if (e != null) {
						tempLinkPoint = e.select(event);
					} else
						tempLinkPoint = null;
				}

				// if(mousedown){
				// x = (int) event.getX();
				// y = (int) event.getY();
				// }
				// if(currentElement!=null){
				// Log.d("HOVER","INSIDE RECT");
				// currentElement.mouseMove(event);
				// }
				currentPointerX = event.getX();
				currentPointerY = event.getY();
			
			} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
				ElementInterface e = getCurrentElement(event.getX(),
						event.getY());
				if(isDelflag()){
					if(e!=null){
						
					}else{
						ConnectorInterface c = getCurrentConnector(event.getX(),
								event.getY());
						if(c!=null){
							
							c.getLinkPointA().setConnector(null);
							c.getLinkPointB().setConnector(null);
							mConnectors.remove(c);
						}
					}
				}else{
					if (e != currentElement && currentElement != null) {
						currentElement.deSelect(event);
						if (e != null) {
							currentElement = e;
							tempLinkPoint = currentElement.select(event);
	
						} else
							currentElement = null;
					} else {
						if (currentElement == null && e != null) {
							currentElement = e;
							tempLinkPoint = currentElement.select(event);
						}
					}
	
					if (tempLinkPoint != null) {
						
						//changes1
						currentElement = null;
						if (newConnector == null) {
							// &
							 timetracker1 = System.currentTimeMillis();
	
							//newConnector = new Connector();
						
							newConnector = connectorFactory.createConnector();
							//$ if it is true
						
							//changes1
							if(newConnector.setLinkPointA(tempLinkPoint))
							
							//then do these two lines
							
						//	newConnector.setLinkPointA(tempLinkPoint);
							{
								tempLinkPoint.setConnector(newConnector);
							
								tempLinkPoint = null;
						
							}
						//if $ is false then make & null
			
							//changes1	
								
							else newConnector = null;
						}
	
					}
	
					// if((event.getEdgeFlags() & MotionEvent.EDGE_BOTTOM)!=0)
					//				if (event.getY() > 630)
						//mElements.add(new Element(getResources(), (int) event
							//	.getX(), (int) event.getY()));
				}
				currentPointerX = event.getX();
				currentPointerY = event.getY();
			} else if (event.getAction() == MotionEvent.ACTION_UP &&  !isDelflag()) {
				if (tempLinkPoint != null) {
					if (newConnector != null) {

						if(newConnector.setLinkPointB(tempLinkPoint))
						
						{
						//newConnector.setLinkPointB(tempLinkPoint);	
						tempLinkPoint.setConnector(newConnector);
						mConnectors.add(newConnector);
						newConnector = null;
						}
						}
					tempLinkPoint = null;
				} else if (newConnector != null){
					
					
					 timetracker2 = System.currentTimeMillis();
					
					if ((timetracker2-timetracker1) > 5000){
						
						//newConnector.setLinkPointA(lp)
						newConnector.getLinkPointA().setConnector(null);
						newConnector =null;
						
					//use time diffrer here
					}else
					
					{	
					 timetracker1 = timetracker2;
						newConnector.addLinkBreakpoint(new LineBreakpoint(
							event.getX(), event.getY(), newConnector));
				
					
					}	
						
					
					}
			}
			// if currentconn != null and templinkpoint ==null

		}
		
		return true;
	}

	public ConnectorFactory getConnectorFactory() {
		return connectorFactory;
	}

	public void setConnectorFactory(ConnectorFactory connectorFactory) {
		this.connectorFactory = connectorFactory;
	}

	public boolean isDelflag() {
		return delflag;
	}

	public void setDelflag(boolean delflag) {
		this.delflag = delflag;
	}
}