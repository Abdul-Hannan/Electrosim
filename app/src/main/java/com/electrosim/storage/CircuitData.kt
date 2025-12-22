package com.electrosim.storage

/**
 * Data classes for circuit serialization/deserialization
 */

/**
 * Represents a saved circuit with all its components and connections
 */
data class CircuitData(
    val id: String,
    val name: String,
    val createdAt: Long,
    val updatedAt: Long,
    val elements: List<ElementData>,
    val connections: List<ConnectionData>
)

/**
 * Represents a single circuit component (gate, LED, VCC, etc.)
 */
data class ElementData(
    val id: Int,
    val type: String,
    val x: Float,
    val y: Float,
    val portCount: Int = 0
)

/**
 * Represents a connection (wire) between two ports
 */
data class ConnectionData(
    val fromElementId: Int,
    val fromPortIndex: Int,
    val toElementId: Int,
    val toPortIndex: Int,
    val breakpoints: List<BreakpointData>
)

/**
 * Represents a breakpoint (bend) in a wire
 */
data class BreakpointData(
    val x: Float,
    val y: Float
)

/**
 * Component types enum for serialization
 */
object ComponentType {
    const val AND_GATE_2 = "AND_2"
    const val AND_GATE_4 = "AND_4"
    const val NAND_GATE_2 = "NAND_2"
    const val NAND_GATE_4 = "NAND_4"
    const val OR_GATE_2 = "OR_2"
    const val OR_GATE_4 = "OR_4"
    const val NOR_GATE_2 = "NOR_2"
    const val NOR_GATE_4 = "NOR_4"
    const val XOR_GATE_2 = "XOR_2"
    const val XOR_GATE_4 = "XOR_4"
    const val XNOR_GATE_2 = "XNOR_2"
    const val XNOR_GATE_4 = "XNOR_4"
    const val NOT_GATE = "NOT"
    const val LED = "LED"
    const val VCC = "VCC"
    const val GROUND = "GND"
    const val EXTENSION = "EXT"
}
