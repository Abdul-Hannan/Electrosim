/**
 * 
 */
package com.electrosim;

import android.graphics.Canvas;

/**
 * @author Abdul Hannan
 *
 */
public interface ConnectorInterface {

public boolean setLinkPointA (LinkPointInterface lp);
public boolean setLinkPointB (LinkPointInterface lp);
public LinkPointInterface getLinkPointA();
public LinkPointInterface getLinkPointB();
public void draw (Canvas canvas);
public void draw(Canvas canvas,float currentX, float currentY);
public void addLinkBreakpoint(LineBreakpoint point);
public boolean pointOnLine(float currentX, float currentY);
}
