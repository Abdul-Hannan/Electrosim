/**
 * 
 */
package com.electrosim.simulation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.electrosim.Connector;
import com.electrosim.LinkPointInterface;

/**
 * @author Abdul Hannan
 * 
 */

public class SimConnector extends Connector {

	OutputPort portA;
	InputPort portB;
	Object data;
	boolean flaghigh;

	/**
	 * 
	 */

	Paint paint = new Paint();

	public SimConnector() {

		paint.setColor(Color.RED);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(3);
		paint.setShadowLayer(2, -1, -1, Color.BLUE);
        paint.setAntiAlias(true);


	}

	@Override
	public boolean setLinkPointA(LinkPointInterface lp) {
		if (lp instanceof OutputPort) {
			this.portA = (OutputPort) lp;
			return super.setLinkPointA(lp);
		}
		return false;
	}

	@Override
	public boolean setLinkPointB(LinkPointInterface lp) {
		if (lp instanceof InputPort) {
			this.portB = (InputPort) lp;

			return super.setLinkPointB(lp);
		}
		return false;
	}

	public void simulate() {

		if (data == null) {
			data = portA.popData();

		}

		// if(data != null)
		// flag=portB.pushData(data);
		// if(flag)
		// data=null;

		if (data != null) {
			if ((Integer) data == 1)
				flaghigh = true;
			else
				flaghigh = false;

			if (portB.pushData(data))
				data = null;

		}

		else
			flaghigh = false;
	}

	protected void prepareToStart() {

		data = null;

	}

	protected void prepareToStop() {

		data = null;
		flaghigh = false;
	}

	@Override
	public void draw(Canvas canvas) {
		if (flaghigh) {
			draw(canvas, paint);
		} else
			super.draw(canvas);
	}

}
