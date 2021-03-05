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

public class AndGate extends SimElement {
	int portcountinput;
	Integer defValue;

Resources res;	
	public AndGate(float x, float y,Container container, int portcountinput, Resources resources) {
		super(container);
		res=resources;
		
	this.portcountinput=portcountinput;
	if(portcountinput==2)
	{
		
		initiailizeElement(BitmapFactory.decodeResource(resources, R.drawable.ugate5), x, y);
		addInputPort(new InputPort(-200f,-50f ,this));
		addInputPort(new InputPort(-200f,+50f ,this));
		addOutputPort(new OutputPort(200f, 0f, this));
	}
	
	else if(portcountinput==4)
		
	{
		initiailizeElement(BitmapFactory.decodeResource(resources, R.drawable.ufourgate5), x, y);

		addInputPort(new InputPort(-100f,-24f ,this));
		addInputPort(new InputPort(-100f,-10f ,this));
        addInputPort(new InputPort(-100f,+12f ,this));
		addInputPort(new InputPort(-100f,+24f ,this));
		addOutputPort(new OutputPort(100f, 0f, this));
		
	}
	
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
			defValue = 1;
			for(InputPort ip : inputPorts )
		{
			
			
			invalue=(Integer)ip.popData();
			if(invalue == null)
				invalue = 0;
			
			
			defValue = defValue & invalue;
			
			
			}
			
		}
		if(outputPorts.get(0).pushData(defValue))
		{
			defValue = null;
			
		}
	
		
	}
	
	@Override
	public ElementInterface createNew() {
		return new AndGate(x,y,container,portcountinput,res);
	}
	

}
