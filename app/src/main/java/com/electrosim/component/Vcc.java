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
public class Vcc extends SimElement {

	int portcountinput;
	Integer defValue;

Resources res;	
	public Vcc(float x, float y,Container container, int portcountinput, Resources resources) {
		super(container);
		res=resources;
		
	
	this.portcountinput=portcountinput;
	
	
		
		initiailizeElement(BitmapFactory.decodeResource(resources, R.drawable.vccf), x, y);

		addOutputPort(new OutputPort(0f, -67f, this));
	
	
	
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

			
		
		outputPorts.get(0).pushData(1);
	
	
		
	}
	
	@Override
	public ElementInterface createNew() {
		return new Vcc(x,y,container,portcountinput,res);
	}

}
