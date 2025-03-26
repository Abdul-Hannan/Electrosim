package com.electrosim.component

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Canvas
import com.electrosim.Container
import com.electrosim.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.junit.Assert.*

@RunWith(RobolectricTestRunner::class)
class LedTest {

    @Mock
    private lateinit var container: Container

    @Mock
    private lateinit var res: Resources

    @Mock
    private lateinit var bitmap: Any
    
    @Mock
    private lateinit var bitmapOn: Any
    
    @Mock
    private lateinit var canvas: Canvas

    private lateinit var led: Led
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        
        // Mock resource loading
        whenever(BitmapFactory.decodeResource(res, R.drawable.uled)).thenReturn(bitmap)
        whenever(BitmapFactory.decodeResource(res, R.drawable.uled1)).thenReturn(bitmapOn)
        
        // Create LED
        led = Led(100f, 100f, container, 1, res)
    }

    @Test
    fun `test LED initialization`() {
        // Verify it has 1 input port and 0 output ports
        assertEquals(1, led.inputPorts.size)
        assertEquals(0, led.outputPorts.size)
        
        // Verify initial state (LED should be off)
        assertFalse(led.isOn())
    }

    @Test
    fun `test LED turns on with high input`() {
        // Prepare
        led.prepareToStart()
        
        // Feed high signal to LED
        led.inputPorts[0].pushData(1)
        
        // Simulate
        led.simulate()
        
        // Check LED is on
        assertTrue(led.isOn())
    }

    @Test
    fun `test LED turns off with low input`() {
        // First turn it on
        led.prepareToStart()
        led.inputPorts[0].pushData(1)
        led.simulate()
        assertTrue(led.isOn())
        
        // Then feed low signal
        led.prepareToStart()
        led.inputPorts[0].pushData(0)
        led.simulate()
        
        // Check LED is off
        assertFalse(led.isOn())
    }
    
    @Test
    fun `test LED stays on until next simulation cycle`() {
        // Turn it on
        led.prepareToStart()
        led.inputPorts[0].pushData(1)
        led.simulate()
        assertTrue(led.isOn())
        
        // Don't feed any new input this cycle
        led.simulate()
        
        // LED should still be on from previous cycle
        assertTrue(led.isOn())
    }
    
    @Test
    fun `test LED returns to off state after prepare to stop`() {
        // Turn it on
        led.prepareToStart()
        led.inputPorts[0].pushData(1)
        led.simulate()
        assertTrue(led.isOn())
        
        // Call prepareToStop
        led.prepareToStop()
        
        // Check LED is off
        assertFalse(led.isOn())
    }

    @Test
    fun `test createNew returns a new instance with same properties`() {
        val newLed = led.createNew() as Led
        
        // Check it has the same number of ports
        assertEquals(led.inputPorts.size, newLed.inputPorts.size)
        assertEquals(0, newLed.outputPorts.size)
    }
    
    // Helper extension function to access the private state of the LED
    private fun Led.isOn(): Boolean {
        // Using reflection to access the private state
        val field = Led::class.java.getDeclaredField("state")
        field.isAccessible = true
        return field.getInt(this) == 1
    }
    
    // Mock verification methods to avoid dependency on mockito-kotlin in imports
    private fun <T> whenever(methodCall: T): OngoingStubbing<T> {
        return `when`(methodCall)
    }
    
    interface OngoingStubbing<T> {
        fun thenReturn(value: T): OngoingStubbing<T>
    }
} 