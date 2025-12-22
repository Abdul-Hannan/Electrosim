package com.electrosim.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream

/**
 * Manages saving and loading circuit data from device storage
 */
class CircuitStorageManager(private val context: Context) {

    companion object {
        private const val CIRCUITS_DIR = "circuits"
        private const val THUMBNAILS_DIR = "thumbnails"
        private const val CIRCUIT_FILE_EXTENSION = ".json"
        private const val THUMBNAIL_FILE_EXTENSION = ".png"
    }

    private val circuitsDir: File by lazy {
        File(context.filesDir, CIRCUITS_DIR).apply { mkdirs() }
    }

    private val thumbnailsDir: File by lazy {
        File(context.filesDir, THUMBNAILS_DIR).apply { mkdirs() }
    }

    /**
     * Save a circuit to device storage
     */
    fun saveCircuit(circuitData: CircuitData, thumbnail: Bitmap?): Boolean {
        return try {
            // Save circuit JSON
            val circuitFile = File(circuitsDir, "${circuitData.id}$CIRCUIT_FILE_EXTENSION")
            val json = circuitToJson(circuitData)
            circuitFile.writeText(json.toString(2))

            // Save thumbnail if provided
            thumbnail?.let {
                val thumbnailFile = File(thumbnailsDir, "${circuitData.id}$THUMBNAIL_FILE_EXTENSION")
                FileOutputStream(thumbnailFile).use { out ->
                    it.compress(Bitmap.CompressFormat.PNG, 90, out)
                }
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Load a circuit by ID
     */
    fun loadCircuit(circuitId: String): CircuitData? {
        return try {
            val circuitFile = File(circuitsDir, "$circuitId$CIRCUIT_FILE_EXTENSION")
            if (!circuitFile.exists()) return null

            val json = JSONObject(circuitFile.readText())
            jsonToCircuit(json)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Get all saved circuits (metadata only for list display)
     */
    fun getAllCircuits(): List<CircuitData> {
        val circuits = mutableListOf<CircuitData>()

        circuitsDir.listFiles()?.filter { it.extension == "json" }?.forEach { file ->
            try {
                val json = JSONObject(file.readText())
                val circuit = jsonToCircuit(json)
                circuits.add(circuit)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return circuits.sortedByDescending { it.updatedAt }
    }

    /**
     * Get thumbnail bitmap for a circuit
     */
    fun getThumbnail(circuitId: String): Bitmap? {
        return try {
            val thumbnailFile = File(thumbnailsDir, "$circuitId$THUMBNAIL_FILE_EXTENSION")
            if (thumbnailFile.exists()) {
                BitmapFactory.decodeFile(thumbnailFile.absolutePath)
            } else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Delete a circuit and its thumbnail
     */
    fun deleteCircuit(circuitId: String): Boolean {
        return try {
            val circuitFile = File(circuitsDir, "$circuitId$CIRCUIT_FILE_EXTENSION")
            val thumbnailFile = File(thumbnailsDir, "$circuitId$THUMBNAIL_FILE_EXTENSION")

            circuitFile.delete()
            thumbnailFile.delete()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Update an existing circuit (keeps the same ID and creation date)
     */
    fun updateCircuit(circuitData: CircuitData, thumbnail: Bitmap?): Boolean {
        val updatedCircuit = circuitData.copy(updatedAt = System.currentTimeMillis())
        return saveCircuit(updatedCircuit, thumbnail)
    }

    // JSON Serialization

    private fun circuitToJson(circuit: CircuitData): JSONObject {
        return JSONObject().apply {
            put("id", circuit.id)
            put("name", circuit.name)
            put("createdAt", circuit.createdAt)
            put("updatedAt", circuit.updatedAt)
            put("elements", elementsToJsonArray(circuit.elements))
            put("connections", connectionsToJsonArray(circuit.connections))
        }
    }

    private fun elementsToJsonArray(elements: List<ElementData>): JSONArray {
        return JSONArray().apply {
            elements.forEach { element ->
                put(JSONObject().apply {
                    put("id", element.id)
                    put("type", element.type)
                    put("x", element.x.toDouble())
                    put("y", element.y.toDouble())
                    put("portCount", element.portCount)
                })
            }
        }
    }

    private fun connectionsToJsonArray(connections: List<ConnectionData>): JSONArray {
        return JSONArray().apply {
            connections.forEach { conn ->
                put(JSONObject().apply {
                    put("fromElementId", conn.fromElementId)
                    put("fromPortIndex", conn.fromPortIndex)
                    put("toElementId", conn.toElementId)
                    put("toPortIndex", conn.toPortIndex)
                    put("breakpoints", JSONArray().apply {
                        conn.breakpoints.forEach { bp ->
                            put(JSONObject().apply {
                                put("x", bp.x.toDouble())
                                put("y", bp.y.toDouble())
                            })
                        }
                    })
                })
            }
        }
    }

    // JSON Deserialization

    private fun jsonToCircuit(json: JSONObject): CircuitData {
        return CircuitData(
            id = json.getString("id"),
            name = json.getString("name"),
            createdAt = json.getLong("createdAt"),
            updatedAt = json.getLong("updatedAt"),
            elements = jsonArrayToElements(json.getJSONArray("elements")),
            connections = jsonArrayToConnections(json.getJSONArray("connections"))
        )
    }

    private fun jsonArrayToElements(jsonArray: JSONArray): List<ElementData> {
        val elements = mutableListOf<ElementData>()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            elements.add(ElementData(
                id = obj.getInt("id"),
                type = obj.getString("type"),
                x = obj.getDouble("x").toFloat(),
                y = obj.getDouble("y").toFloat(),
                portCount = obj.optInt("portCount", 0)
            ))
        }
        return elements
    }

    private fun jsonArrayToConnections(jsonArray: JSONArray): List<ConnectionData> {
        val connections = mutableListOf<ConnectionData>()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            connections.add(ConnectionData(
                fromElementId = obj.getInt("fromElementId"),
                fromPortIndex = obj.getInt("fromPortIndex"),
                toElementId = obj.getInt("toElementId"),
                toPortIndex = obj.getInt("toPortIndex"),
                breakpoints = jsonArrayToBreakpoints(obj.getJSONArray("breakpoints"))
            ))
        }
        return connections
    }

    private fun jsonArrayToBreakpoints(jsonArray: JSONArray): List<BreakpointData> {
        val breakpoints = mutableListOf<BreakpointData>()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            breakpoints.add(BreakpointData(
                x = obj.getDouble("x").toFloat(),
                y = obj.getDouble("y").toFloat()
            ))
        }
        return breakpoints
    }
}
