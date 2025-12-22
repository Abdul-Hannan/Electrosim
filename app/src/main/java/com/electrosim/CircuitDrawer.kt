package com.electrosim

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.electrosim.component.*
import com.electrosim.databinding.MainBinding
import com.electrosim.simulation.SimConnectorFactory
import com.electrosim.simulation.SimContainer
import com.electrosim.storage.CircuitData
import com.electrosim.storage.CircuitSerializer
import com.electrosim.storage.CircuitStorageManager

class CircuitDrawer : Activity(), View.OnClickListener {
    private val btElementMap = HashMap<Int, ElementInterface>()
    private lateinit var container: Container
    private var simtoggle = false
    private lateinit var binding: MainBinding
    private lateinit var storageManager: CircuitStorageManager
    private lateinit var circuitSerializer: CircuitSerializer

    // Track current circuit state
    private var currentCircuitId: String? = null
    private var currentCircuitName: String = DEFAULT_CIRCUIT_NAME

    companion object {
        const val EXTRA_CIRCUIT_ID = "circuit_id"
        const val DEFAULT_CIRCUIT_NAME = "Unnamed Circuit"
    }

    /** Called when the activity is first created.  */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Initialize ViewBinding
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        container = binding.surfaceView1
        container.connectorFactory = SimConnectorFactory()

        // Initialize storage and serializer
        storageManager = CircuitStorageManager(this)
        circuitSerializer = CircuitSerializer(container, resources)

        // Setup button click listeners
        val buttonIds = listOf(
            R.id.ext, R.id.and, R.id.nand, R.id.or, R.id.nor,
            R.id.xor, R.id.xnor, R.id.not, R.id.led, R.id.vcc,
            R.id.gnd, R.id.and4, R.id.nand4, R.id.or4, R.id.nor4,
            R.id.xor4, R.id.xnor4
        )

        for (id in buttonIds) {
            findViewById<Button>(id).setOnClickListener(this)
        }

        // Simulation control button
        binding.sim.setOnClickListener { v ->
            val simContainer = container as SimContainer
            if (simContainer.isRunning()) {
                simContainer.stop()
                v.setBackgroundResource(R.drawable.play)
            } else {
                simContainer.start()
                v.setBackgroundResource(R.drawable.pause)
            }
        }

        // Save button
        binding.save.setOnClickListener {
            showSaveDialog()
        }

        // Delete mode button
        binding.del.setOnClickListener {
            container.delflag = !container.delflag
        }

        // Initialize the component map
        val resources = resources

        with(btElementMap) {
            put(R.id.ext, Extension(20f, 220f, container, 1, resources))

            put(R.id.and, AndGate(20f, 220f, container, 2, resources))
            put(R.id.and4, AndGate(20f, 220f, container, 4, resources))

            put(R.id.nand, NandGate(20f, 220f, container, 2, resources))
            put(R.id.nand4, NandGate(20f, 220f, container, 4, resources))

            put(R.id.or, OrGate(20f, 220f, container, 2, resources))
            put(R.id.or4, OrGate(20f, 220f, container, 4, resources))

            put(R.id.xor, XorGate(20f, 220f, container, 2, resources))
            put(R.id.xor4, XorGate(20f, 220f, container, 4, resources))

            put(R.id.nor, NorGate(20f, 220f, container, 2, resources))
            put(R.id.nor4, NorGate(20f, 220f, container, 4, resources))

            put(R.id.xnor, XnorGate(20f, 220f, container, 2, resources))
            put(R.id.xnor4, XnorGate(20f, 220f, container, 4, resources))

            put(R.id.not, NotGate(20f, 220f, container, 0, resources))
            put(R.id.led, Led(20f, 220f, container, 1, resources))
            put(R.id.vcc, Vcc(20f, 220f, container, 0, resources))
            put(R.id.gnd, Ground(20f, 220f, container, 0, resources))
        }

        // Check if we're loading an existing circuit
        currentCircuitId = intent.getStringExtra(EXTRA_CIRCUIT_ID)
        if (currentCircuitId != null) {
            loadCircuit(currentCircuitId!!)
        } else {
            // New circuit - set default name
            updateCircuitNameDisplay()
        }
    }

    private fun updateCircuitNameDisplay() {
        findViewById<TextView>(R.id.circuit_name_label)?.text = currentCircuitName
        // Show "Saved" status if this is a saved circuit
        findViewById<TextView>(R.id.circuit_status)?.text = if (currentCircuitId != null) "(Saved)" else ""
    }

    private fun showSaveDialog() {
        val elements = container.getElements()
        if (elements.isEmpty()) {
            Toast.makeText(this, "No circuit to save", Toast.LENGTH_SHORT).show()
            return
        }

        val alert = AlertDialog.Builder(this)

        alert.setTitle("Save Circuit")
        alert.setMessage("Enter circuit name:")

        // Set an EditText view to get user input
        val input = EditText(this)
        input.hint = "My Circuit"

        // Pre-fill with current circuit name
        input.setText(currentCircuitName)
        // Select all text for easy replacement
        input.selectAll()

        alert.setView(input)

        alert.setPositiveButton("Save") { _, _ ->
            val circuitName = input.text.toString().trim()
            if (circuitName.isEmpty()) {
                Toast.makeText(this, "Please enter a circuit name", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            saveCircuit(circuitName)
        }

        alert.setNegativeButton("Cancel") { _, _ ->
            // Canceled
        }

        alert.show()
    }

    private fun saveCircuit(name: String) {
        try {
            val elements = container.getElements()
            val connectors = container.getConnectors()

            // Check if we're updating an existing circuit or creating new
            val circuitData: CircuitData
            val isUpdate = currentCircuitId != null

            if (isUpdate) {
                // Load existing circuit to preserve ID and creation date
                val existingCircuit = storageManager.loadCircuit(currentCircuitId!!)
                if (existingCircuit != null) {
                    // Update existing circuit with new data
                    circuitData = circuitSerializer.serializeCircuit(name, elements, connectors)
                        .copy(
                            id = existingCircuit.id,
                            createdAt = existingCircuit.createdAt,
                            updatedAt = System.currentTimeMillis()
                        )
                } else {
                    // Circuit was deleted, create new
                    circuitData = circuitSerializer.serializeCircuit(name, elements, connectors)
                }
            } else {
                // Create new circuit
                circuitData = circuitSerializer.serializeCircuit(name, elements, connectors)
            }

            // Capture thumbnail
            val thumbnail = container.captureCircuitBitmap()

            // Save to storage
            val success = storageManager.saveCircuit(circuitData, thumbnail)

            if (success) {
                currentCircuitId = circuitData.id
                currentCircuitName = name
                updateCircuitNameDisplay()

                val message = if (isUpdate) "Circuit updated!" else "Circuit saved!"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                Log.d("CircuitDrawer", "Circuit saved: ${circuitData.id}")
            } else {
                Toast.makeText(this, "Failed to save circuit", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error saving circuit: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadCircuit(circuitId: String) {
        try {
            val circuitData = storageManager.loadCircuit(circuitId)
            if (circuitData == null) {
                Toast.makeText(this, "Circuit not found", Toast.LENGTH_SHORT).show()
                return
            }

            // Deserialize and load
            val (elements, connectors) = circuitSerializer.deserializeCircuit(circuitData)
            container.loadCircuit(elements, connectors)

            // Update circuit tracking
            currentCircuitId = circuitData.id
            currentCircuitName = circuitData.name
            updateCircuitNameDisplay()

            Toast.makeText(this, "Loaded: ${circuitData.name}", Toast.LENGTH_SHORT).show()
            Log.d("CircuitDrawer", "Circuit loaded: ${circuitData.name}")
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error loading circuit: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(button: View) {
        val element = btElementMap[button.id]
        if (element != null) {
            container.insertElement(element.createNew())
        } else {
            Log.d("ss", "image not found")
        }
    }
}
