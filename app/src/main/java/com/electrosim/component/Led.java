/**
 * 
 */
package com.electrosim.component;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.electrosim.Container;
import com.electrosim.ElementInterface;
import com.electrosim.R;
import com.electrosim.simulation.InputPort;
import com.electrosim.simulation.SimElement;

/**
 * @author Abdul Hannan
 *
 */
public class Led extends SimElement {

	int portcountinput;
	Integer defValue;
	Bitmap glowingledbm;
	//Paint paint = new Paint();


Resources res;
 boolean GlowLed;	
	public Led(float x, float y,Container container, int portcountinput, Resources resources) {
		super(container);
		res=resources;
		
	
	this.portcountinput=portcountinput;
	
		
		initiailizeElement(BitmapFactory.decodeResource(resources, R.drawable.ledo), x, y);
		addInputPort(new InputPort(-90f,0f ,this));
		
	glowingledbm = BitmapFactory.decodeResource(resources, R.drawable.ledonn);
	

	
	}
@Override
public void doDraw(Canvas canvas) {
		
if(GlowLed)
	canvas.drawBitmap(glowingledbm, mX, mY, paint);

else
		
	super.doDraw(canvas);
}

@Override
protected void prepareToStop() {
GlowLed=false;	
	super.prepareToStop();
}
	@Override
	protected void simulate() {
 
		Integer invalue;
		
			

			for(InputPort ip : inputPorts )
		{
			
			
			invalue=(Integer)ip.popData();
			if(invalue == null)
				invalue = 0;
			if(invalue==0)
			{
				GlowLed = false;
				
			}

			else GlowLed =true;
			
			}
			

		
	
		
	}
	
	@Override
	public ElementInterface createNew() {
		return new Led(x,y,container,portcountinput,res);
	}
	
}
