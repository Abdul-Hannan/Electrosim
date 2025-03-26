package com.electrosim.component

import android.content.res.Resources
import android.graphics.BitmapFactory
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
class VccTest {

    @Mock
    private lateinit var container: Container

    @Mock
    private lateinit var res: Resources

    @Mock
    private lateinit var bitmap: Any

    private lateinit var vcc: Vcc
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        
        // Mock resource loading
        whenever(BitmapFactory.decodeResource(res, R.drawable.uvcc)).thenReturn(bitmap)
        
        // Create Vcc power source
        vcc = Vcc(100f, 100f, container, 1, res)
    }

    @Test
    fun `test Vcc initialization`() {
        // Verify it has 0 input ports and 1 output port
        assertEquals(0, vcc.inputPorts.size)
        assertEquals(1, vcc.outputPorts.size)
    }

    @Test
    fun `test Vcc produces high output`() {
        // Prepare
        vcc.prepareToStart()
        
        // Simulate
        vcc.simulate()
        
        // Check output is high (1)
        val outputData = vcc.outputPorts[0].popData()
        assertEquals(1, outputData)
    }

    @Test
    fun `test Vcc always produces high output on continuous simulation`() {
        // Prepare
        vcc.prepareToStart()
        
        // First simulation
        vcc.simulate()
        assertEquals(1, vcc.outputPorts[0].popData())
        
        // Second simulation
        vcc.simulate()
        assertEquals(1, vcc.outputPorts[0].popData())
        
        // Third simulation
        vcc.simulate()
        assertEquals(1, vcc.outputPorts[0].popData())
    }

    @Test
    fun `test Vcc creates new instance`() {
        val newVcc = vcc.createNew() as Vcc
        
        // Check it has the same port configuration
        assertEquals(vcc.inputPorts.size, newVcc.inputPorts.size)
        assertEquals(vcc.outputPorts.size, newVcc.outputPorts.size)
    }
    
    @Test
    fun `test Vcc output is available after prepare to start`() {
        // First stop any previous simulation
        vcc.prepareToStop()
        
        // Then prepare to start again
        vcc.prepareToStart()
        
        // Simulate
        vcc.simulate()
        
        // Check output is high (1)
        assertEquals(1, vcc.outputPorts[0].popData())
    }
    
    // Mock verification methods to avoid dependency on mockito-kotlin in imports
    private fun <T> whenever(methodCall: T): OngoingStubbing<T> {
        return `when`(methodCall)
    }
    
    interface OngoingStubbing<T> {
        fun thenReturn(value: T): OngoingStubbing<T>
    }
} 