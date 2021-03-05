/**
 * 
 */
package com.electrosim.simulation;

import com.electrosim.ElementInterface;

/**
 * @author Abdul Hannan
 *
 */
public class OutputPort extends SimPort {

	/**
	 * @param x
	 * @param y
	 * @param radius
	 * @param container
	 */
	public OutputPort(float x, float y, float radius, ElementInterface container) {
		super(x, y, radius, container);
		// TODO Auto-generated constructor stub
	}
	
	public OutputPort(float x, float y, ElementInterface container) {
		super(x, y, container);
		// TODO Auto-generated constructor stub
	}

}
