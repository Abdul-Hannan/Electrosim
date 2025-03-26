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
class XorGateTest {

    @Mock
    private lateinit var container: Container

    @Mock
    private lateinit var res: Resources

    @Mock
    private lateinit var bitmap: Any

    private lateinit var xorGate: XorGate
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        
        // Mock resource loading
        whenever(BitmapFactory.decodeResource(res, R.drawable.ugate7)).thenReturn(bitmap)
        whenever(BitmapFactory.decodeResource(res, R.drawable.ufourgate7)).thenReturn(bitmap)
        
        // Create XorGate with 2 inputs
        xorGate = XorGate(100f, 100f, container, 2, res)
    }

    @Test
    fun `test XOR gate with both inputs low produces low output`() {
        // Get the input ports
        val input1 = xorGate.inputPorts[0]
        val input2 = xorGate.inputPorts[1]
        val output = xorGate.outputPorts[0]
        
        // Prepare
        xorGate.prepareToStart()
        
        // Simulate with both inputs low (0)
        input1.pushData(0)
        input2.pushData(0)
        xorGate.simulate()
        
        // Check that output has value 0 (low) - XOR(0,0) = 0
        assertEquals(0, output.popData())
    }

    @Test
    fun `test XOR gate with one input high produces high output`() {
        // Get the input ports
        val input1 = xorGate.inputPorts[0]
        val input2 = xorGate.inputPorts[1]
        val output = xorGate.outputPorts[0]
        
        // Prepare
        xorGate.prepareToStart()
        
        // Simulate with one input high (1) and one low (0)
        input1.pushData(1)
        input2.pushData(0)
        xorGate.simulate()
        
        // Check that output has value 1 (high) - XOR(1,0) = 1
        assertEquals(1, output.popData())
    }

    @Test
    fun `test XOR gate with both inputs high produces low output`() {
        // Get the input ports
        val input1 = xorGate.inputPorts[0]
        val input2 = xorGate.inputPorts[1]
        val output = xorGate.outputPorts[0]
        
        // Prepare
        xorGate.prepareToStart()
        
        // Simulate with both inputs high (1)
        input1.pushData(1)
        input2.pushData(1)
        xorGate.simulate()
        
        // Check that output has value 0 (low) - XOR(1,1) = 0
        assertEquals(0, output.popData())
    }

    @Test
    fun `test XOR gate with 4 inputs initialization`() {
        // Create a 4-input XOR gate
        val fourInputGate = XorGate(100f, 100f, container, 4, res)
        
        // Verify it has 4 input ports and 1 output port
        assertEquals(4, fourInputGate.inputPorts.size)
        assertEquals(1, fourInputGate.outputPorts.size)
    }

    @Test
    fun `test XOR gate four-input logic odd number of high inputs`() {
        // Create a 4-input XOR gate
        val fourInputGate = XorGate(100f, 100f, container, 4, res)
        
        // Get ports
        val output = fourInputGate.outputPorts[0]
        
        // Prepare
        fourInputGate.prepareToStart()
        
        // Set odd number of inputs high (3 high, 1 low)
        for (i in 0 until 3) {
            fourInputGate.inputPorts[i].pushData(1)
        }
        fourInputGate.inputPorts[3].pushData(0)
        
        // Simulate
        fourInputGate.simulate()
        
        // Check output is high (XOR of odd number of 1s is 1)
        assertEquals(1, output.popData())
    }

    @Test
    fun `test XOR gate four-input logic even number of high inputs`() {
        // Create a 4-input XOR gate
        val fourInputGate = XorGate(100f, 100f, container, 4, res)
        
        // Get ports
        val output = fourInputGate.outputPorts[0]
        
        // Prepare
        fourInputGate.prepareToStart()
        
        // Set even number of inputs high (2 high, 2 low)
        fourInputGate.inputPorts[0].pushData(1)
        fourInputGate.inputPorts[1].pushData(1)
        fourInputGate.inputPorts[2].pushData(0)
        fourInputGate.inputPorts[3].pushData(0)
        
        // Simulate
        fourInputGate.simulate()
        
        // Check output is low (XOR of even number of 1s is 0)
        assertEquals(0, output.popData())
    }
    
    @Test
    fun `test createNew returns a new instance with same properties`() {
        val newGate = xorGate.createNew() as XorGate
        
        // Check it has the same number of ports
        assertEquals(xorGate.inputPorts.size, newGate.inputPorts.size)
        assertEquals(xorGate.outputPorts.size, newGate.outputPorts.size)
    }
    
    // Mock verification methods to avoid dependency on mockito-kotlin in imports
    private fun <T> whenever(methodCall: T): OngoingStubbing<T> {
        return `when`(methodCall)
    }
    
    interface OngoingStubbing<T> {
        fun thenReturn(value: T): OngoingStubbing<T>
    }
} 