package com.electrosim

import android.graphics.Canvas
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
class ConnectorTest {

    @Mock
    private lateinit var pointA: LinkPointInterface

    @Mock
    private lateinit var pointB: LinkPointInterface

    @Mock
    private lateinit var canvas: Canvas

    private lateinit var connector: Connector

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        
        // Setup the link points with positions
        whenever(pointA.getX()).thenReturn(100f)
        whenever(pointA.getY()).thenReturn(100f)
        
        whenever(pointB.getX()).thenReturn(200f)
        whenever(pointB.getY()).thenReturn(200f)
        
        connector = Connector()
    }

    @Test
    fun `test setting and getting link points`() {
        // Set point A
        assertTrue(connector.setLinkPointA(pointA))
        assertEquals(pointA, connector.getLinkPointA())
        
        // Set point B
        assertTrue(connector.setLinkPointB(pointB))
        assertEquals(pointB, connector.getLinkPointB())
    }
    
    @Test
    fun `test adding breakpoint to connector`() {
        // Set the link points
        connector.setLinkPointA(pointA)
        connector.setLinkPointB(pointB)
        
        // Create a breakpoint
        val breakpoint = LineBreakpoint(150f, 150f, connector)
        
        // Add the breakpoint
        connector.addLinkBreakpoint(breakpoint)
        
        // Verify breakpoint was added by checking if point is on line
        assertTrue(connector.pointOnLine(150f, 150f))
    }
    
    @Test
    fun `test point on line detection - straight line`() {
        // Set the link points for a straight line from 100,100 to 200,200
        connector.setLinkPointA(pointA)
        connector.setLinkPointB(pointB)
        
        // Test a point on the line (150,150)
        assertTrue(connector.pointOnLine(150f, 150f))
        
        // Test a point not on the line (160,150)
        assertFalse(connector.pointOnLine(160f, 150f))
    }
    
    @Test
    fun `test point on line detection - vertical line`() {
        // Setup for a vertical line (100,100 to 100,200)
        whenever(pointA.getX()).thenReturn(100f)
        whenever(pointA.getY()).thenReturn(100f)
        
        whenever(pointB.getX()).thenReturn(100f)
        whenever(pointB.getY()).thenReturn(200f)
        
        connector.setLinkPointA(pointA)
        connector.setLinkPointB(pointB)
        
        // Test a point on the line (100,150)
        assertTrue(connector.pointOnLine(100f, 150f))
        
        // Test a point not on the line (110,150)
        assertFalse(connector.pointOnLine(110f, 150f))
    }
    
    @Test
    fun `test draw method calls with and without points set`() {
        // Without points set, draw should not throw exception
        connector.draw(canvas)
        
        // Set the points and test draw again
        connector.setLinkPointA(pointA)
        connector.setLinkPointB(pointB)
        connector.draw(canvas)
        
        // Test draw with cursor position
        connector.draw(canvas, 150f, 150f)
    }
} 