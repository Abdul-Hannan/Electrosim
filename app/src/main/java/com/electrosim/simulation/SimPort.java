/**
 * 
 */
package com.electrosim.simulation;

import com.electrosim.ElementInterface;
import com.electrosim.LinkPoint;

/**
 * @author Abdul Hannan
 *
 */
public class SimPort extends LinkPoint {

	
	Object data;
	public final static float DEFAULT_RADIUS = (float) 30.0;
	
	/**
	 * @param x the horizontal value where Port is to be created
	 * @param y the vertical value where port is to be created
	 * @param radius radios of the port
	 * @param container the base on which this port will be created
	 */
	public SimPort(float x, float y, float radius, ElementInterface container) {
		super(x, y, radius, container);
	}
	
	public SimPort(float x, float y, ElementInterface container) {
		super(x, y, DEFAULT_RADIUS, container);
	}
	
	/**
	 * @param idata the data coming into the port
	 * @return confirms that the data of the element has been updated
	 */
	public boolean pushData(Object idata)
	{
		if(data==null){
			data=idata;
			return true;
		}
		return false;
	}
	
	/**
	 * @return the data which will be forwarded to next element in series
	 */
	public Object popData(){
		Object idata=data;
		data=null;
		return idata;
	}

	protected void prepareToStart()
	{
		
	data=null;

	}
	
	protected void prepareToStop()
	{
		
	data=null;

	}


}
