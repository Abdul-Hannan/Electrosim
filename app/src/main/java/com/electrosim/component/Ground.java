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
import com.electrosim.simulation.OutputPort;
import com.electrosim.simulation.SimElement;

/**
 * @author Abdul Hannan
 *
 */
public class Ground extends SimElement {

	int portcountinput;
	Integer defValue;

Resources res;	
	public Ground(float x, float y,Container container, int portcountinput, Resources resources) {
		super(container);
		res=resources;
		
	this.portcountinput=portcountinput;
	
	
		
		initiailizeElement(BitmapFactory.decodeResource(resources, R.drawable.groundf), x, y);
		addOutputPort(new OutputPort(0f, 50f, this));
	
	
	
	}
@Override
public void doDraw(Canvas canvas) {

	
	
	super.doDraw(canvas);
}

@Override
protected void prepareToStart() {
	// TODO Auto-generated method stub
	super.prepareToStart();
}
	@Override
	protected void simulate() {

			
		
		outputPorts.get(0).pushData(0);
	
		
	}
	
	@Override
	public ElementInterface createNew() {
		return new Ground(x,y,container,portcountinput,res);
	}

}
