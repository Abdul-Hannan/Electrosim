package com.electrosim

import android.app.Activity
import android.app.AlertDialog
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import com.electrosim.component.*
import com.electrosim.databinding.MainBinding
import com.electrosim.simulation.SimConnectorFactory
import com.electrosim.simulation.SimContainer

class CircuitDrawer : Activity(), View.OnClickListener {
    private val btElementMap = HashMap<Int, ElementInterface>()
    private lateinit var container: Container
    private var simtoggle = false
    private lateinit var binding: MainBinding

    /** Called when the activity is first created.  */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        
        // Initialize ViewBinding
        binding = MainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        container = binding.surfaceView1
        container.connectorFactory = SimConnectorFactory()
        
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
            showDiag()
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
    }

    private fun showDiag() {
        val alert = AlertDialog.Builder(this)
        
        alert.setTitle("Save Circuit")
        alert.setMessage("Circuit Name:")
        
        // Set an EditText view to get user input
        val input = EditText(this)
        alert.setView(input)
        
        alert.setPositiveButton("Ok") { _, _ ->
            // Store circuit name from input instead of declaring unused variable
            val circuitName = input.text.toString()
            // Use the circuit name - for now just log it
            Log.d("CircuitDrawer", "Circuit name: $circuitName")
        }
        
        alert.setNegativeButton("Cancel") { _, _ ->
            // Canceled.
        }
        
        alert.show()
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