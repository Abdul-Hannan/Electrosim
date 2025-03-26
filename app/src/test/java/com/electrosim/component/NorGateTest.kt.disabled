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
class NorGateTest {

    @Mock
    private lateinit var container: Container

    @Mock
    private lateinit var res: Resources

    @Mock
    private lateinit var bitmap: Any

    private lateinit var norGate: NorGate
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        
        // Mock resource loading
        whenever(BitmapFactory.decodeResource(res, R.drawable.ugate3)).thenReturn(bitmap)
        whenever(BitmapFactory.decodeResource(res, R.drawable.ufourgate3)).thenReturn(bitmap)
        
        // Create NorGate with 2 inputs
        norGate = NorGate(100f, 100f, container, 2, res)
    }

    @Test
    fun `test NOR gate with both inputs low produces high output`() {
        // Get the input ports
        val input1 = norGate.inputPorts[0]
        val input2 = norGate.inputPorts[1]
        val output = norGate.outputPorts[0]
        
        // Prepare
        norGate.prepareToStart()
        
        // Simulate with both inputs low (0)
        input1.pushData(0)
        input2.pushData(0)
        norGate.simulate()
        
        // Check that output has value 1 (high) - NOR(0,0) = 1
        assertEquals(1, output.popData())
    }

    @Test
    fun `test NOR gate with one input high produces low output`() {
        // Get the input ports
        val input1 = norGate.inputPorts[0]
        val input2 = norGate.inputPorts[1]
        val output = norGate.outputPorts[0]
        
        // Prepare
        norGate.prepareToStart()
        
        // Simulate with one input high (1) and one low (0)
        input1.pushData(1)
        input2.pushData(0)
        norGate.simulate()
        
        // Check that output has value 0 (low) - NOR(1,0) = 0
        assertEquals(0, output.popData())
    }

    @Test
    fun `test NOR gate with both inputs high produces low output`() {
        // Get the input ports
        val input1 = norGate.inputPorts[0]
        val input2 = norGate.inputPorts[1]
        val output = norGate.outputPorts[0]
        
        // Prepare
        norGate.prepareToStart()
        
        // Simulate with both inputs high (1)
        input1.pushData(1)
        input2.pushData(1)
        norGate.simulate()
        
        // Check that output has value 0 (low) - NOR(1,1) = 0
        assertEquals(0, output.popData())
    }

    @Test
    fun `test NOR gate with 4 inputs initialization`() {
        // Create a 4-input NOR gate
        val fourInputGate = NorGate(100f, 100f, container, 4, res)
        
        // Verify it has 4 input ports and 1 output port
        assertEquals(4, fourInputGate.inputPorts.size)
        assertEquals(1, fourInputGate.outputPorts.size)
    }

    @Test
    fun `test NOR gate four-input logic all low`() {
        // Create a 4-input NOR gate
        val fourInputGate = NorGate(100f, 100f, container, 4, res)
        
        // Get ports
        val output = fourInputGate.outputPorts[0]
        
        // Prepare
        fourInputGate.prepareToStart()
        
        // Set all inputs low
        for (input in fourInputGate.inputPorts) {
            input.pushData(0)
        }
        
        // Simulate
        fourInputGate.simulate()
        
        // Check output is high (NOR of all zeros is 1)
        assertEquals(1, output.popData())
    }

    @Test
    fun `test NOR gate four-input logic one high`() {
        // Create a 4-input NOR gate
        val fourInputGate = NorGate(100f, 100f, container, 4, res)
        
        // Get ports
        val output = fourInputGate.outputPorts[0]
        
        // Prepare
        fourInputGate.prepareToStart()
        
        // Set first 3 inputs low, last one high
        for (i in 0 until 3) {
            fourInputGate.inputPorts[i].pushData(0)
        }
        fourInputGate.inputPorts[3].pushData(1)
        
        // Simulate
        fourInputGate.simulate()
        
        // Check output is low (NOR with any input high is 0)
        assertEquals(0, output.popData())
    }
    
    @Test
    fun `test createNew returns a new instance with same properties`() {
        val newGate = norGate.createNew() as NorGate
        
        // Check it has the same number of ports
        assertEquals(norGate.inputPorts.size, newGate.inputPorts.size)
        assertEquals(norGate.outputPorts.size, newGate.outputPorts.size)
    }
    
    // Mock verification methods to avoid dependency on mockito-kotlin in imports
    private fun <T> whenever(methodCall: T): OngoingStubbing<T> {
        return `when`(methodCall)
    }
    
    interface OngoingStubbing<T> {
        fun thenReturn(value: T): OngoingStubbing<T>
    }
} 