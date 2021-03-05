package com.electrosim;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

public abstract class Element implements ElementInterface {
	protected float mX;
	private List<LinkPointInterface> linkPoints = new ArrayList<LinkPointInterface>();
	protected float mY;
	private float mWidth, mHeight;
	boolean hover;
	private Bitmap mBitmap;
	protected Paint paint = new Paint();
	protected float x;
	protected float y;
	protected Container container;

	
	protected Element( Container container) {
		
	this.container=container;
	}

	protected ElementInterface initiailizeElement(Bitmap bitmap, float x, float y)
	{	
		mBitmap = bitmap;
		mX = x - mBitmap.getWidth() / 2;
		mY = y - mBitmap.getHeight() / 2;
		mWidth = mBitmap.getWidth();
		mHeight = mBitmap.getHeight();
		paint.setColor(Color.RED);
		paint.setStyle(Paint.Style.STROKE);
		//linkPoints.add(new LinkPoint((mWidth/2)* -1 + 3, (mHeight/2)* -1+3, 10, this));
		//linkPoints.add(new LinkPoint((mWidth/2)* -1 +3, (mHeight/2)-3 , 10, this));

		this.x=x;
		this.y=y;
	return this;
	}
	public boolean insideRectCheck(float x, float y) {
		boolean res = (x > mX && x < (mX + mWidth) && y > mY && y < (mY + mHeight));
		// Log.d("RECTT", " -== " + res);
		return res;
	}

	public void doDraw(Canvas canvas) {
		if (hover) {
			// canvas.drawColor(Color.RED);
			// canvas.
			canvas.drawRect(mX - 2, mY - 2, mX + mWidth + 2, mY + mHeight + 2,
					paint);
			canvas.drawText(mX + "," + mY, mX, mY + mHeight + 14, paint);
		}

		canvas.drawBitmap(mBitmap, mX, mY, paint);
		
//		for (LinkPointInterface lpoint : linkPoints) {
//
//			canvas.drawCircle(lpoint.getX(), lpoint.getY(), 10, paint);
//
//		}
	}

	protected void addLinkPoint(LinkPointInterface lp)
	{
		
		linkPoints.add(lp);
		
	}
	
	public void mouseMove(MotionEvent event) {
		hover = true;
	}

	public void mouseOut(MotionEvent event) {
		hover = false;
	}

	public LinkPointInterface select(MotionEvent event) {

		for (LinkPointInterface lpoint : linkPoints) {

			if (lpoint.pointHitTest(event))
				return lpoint;

		}

		hover = true;
		return null;
	}

	public void deSelect(MotionEvent event) {
		hover = false;
	}

	public void translate(float deltaX, float deltaY) {
		
		this.x += deltaX;
		this.y += deltaY;
		
		mX = x - mWidth / 2;
		mY = y - mHeight / 2;
	}

	@Override
	public float getX() {
	return x;
	}

	@Override
	public float getY() {
				
		return y;
	}
	
}
