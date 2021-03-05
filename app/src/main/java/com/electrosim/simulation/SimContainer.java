/**
 * 
 */
package com.electrosim.simulation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import com.electrosim.ConnectorInterface;
import com.electrosim.Container;
import com.electrosim.ElementInterface;

/**
 * @author Abdul Hannan
 *
 */
public class SimContainer extends Container implements Runnable {

	Thread runnningThread;
	boolean checkthread;
	
	
	/**
	 * @param context
	 */
	public SimContainer(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public SimContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
 
public boolean isRunning()
{
	
return checkthread;

}
public synchronized void start()
	{
	if(checkthread == false)

	{
		checkthread = true;
		
		runnningThread = new Thread(this);
	runnningThread.start();
		
	}
	
	}
	
public synchronized void stop(){
	
	if(checkthread == true)
		
	{
	
		checkthread = false;
		while(true){
			try {
				runnningThread.join();
				runnningThread = null;
				break;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			
				e.printStackTrace();
			
			}
		
		
		}
	
	}
	}
	
	@Override
	public void run() {
	

		for(ElementInterface el : mElements)
		{
			((SimElement)el).prepareToStart();
			
		}
		for(ConnectorInterface cn :mConnectors)
		{
			((SimConnector)cn).prepareToStart();
			
		}
		
		while(checkthread)
			
		{
		synchronized(mElements)
		{
		
			for( ElementInterface ele : mElements )
			{
				
				((SimElement)ele).simulate();
				
			}
			
			for( ConnectorInterface con : mConnectors )
			{
				
				((SimConnector)con).simulate();
				
			}
			
		
		
		}
		

			
		}
	
		for(ElementInterface el : mElements)
		{
			((SimElement)el).prepareToStop();
			
		}
		for(ConnectorInterface cn :mConnectors)
		{
			((SimConnector)cn).prepareToStop();
			
		}
		
		
	}
	
@Override
public void insertElement(ElementInterface element) {
	// TODO Auto-generated method stub
if(checkthread == false)
	super.insertElement(element);
}

@Override
public void surfaceDestroyed(SurfaceHolder holder) {
	
	this.stop();
	// TODO Auto-generated method stub
	super.surfaceDestroyed(holder);
}

}


