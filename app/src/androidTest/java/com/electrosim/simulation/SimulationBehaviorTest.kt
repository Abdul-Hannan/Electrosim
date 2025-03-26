package com.electrosim.simulation

import android.content.Context
import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.electrosim.R
import com.electrosim.component.*
import com.electrosim.connector.Connector
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import java.lang.reflect.Field
import java.lang.reflect.Method

@RunWith(AndroidJUnit4::class)
class SimulationBehaviorTest {

    private lateinit var appContext: Context
    private lateinit var bitmap: Bitmap
    private lateinit var container: Container
    
    @Before
    fun setup() {
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        
        // Create a small bitmap for testing
        bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)
        
        // Create a real container (not mocked)
        container = Container(appContext.resources)
        
        // Pre-load resources to ensure consistent test behavior
        container.preloadResource(R.drawable.uvcc)
        container.preloadResource(R.drawable.uand)
        container.preloadResource(R.drawable.unot)
        container.preloadResource(R.drawable.uor)
        container.preloadResource(R.drawable.uled_off)
        container.preloadResource(R.drawable.uled_on)
    }
    
    @Test
    fun testBasicAndGateSimulation() {
        // Create elements
        val vcc1 = Vcc(container)
        val vcc2 = Vcc(container)
        val andGate = AndGate(container, 2)
        val led = LED(container)
        
        // Position elements
        vcc1.posX = 100
        vcc1.posY = 100
        
        vcc2.posX = 100
        vcc2.posY = 200
        
        andGate.posX = 300
        andGate.posY = 150
        
        led.posX = 500
        led.posY = 150
        
        // Connect elements
        connectElements(vcc1, 0, andGate, 0)
        connectElements(vcc2, 0, andGate, 1)
        connectElements(andGate, 0, led, 0)
        
        // Run simulation
        val simulation = Simulation()
        simulation.addElement(vcc1)
        simulation.addElement(vcc2)
        simulation.addElement(andGate)
        simulation.addElement(led)
        
        simulation.prepareToStart()
        simulation.simulateStep()
        
        // Verify LED is on
        assert(isLEDOn(led))
    }
    
    @Test
    fun testNotGateSimulation() {
        // Create elements
        val vcc = Vcc(container)
        val notGate = NotGate(container)
        val led = LED(container)
        
        // Position elements
        vcc.posX = 100
        vcc.posY = 100
        
        notGate.posX = 300
        notGate.posY = 100
        
        led.posX = 500
        led.posY = 100
        
        // Connect elements
        connectElements(vcc, 0, notGate, 0)
        connectElements(notGate, 0, led, 0)
        
        // Run simulation
        val simulation = Simulation()
        simulation.addElement(vcc)
        simulation.addElement(notGate)
        simulation.addElement(led)
        
        simulation.prepareToStart()
        simulation.simulateStep()
        
        // Verify LED is off (NOT gate inverts the VCC)
        assert(!isLEDOn(led))
    }
    
    @Test
    fun testOrGateSimulation() {
        // Create elements with different inputs
        val vcc = Vcc(container)
        val orGate = OrGate(container, 2)
        val led = LED(container)
        
        // Position elements
        vcc.posX = 100
        vcc.posY = 100
        
        orGate.posX = 300
        orGate.posY = 150
        
        led.posX = 500
        led.posY = 150
        
        // Connect only one input of OR gate
        connectElements(vcc, 0, orGate, 0)
        connectElements(orGate, 0, led, 0)
        
        // Run simulation
        val simulation = Simulation()
        simulation.addElement(vcc)
        simulation.addElement(orGate)
        simulation.addElement(led)
        
        simulation.prepareToStart()
        simulation.simulateStep()
        
        // Verify LED is on (OR gate with one high input)
        assert(isLEDOn(led))
    }
    
    @Test
    fun testXorGateSimulation() {
        // Create elements with both inputs high
        val vcc1 = Vcc(container)
        val vcc2 = Vcc(container)
        val xorGate = XorGate(container, 2)
        val led = LED(container)
        
        // Position elements
        vcc1.posX = 100
        vcc1.posY = 100
        
        vcc2.posX = 100
        vcc2.posY = 200
        
        xorGate.posX = 300
        xorGate.posY = 150
        
        led.posX = 500
        led.posY = 150
        
        // Connect elements
        connectElements(vcc1, 0, xorGate, 0)
        connectElements(vcc2, 0, xorGate, 1)
        connectElements(xorGate, 0, led, 0)
        
        // Run simulation
        val simulation = Simulation()
        simulation.addElement(vcc1)
        simulation.addElement(vcc2)
        simulation.addElement(xorGate)
        simulation.addElement(led)
        
        simulation.prepareToStart()
        simulation.simulateStep()
        
        // Verify LED is off (XOR gate with both inputs high)
        assert(!isLEDOn(led))
    }
    
    @Test
    fun testComplexCircuit() {
        // Create elements for (A AND B) OR (NOT C)
        val vccA = Vcc(container)
        val vccB = Vcc(container)
        val vccC = Vcc(container)
        val andGate = AndGate(container, 2)
        val notGate = NotGate(container)
        val orGate = OrGate(container, 2)
        val led = LED(container)
        
        // Position elements
        vccA.posX = 50
        vccA.posY = 50
        
        vccB.posX = 50
        vccB.posY = 150
        
        vccC.posX = 50
        vccC.posY = 250
        
        andGate.posX = 200
        andGate.posY = 100
        
        notGate.posX = 200
        notGate.posY = 250
        
        orGate.posX = 350
        orGate.posY = 175
        
        led.posX = 500
        led.posY = 175
        
        // Connect elements
        connectElements(vccA, 0, andGate, 0)
        connectElements(vccB, 0, andGate, 1)
        connectElements(vccC, 0, notGate, 0)
        connectElements(andGate, 0, orGate, 0)
        connectElements(notGate, 0, orGate, 1)
        connectElements(orGate, 0, led, 0)
        
        // Run simulation
        val simulation = Simulation()
        simulation.addElement(vccA)
        simulation.addElement(vccB)
        simulation.addElement(vccC)
        simulation.addElement(andGate)
        simulation.addElement(notGate)
        simulation.addElement(orGate)
        simulation.addElement(led)
        
        simulation.prepareToStart()
        simulation.simulateStep()
        
        // (1 AND 1) OR (NOT 1) = 1 OR 0 = 1, LED should be on
        assert(isLEDOn(led))
    }
    
    @Test
    fun testContinuousSimulation() {
        // Create a simple circuit
        val vcc = Vcc(container)
        val led = LED(container)
        
        // Position elements
        vcc.posX = 100
        vcc.posY = 100
        
        led.posX = 300
        led.posY = 100
        
        // Connect elements
        connectElements(vcc, 0, led, 0)
        
        // Run simulation for multiple steps
        val simulation = Simulation()
        simulation.addElement(vcc)
        simulation.addElement(led)
        
        simulation.prepareToStart()
        
        // Run multiple steps and check LED state each time
        for (i in 0 until 5) {
            simulation.simulateStep()
            assert(isLEDOn(led))
        }
    }
    
    // Helper methods
    
    private fun connectElements(source: Element, sourcePortIndex: Int, target: Element, targetPortIndex: Int) {
        val connector = Connector()
        
        // Use reflection to access source element's outputs
        val sourceOutputsField = Element::class.java.getDeclaredField("outputs")
        sourceOutputsField.isAccessible = true
        val sourceOutputs = sourceOutputsField.get(source) as Array<*>
        
        // Use reflection to access target element's inputs
        val targetInputsField = Element::class.java.getDeclaredField("inputs")
        targetInputsField.isAccessible = true
        val targetInputs = targetInputsField.get(target) as Array<*>
        
        // Set the connector's source and target
        val sourcePortField = sourceOutputs[sourcePortIndex].javaClass.getDeclaredField("connector")
        sourcePortField.isAccessible = true
        sourcePortField.set(sourceOutputs[sourcePortIndex], connector)
        
        val targetPortField = targetInputs[targetPortIndex].javaClass.getDeclaredField("connector")
        targetPortField.isAccessible = true
        targetPortField.set(targetInputs[targetPortIndex], connector)
        
        // Set connector's source and target
        val connectorSourceField = Connector::class.java.getDeclaredField("sourceElement")
        connectorSourceField.isAccessible = true
        connectorSourceField.set(connector, source)
        
        val connectorSourcePortField = Connector::class.java.getDeclaredField("sourcePort")
        connectorSourcePortField.isAccessible = true
        connectorSourcePortField.set(connector, sourcePortIndex)
        
        val connectorTargetField = Connector::class.java.getDeclaredField("targetElement")
        connectorTargetField.isAccessible = true
        connectorTargetField.set(connector, target)
        
        val connectorTargetPortField = Connector::class.java.getDeclaredField("targetPort")
        connectorTargetPortField.isAccessible = true
        connectorTargetPortField.set(connector, targetPortIndex)
    }
    
    private fun isLEDOn(led: LED): Boolean {
        // Use reflection to access the LED's state
        val stateField = LED::class.java.getDeclaredField("state")
        stateField.isAccessible = true
        return stateField.getInt(led) != 0
    }
} 