package com.electrosim.component

import android.content.res.Resources
import android.graphics.BitmapFactory
import com.electrosim.Container
import com.electrosim.R
import com.electrosim.simulation.InputPort
import com.electrosim.simulation.OutputPort
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.junit.Assert.*

@RunWith(RobolectricTestRunner::class)
class AndGateTest {

    @Mock
    private lateinit var container: Container

    @Mock
    private lateinit var res: Resources

    @Mock
    private lateinit var bitmap: Any

    private lateinit var andGate: AndGate
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        
        // Mock resource loading
        whenever(BitmapFactory.decodeResource(res, R.drawable.ugate5)).thenReturn(bitmap)
        whenever(BitmapFactory.decodeResource(res, R.drawable.ufourgate5)).thenReturn(bitmap)
        
        // Create AndGate with 2 inputs
        andGate = AndGate(100f, 100f, container, 2, res)
    }

    @Test
    fun `test AND gate with both inputs high produces high output`() {
        // Set up test
        // Get the input ports
        val input1 = andGate.inputPorts[0]
        val input2 = andGate.inputPorts[1]
        val output = andGate.outputPorts[0]
        
        // Prepare
        andGate.prepareToStart()
        
        // Simulate with both inputs high (1)
        input1.pushData(1)
        input2.pushData(1)
        andGate.simulate()
        
        // Check that output has value 1 (high)
        assertEquals(1, output.popData())
    }

    @Test
    fun `test AND gate with one input low produces low output`() {
        // Get the input ports
        val input1 = andGate.inputPorts[0]
        val input2 = andGate.inputPorts[1]
        val output = andGate.outputPorts[0]
        
        // Prepare
        andGate.prepareToStart()
        
        // Simulate with one input high (1) and one low (0)
        input1.pushData(1)
        input2.pushData(0)
        andGate.simulate()
        
        // Check that output has value 0 (low)
        assertEquals(0, output.popData())
    }

    @Test
    fun `test AND gate with both inputs low produces low output`() {
        // Get the input ports
        val input1 = andGate.inputPorts[0]
        val input2 = andGate.inputPorts[1]
        val output = andGate.outputPorts[0]
        
        // Prepare
        andGate.prepareToStart()
        
        // Simulate with both inputs low (0)
        input1.pushData(0)
        input2.pushData(0)
        andGate.simulate()
        
        // Check that output has value 0 (low)
        assertEquals(0, output.popData())
    }

    @Test
    fun `test AND gate with 4 inputs initialization`() {
        // Create a 4-input AND gate
        val fourInputGate = AndGate(100f, 100f, container, 4, res)
        
        // Verify it has 4 input ports and 1 output port
        assertEquals(4, fourInputGate.inputPorts.size)
        assertEquals(1, fourInputGate.outputPorts.size)
    }

    @Test
    fun `test AND gate four-input logic all high`() {
        // Create a 4-input AND gate
        val fourInputGate = AndGate(100f, 100f, container, 4, res)
        
        // Get ports
        val output = fourInputGate.outputPorts[0]
        
        // Prepare
        fourInputGate.prepareToStart()
        
        // Set all inputs high
        for (input in fourInputGate.inputPorts) {
            input.pushData(1)
        }
        
        // Simulate
        fourInputGate.simulate()
        
        // Check output is high
        assertEquals(1, output.popData())
    }

    @Test
    fun `test AND gate four-input logic one low`() {
        // Create a 4-input AND gate
        val fourInputGate = AndGate(100f, 100f, container, 4, res)
        
        // Get ports
        val output = fourInputGate.outputPorts[0]
        
        // Prepare
        fourInputGate.prepareToStart()
        
        // Set first 3 inputs high, last one low
        for (i in 0 until 3) {
            fourInputGate.inputPorts[i].pushData(1)
        }
        fourInputGate.inputPorts[3].pushData(0)
        
        // Simulate
        fourInputGate.simulate()
        
        // Check output is low
        assertEquals(0, output.popData())
    }
    
    @Test
    fun `test createNew returns a new instance with same properties`() {
        val newGate = andGate.createNew() as AndGate
        
        // Check it has the same number of ports
        assertEquals(andGate.inputPorts.size, newGate.inputPorts.size)
        assertEquals(andGate.outputPorts.size, newGate.outputPorts.size)
    }
    
    // Mock verification methods to avoid dependency on mockito-kotlin in imports
    private fun <T> whenever(methodCall: T): OngoingStubbing<T> {
        return `when`(methodCall)
    }
    
    interface OngoingStubbing<T> {
        fun thenReturn(value: T): OngoingStubbing<T>
    }
} 