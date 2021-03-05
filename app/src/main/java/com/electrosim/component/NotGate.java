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
public class NotGate extends SimElement {

	int portcountinput;
	Integer defValue;

Resources res;	
	public NotGate(float x, float y,Container container, int portcountinput, Resources resources) {
		super(container);
		res=resources;
		
		// TODO Auto-generated constructor stub
	this.portcountinput=portcountinput;
	
		
		initiailizeElement(BitmapFactory.decodeResource(resources, R.drawable.ugate7), x, y);
		addInputPort(new InputPort(-200f,0f ,this));
		addOutputPort(new OutputPort(200f, 0f, this));
	
	
	
	
	}
@Override
public void doDraw(Canvas canvas) {
	
	super.doDraw(canvas);
}

@Override
protected void prepareToStart() {
	super.prepareToStart();
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
				invalue = 1;
			//if defval is null then defval is inval else downwards
			
		if(invalue == 0)
			defValue = 1;
		else defValue = 0;
			
			
		}
			
			}
		if(outputPorts.get(0).pushData(defValue))
		{
			defValue = null;
			
		}
	
		
	}
	
	@Override
	public ElementInterface createNew() {
		return new NotGate(x,y,container,portcountinput,res);
	}

}
