package com.electrosim.storage

import android.content.res.Resources
import com.electrosim.ConnectorInterface
import com.electrosim.Container
import com.electrosim.ElementInterface
import com.electrosim.LineBreakpoint
import com.electrosim.component.*
import com.electrosim.simulation.InputPort
import com.electrosim.simulation.OutputPort
import com.electrosim.simulation.SimConnector
import com.electrosim.simulation.SimElement
import java.util.UUID

/**
 * Handles serialization and deserialization of circuit data
 */
class CircuitSerializer(
    private val container: Container,
    private val resources: Resources
) {

    /**
     * Serialize the current circuit state to CircuitData
     */
    fun serializeCircuit(
        name: String,
        elements: List<ElementInterface>,
        connectors: List<ConnectorInterface>
    ): CircuitData {
        val elementDataList = mutableListOf<ElementData>()
        val elementIdMap = mutableMapOf<ElementInterface, Int>()

        // Assign IDs to elements and serialize them
        elements.forEachIndexed { index, element ->
            elementIdMap[element] = index
            elementDataList.add(serializeElement(element, index))
        }

        // Serialize connections
        val connectionDataList = connectors.mapNotNull { connector ->
            serializeConnection(connector, elements, elementIdMap)
        }

        val now = System.currentTimeMillis()
        return CircuitData(
            id = UUID.randomUUID().toString(),
            name = name,
            createdAt = now,
            updatedAt = now,
            elements = elementDataList,
            connections = connectionDataList
        )
    }

    private fun serializeElement(element: ElementInterface, id: Int): ElementData {
        val type = getElementType(element)
        val portCount = getElementPortCount(element)

        return ElementData(
            id = id,
            type = type,
            x = element.getX(),
            y = element.getY(),
            portCount = portCount
        )
    }

    private fun getElementType(element: ElementInterface): String {
        return when (element) {
            is AndGate -> if (getInputPortCount(element) == 4) ComponentType.AND_GATE_4 else ComponentType.AND_GATE_2
            is NandGate -> if (getInputPortCount(element) == 4) ComponentType.NAND_GATE_4 else ComponentType.NAND_GATE_2
            is OrGate -> if (getInputPortCount(element) == 4) ComponentType.OR_GATE_4 else ComponentType.OR_GATE_2
            is NorGate -> if (getInputPortCount(element) == 4) ComponentType.NOR_GATE_4 else ComponentType.NOR_GATE_2
            is XorGate -> if (getInputPortCount(element) == 4) ComponentType.XOR_GATE_4 else ComponentType.XOR_GATE_2
            is XnorGate -> if (getInputPortCount(element) == 4) ComponentType.XNOR_GATE_4 else ComponentType.XNOR_GATE_2
            is NotGate -> ComponentType.NOT_GATE
            is Led -> ComponentType.LED
            is Vcc -> ComponentType.VCC
            is Ground -> ComponentType.GROUND
            is Extension -> ComponentType.EXTENSION
            else -> "UNKNOWN"
        }
    }

    private fun getInputPortCount(element: ElementInterface): Int {
        return if (element is SimElement) element.inputPorts.size else 0
    }

    private fun getElementPortCount(element: ElementInterface): Int {
        return if (element is SimElement) element.inputPorts.size else 0
    }

    private fun serializeConnection(
        connector: ConnectorInterface,
        elements: List<ElementInterface>,
        elementIdMap: Map<ElementInterface, Int>
    ): ConnectionData? {
        try {
            val pointA = connector.getLinkPointA()
            val pointB = connector.getLinkPointB()

            // Find which elements these ports belong to
            var fromElementId = -1
            var fromPortIndex = -1
            var toElementId = -1
            var toPortIndex = -1

            for (element in elements) {
                if (element is SimElement) {
                    // Check output ports (source of connection)
                    element.outputPorts.forEachIndexed { index, port ->
                        if (port === pointA) {
                            fromElementId = elementIdMap[element] ?: -1
                            fromPortIndex = index
                        }
                    }

                    // Check input ports (destination of connection)
                    element.inputPorts.forEachIndexed { index, port ->
                        if (port === pointB) {
                            toElementId = elementIdMap[element] ?: -1
                            toPortIndex = index
                        }
                    }
                }
            }

            if (fromElementId == -1 || toElementId == -1) {
                return null
            }

            // Serialize breakpoints
            val breakpoints = connector.getBreakpoints().map { bp ->
                BreakpointData(bp.x, bp.y)
            }

            return ConnectionData(
                fromElementId = fromElementId,
                fromPortIndex = fromPortIndex,
                toElementId = toElementId,
                toPortIndex = toPortIndex,
                breakpoints = breakpoints
            )
        } catch (e: IllegalStateException) {
            return null
        }
    }

    /**
     * Deserialize CircuitData and create elements/connections
     * Returns a pair of (elements list, connections list)
     */
    fun deserializeCircuit(circuitData: CircuitData): Pair<List<ElementInterface>, List<ConnectorInterface>> {
        val elements = mutableListOf<ElementInterface>()
        val elementById = mutableMapOf<Int, ElementInterface>()

        // Create elements
        for (elementData in circuitData.elements) {
            val element = createElementFromData(elementData)
            if (element != null) {
                elements.add(element)
                elementById[elementData.id] = element
            }
        }

        // Create connections
        val connectors = mutableListOf<ConnectorInterface>()
        for (connectionData in circuitData.connections) {
            val connector = createConnectionFromData(connectionData, elementById)
            if (connector != null) {
                connectors.add(connector)
            }
        }

        return Pair(elements, connectors)
    }

    private fun createElementFromData(data: ElementData): ElementInterface? {
        return when (data.type) {
            ComponentType.AND_GATE_2 -> AndGate(data.x, data.y, container, 2, resources)
            ComponentType.AND_GATE_4 -> AndGate(data.x, data.y, container, 4, resources)
            ComponentType.NAND_GATE_2 -> NandGate(data.x, data.y, container, 2, resources)
            ComponentType.NAND_GATE_4 -> NandGate(data.x, data.y, container, 4, resources)
            ComponentType.OR_GATE_2 -> OrGate(data.x, data.y, container, 2, resources)
            ComponentType.OR_GATE_4 -> OrGate(data.x, data.y, container, 4, resources)
            ComponentType.NOR_GATE_2 -> NorGate(data.x, data.y, container, 2, resources)
            ComponentType.NOR_GATE_4 -> NorGate(data.x, data.y, container, 4, resources)
            ComponentType.XOR_GATE_2 -> XorGate(data.x, data.y, container, 2, resources)
            ComponentType.XOR_GATE_4 -> XorGate(data.x, data.y, container, 4, resources)
            ComponentType.XNOR_GATE_2 -> XnorGate(data.x, data.y, container, 2, resources)
            ComponentType.XNOR_GATE_4 -> XnorGate(data.x, data.y, container, 4, resources)
            ComponentType.NOT_GATE -> NotGate(data.x, data.y, container, 0, resources)
            ComponentType.LED -> Led(data.x, data.y, container, 1, resources)
            ComponentType.VCC -> Vcc(data.x, data.y, container, 0, resources)
            ComponentType.GROUND -> Ground(data.x, data.y, container, 0, resources)
            ComponentType.EXTENSION -> Extension(data.x, data.y, container, 1, resources)
            else -> null
        }
    }

    private fun createConnectionFromData(
        data: ConnectionData,
        elementById: Map<Int, ElementInterface>
    ): ConnectorInterface? {
        val fromElement = elementById[data.fromElementId] as? SimElement ?: return null
        val toElement = elementById[data.toElementId] as? SimElement ?: return null

        if (data.fromPortIndex >= fromElement.outputPorts.size) return null
        if (data.toPortIndex >= toElement.inputPorts.size) return null

        val outputPort = fromElement.outputPorts[data.fromPortIndex]
        val inputPort = toElement.inputPorts[data.toPortIndex]

        val connector = SimConnector()
        if (!connector.setLinkPointA(outputPort)) return null
        if (!connector.setLinkPointB(inputPort)) return null

        outputPort.setConnector(connector)
        inputPort.setConnector(connector)

        // Add breakpoints
        for (bpData in data.breakpoints) {
            connector.addLinkBreakpoint(LineBreakpoint(bpData.x, bpData.y, connector))
        }

        return connector
    }
}
