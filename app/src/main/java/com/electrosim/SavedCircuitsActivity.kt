package com.electrosim

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import com.electrosim.adapter.SavedCircuitsAdapter
import com.electrosim.storage.CircuitData
import com.electrosim.storage.CircuitStorageManager

class SavedCircuitsActivity : Activity() {

    private lateinit var storageManager: CircuitStorageManager
    private lateinit var adapter: SavedCircuitsAdapter
    private lateinit var circuitsList: ListView
    private lateinit var emptyState: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_circuits)

        storageManager = CircuitStorageManager(this)

        // Initialize views
        circuitsList = findViewById(R.id.circuits_list)
        emptyState = findViewById(R.id.empty_state)

        // Setup adapter
        val circuits = storageManager.getAllCircuits()
        adapter = SavedCircuitsAdapter(this, circuits, storageManager) { circuit ->
            showDeleteConfirmation(circuit)
        }
        circuitsList.adapter = adapter

        // Handle item click to open circuit
        circuitsList.setOnItemClickListener { _, _, position, _ ->
            val circuit = adapter.getItem(position)
            openCircuit(circuit)
        }

        // Handle new circuit button
        findViewById<Button>(R.id.btn_new_circuit).setOnClickListener {
            openNewCircuit()
        }

        updateEmptyState()
    }

    override fun onResume() {
        super.onResume()
        refreshCircuits()
    }

    private fun refreshCircuits() {
        val circuits = storageManager.getAllCircuits()
        adapter.updateCircuits(circuits)
        updateEmptyState()
    }

    private fun updateEmptyState() {
        if (adapter.count == 0) {
            emptyState.visibility = View.VISIBLE
            circuitsList.visibility = View.GONE
        } else {
            emptyState.visibility = View.GONE
            circuitsList.visibility = View.VISIBLE
        }
    }

    private fun openCircuit(circuit: CircuitData) {
        val intent = Intent(this, CircuitDrawer::class.java).apply {
            putExtra(CircuitDrawer.EXTRA_CIRCUIT_ID, circuit.id)
        }
        startActivity(intent)
    }

    private fun openNewCircuit() {
        val intent = Intent(this, CircuitDrawer::class.java)
        startActivity(intent)
    }

    private fun showDeleteConfirmation(circuit: CircuitData) {
        AlertDialog.Builder(this)
            .setTitle("Delete Circuit")
            .setMessage("Are you sure you want to delete \"${circuit.name}\"?")
            .setPositiveButton("Delete") { _, _ ->
                deleteCircuit(circuit)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteCircuit(circuit: CircuitData) {
        val success = storageManager.deleteCircuit(circuit.id)
        if (success) {
            Toast.makeText(this, "Circuit deleted", Toast.LENGTH_SHORT).show()
            refreshCircuits()
        } else {
            Toast.makeText(this, "Failed to delete circuit", Toast.LENGTH_SHORT).show()
        }
    }
}
