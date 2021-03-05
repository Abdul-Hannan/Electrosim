package com.electrosim.simulation;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

import com.electrosim.Container;
import com.electrosim.Element;
import com.electrosim.ElementInterface;

 public abstract class SimElement extends Element {

	public List<InputPort> inputPorts = new ArrayList<InputPort>();
	public List<OutputPort> outputPorts = new ArrayList<OutputPort>();
	protected SimElement(Container container) {
		super(container);
		// TODO Auto-generated constructor stub
	}

	protected void addInputPort(InputPort i )
	{
		
		
		
		inputPorts.add(i);		
	addLinkPoint(i);
	
	}
	
	protected void addOutputPort(OutputPort o )
	{
		outputPorts.add(o);
		addLinkPoint(o);
	}
	
	void run()
	{
		
		
		
	}

	protected abstract void simulate();

	//reset()
	
 protected  void prepareToStart()
 {
	 
	 for(InputPort ip : inputPorts)
		 
	 {
		 
		 ip.prepareToStart();
		 
	 }
 
	 for(OutputPort op : outputPorts)
	 {
		 
		 op.prepareToStart();
	 }
 
 }
 protected  void prepareToStop()
 
 {
	 
for(InputPort ip : inputPorts)
		 
	 {
		 
		 ip.prepareToStop();
		 
	 }
 
	 for(OutputPort op : outputPorts)
	 {
		 
		 op.prepareToStop();
	 }
	 
	 
 }
 
 }
 
