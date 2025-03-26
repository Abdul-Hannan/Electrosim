package com.electrosim.simulation

import android.content.res.Resources
import android.graphics.BitmapFactory
import com.electrosim.ConnectorInterface
import com.electrosim.Container
import com.electrosim.R
import com.electrosim.component.AndGate
import com.electrosim.component.Led
import com.electrosim.component.NotGate
import com.electrosim.component.OrGate
import com.electrosim.component.Vcc
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.junit.Assert.*

@RunWith(RobolectricTestRunner::class)
class ComplexCircuitTest {

    @Mock
    private lateinit var container: Container

    @Mock
    private lateinit var res: Resources

    @Mock
    private lateinit var bitmap: Any
    
    @Mock
    private lateinit var connector: ConnectorInterface
    
    // Components for our circuit
    private lateinit var vcc1: Vcc
    private lateinit var vcc2: Vcc
    private lateinit var andGate: AndGate
    private lateinit var notGate: NotGate
    private lateinit var orGate: OrGate
    private lateinit var led: Led
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        
        // Mock resource loading for all needed components
        whenever(BitmapFactory.decodeResource(any(), anyInt())).thenReturn(bitmap)
        
        // Create components 
        vcc1 = Vcc(100f, 100f, container, 1, res)
        vcc2 = Vcc(100f, 200f, container, 1, res)
        andGate = AndGate(200f, 150f, container, 2, res)
        notGate = NotGate(300f, 150f, container, 1, res)
        orGate = OrGate(400f, 150f, container, 2, res)
        led = Led(500f, 150f, container, 1, res)
        
        // Setup connector mock behavior
        whenever(connector.getLinkPointA()).thenCallRealMethod()
        whenever(connector.getLinkPointB()).thenCallRealMethod()
    }

    /**
     * Test a complex circuit: (Vcc1 AND Vcc2) -> NOT -> OR(input, Vcc2) -> LED
     * This creates a circuit that should result in the LED being on
     */
    @Test
    fun `test complex circuit simulation`() {
        // Prepare all components for simulation
        prepareCircuit()
        
        // VCC1(1) AND VCC2(1) = 1
        wireComponents(vcc1, 0, andGate, 0)
        wireComponents(vcc2, 0, andGate, 1)
        
        // AND(1) -> NOT = 0
        wireComponents(andGate, 0, notGate, 0)
        
        // NOT(0) -> OR(input 0) = (wait for other input)
        wireComponents(notGate, 0, orGate, 0)
        
        // VCC2(1) -> OR(input 1) = OR(0,1) = 1
        wireComponents(vcc2, 0, orGate, 1)
        
        // OR(1) -> LED should turn on the LED
        wireComponents(orGate, 0, led, 0)
        
        // Run simulation cycle
        simulateCircuit()
        
        // LED should be on
        assertTrue(isLedOn(led))
    }
    
    /**
     * Test a circuit with feedback loop:
     * (Vcc1 AND Feedback) -> NOT -> [Feedback OR Vcc2] -> LED
     * This tests oscillator/feedback behavior
     */
    @Test
    fun `test circuit with oscillating behavior`() {
        // Prepare all components for simulation
        prepareCircuit()
        
        // VCC1(1) AND [Feedback from OR] = (initially don't connect feedback)
        wireComponents(vcc1, 0, andGate, 0)
        
        // AND -> NOT
        wireComponents(andGate, 0, notGate, 0)
        
        // NOT -> OR(input 0)
        wireComponents(notGate, 0, orGate, 0)
        
        // VCC2(1) -> OR(input 1) = OR(NOT output, 1) = 1 initially
        wireComponents(vcc2, 0, orGate, 1)
        
        // OR -> LED
        wireComponents(orGate, 0, led, 0)
        
        // First simulation cycle (no feedback yet)
        simulateCircuit()
        
        // LED should be on (OR output is 1)
        assertTrue(isLedOn(led))
        
        // Now connect feedback from OR back to AND input 1
        wireComponents(orGate, 0, andGate, 1)
        
        // Second simulation cycle (with feedback)
        // AND(1,1) -> NOT(1) -> 0
        // OR(0,1) -> 1
        simulateCircuit()
        
        // LED should still be on (OR output is 1)
        assertTrue(isLedOn(led))
        
        // Third simulation cycle
        // AND(1,1) -> NOT(1) -> 0
        // OR(0,1) -> 1
        simulateCircuit()
        
        // LED should remain on
        assertTrue(isLedOn(led))
    }
    
    /**
     * Edge case: Tests circuit with unconnected inputs (default to 0)
     */
    @Test
    fun `test circuit with unconnected inputs`() {
        // Prepare all components
        prepareCircuit()
        
        // Only connect NOT output to LED, leaving NOT input unconnected
        wireComponents(notGate, 0, led, 0)
        
        // Run simulation
        simulateCircuit()
        
        // NOT gate with unconnected input treats it as 0, so output is 1
        // LED should be ON
        assertTrue(isLedOn(led))
    }
    
    /**
     * Edge case: Test multiple connections to the same output port
     */
    @Test
    fun `test single output driving multiple inputs`() {
        // Prepare all components
        prepareCircuit()
        
        // Connect VCC1 to both AND gate inputs
        wireComponents(vcc1, 0, andGate, 0)
        wireComponents(vcc1, 0, andGate, 1)
        
        // Connect AND output to both OR gate inputs
        wireComponents(andGate, 0, orGate, 0)
        wireComponents(andGate, 0, orGate, 1)
        
        // Connect OR to LED
        wireComponents(orGate, 0, led, 0)
        
        // Run simulation
        simulateCircuit()
        
        // LED should be ON since AND(1,1)=1 and OR(1,1)=1
        assertTrue(isLedOn(led))
    }
    
    // Helper methods
    
    private fun prepareCircuit() {
        vcc1.prepareToStart()
        vcc2.prepareToStart()
        andGate.prepareToStart()
        notGate.prepareToStart()
        orGate.prepareToStart()
        led.prepareToStart()
    }
    
    private fun simulateCircuit() {
        // Simulate in order of signal flow
        vcc1.simulate()
        vcc2.simulate()
        andGate.simulate()
        notGate.simulate()
        orGate.simulate()
        led.simulate()
    }
    
    // Simulate wiring by directly transferring data between ports
    private fun wireComponents(
        sourceComponent: SimElement, sourcePortIndex: Int,
        destComponent: SimElement, destPortIndex: Int
    ) {
        // First check if source is an output port and dest is an input port
        if (sourceComponent.outputPorts.size > sourcePortIndex && 
            destComponent.inputPorts.size > destPortIndex) {
            
            // Get the signal from output port
            val signal = sourceComponent.outputPorts[sourcePortIndex].popData()
            // Push to input port
            destComponent.inputPorts[destPortIndex].pushData(signal)
            
        } else if (sourceComponent.inputPorts.size > sourcePortIndex &&
                  destComponent.inputPorts.size > destPortIndex) {
            
            // Get the signal from input port (passthrough for test)
            val signal = sourceComponent.inputPorts[sourcePortIndex].popData()
            if (signal != null) {
                // Push to input port
                destComponent.inputPorts[destPortIndex].pushData(signal)
            }
        }
    }
    
    private fun isLedOn(led: Led): Boolean {
        // Using reflection to access the private state
        val field = Led::class.java.getDeclaredField("state")
        field.isAccessible = true
        return field.getInt(led) == 1
    }
    
    // Mockito helpers
    private fun <T> whenever(methodCall: T): OngoingStubbing<T> {
        return `when`(methodCall)
    }
    
    private fun <T> any(): T {
        return Mockito.any()
    }
    
    private fun anyInt(): Int {
        return Mockito.anyInt()
    }
    
    interface OngoingStubbing<T> {
        fun thenReturn(value: T): OngoingStubbing<T>
        fun thenCallRealMethod(): OngoingStubbing<T>
    }
} 