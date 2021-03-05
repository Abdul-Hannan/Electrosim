package com.electrosim.simulation;

import com.electrosim.ConnectorFactory;
import com.electrosim.ConnectorInterface;

public class SimConnectorFactory  implements ConnectorFactory{

	@Override
	public ConnectorInterface createConnector() {
		return new SimConnector();
	}

}
