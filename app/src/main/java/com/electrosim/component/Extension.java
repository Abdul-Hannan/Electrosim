/**
 * 
 */
package com.electrosim.component;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.electrosim.Container;
import com.electrosim.ElementInterface;
import com.electrosim.R;
import com.electrosim.simulation.InputPort;
import com.electrosim.simulation.OutputPort;
import com.electrosim.simulation.SimElement;

/**
 * @author Abdul Hannan
 *
 */
public class Extension extends SimElement {

	int portcountinput;
	Integer defValue;

Resources res;	
	public Extension(float x, float y,Container container, int portcountinput, Resources resources) {
		super(container);
		res=resources;
		
	this.portcountinput=portcountinput;
	
		
		initiailizeElement(BitmapFactory.decodeResource(resources, R.drawable.connectorf), x, y);
		addInputPort(new InputPort(-50f,0f ,this));
		addOutputPort(new OutputPort(15f, +60f, this));
		addOutputPort(new OutputPort(15f, -60f, this));

	
	}
@Override
public void doDraw(Canvas canvas) {
	
	super.doDraw(canvas);
}

@Override
protected void prepareToStop() {
	defValue=null;
	super.prepareToStop();
}
	@Override
	protected void simulate() {
 
		Integer invalue;
		
		if(defValue == null)
			
		{
			
			for(InputPort ip : inputPorts )
		{
			
			
			invalue=(Integer)ip.popData();
			if(invalue == null)
				invalue = 0;
		
			
			defValue = invalue;
			
			
			}
			
		}
		if(outputPorts.get(0).pushData(defValue) && outputPorts.get(1).pushData(defValue))
		{
			defValue = null;
			
		}
	
		
	}
	
	@Override
	public ElementInterface createNew() {
		return new Extension(x,y,container,portcountinput,res);
	}
	

}
