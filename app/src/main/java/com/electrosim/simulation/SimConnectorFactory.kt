package com.electrosim.simulation

import com.electrosim.ConnectorFactory
import com.electrosim.ConnectorInterface

class SimConnectorFactory : ConnectorFactory {
    override fun createConnector(): ConnectorInterface {
        return SimConnector()
    }
} 