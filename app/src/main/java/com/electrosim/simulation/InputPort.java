/**
 * 
 */
package com.electrosim.simulation;

import com.electrosim.ElementInterface;

/**
 * @author HANNAN
 *
 */
public class InputPort extends SimPort {

	/**
	 * @param x
	 * @param y
	 * @param radius
	 * @param container
	 */
	public InputPort(float x, float y, float radius, ElementInterface container) {
		super(x, y, radius, container);
	}

	public InputPort(float x, float y,ElementInterface container) {
		super(x, y, container);
	}

}
