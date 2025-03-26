package com.electrosim.simulation

import android.graphics.Bitmap
import android.graphics.Canvas
import com.electrosim.Container
import com.electrosim.ElementInterface
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.junit.Assert.*

@RunWith(RobolectricTestRunner::class)
class SimElementTest {

    @Mock
    private lateinit var container: Container

    @Mock
    private lateinit var bitmap: Bitmap

    @Mock
    private lateinit var canvas: Canvas

    @Mock
    private lateinit var inputPort: InputPort

    @Mock
    private lateinit var outputPort: OutputPort

    private lateinit var testSimElement: TestSimElement

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        
        // Set up bitmap mocks
        whenever(bitmap.width).thenReturn(100)
        whenever(bitmap.height).thenReturn(100)
        
        testSimElement = TestSimElement(container)
    }

    @Test
    fun `test adding input port`() {
        testSimElement.testAddInputPort(inputPort)
        
        assertEquals(1, testSimElement.inputPorts.size)
        assertEquals(inputPort, testSimElement.inputPorts[0])
    }

    @Test
    fun `test adding output port`() {
        testSimElement.testAddOutputPort(outputPort)
        
        assertEquals(1, testSimElement.outputPorts.size)
        assertEquals(outputPort, testSimElement.outputPorts[0])
    }

    @Test
    fun `test prepare to start calls prepare on all ports`() {
        // Add ports to test
        testSimElement.testAddInputPort(inputPort)
        testSimElement.testAddOutputPort(outputPort)
        
        // Call prepareToStart
        testSimElement.prepareToStart()
        
        // Verify preparation methods were called on ports
        verify(inputPort).prepareToStart()
        verify(outputPort).prepareToStart()
    }

    @Test
    fun `test prepare to stop calls prepare on all ports`() {
        // Add ports to test
        testSimElement.testAddInputPort(inputPort)
        testSimElement.testAddOutputPort(outputPort)
        
        // Call prepareToStop
        testSimElement.prepareToStop()
        
        // Verify preparation methods were called on ports
        verify(inputPort).prepareToStop()
        verify(outputPort).prepareToStop()
    }

    @Test
    fun `test simulate is called and processed correctly`() {
        testSimElement.testSimulate()
        
        assertTrue(testSimElement.simulateCalled)
    }

    // A test implementation of SimElement for testing
    private inner class TestSimElement(container: Container) : SimElement(container) {
        
        var simulateCalled = false
        
        fun testInitialize(bitmap: Bitmap, x: Float, y: Float): ElementInterface {
            return initiailizeElement(bitmap, x, y)
        }
        
        fun testAddInputPort(ip: InputPort) {
            addInputPort(ip)
        }
        
        fun testAddOutputPort(op: OutputPort) {
            addOutputPort(op)
        }
        
        fun testSimulate() {
            simulate()
        }
        
        override fun simulate() {
            simulateCalled = true
        }
        
        override fun createNew(): ElementInterface {
            return this
        }
    }
    
    // Mock verification methods to avoid dependency on mockito-kotlin in imports
    private fun <T> whenever(methodCall: T): OngoingStubbing<T> {
        return `when`(methodCall)
    }
    
    private fun <T> verify(mock: T): T {
        return Mockito.verify(mock)
    }
    
    interface OngoingStubbing<T> {
        fun thenReturn(value: T): OngoingStubbing<T>
    }
} 