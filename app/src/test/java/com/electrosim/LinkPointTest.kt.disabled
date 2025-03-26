package com.electrosim

import android.graphics.Canvas
import android.view.MotionEvent
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.junit.Assert.*

@RunWith(RobolectricTestRunner::class)
class LinkPointTest {

    @Mock
    private lateinit var elementInterface: ElementInterface

    @Mock
    private lateinit var motionEvent: MotionEvent

    @Mock
    private lateinit var canvas: Canvas

    @Mock
    private lateinit var connector: ConnectorInterface

    private lateinit var linkPoint: LinkPoint

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        
        // Mock element position
        whenever(elementInterface.getX()).thenReturn(100f)
        whenever(elementInterface.getY()).thenReturn(100f)
        
        // Create linkpoint at offset 20,20 from element with radius 10
        linkPoint = LinkPoint(20f, 20f, 10f, elementInterface)
    }

    @Test
    fun `test getX returns correct world position`() {
        // LinkPoint is at offset 20,20 from element at position 100,100
        assertEquals(120f, linkPoint.getX())
    }

    @Test
    fun `test getY returns correct world position`() {
        assertEquals(120f, linkPoint.getY())
    }

    @Test
    fun `test point hit test when point is within radius`() {
        // Point at 121,121 is within radius 10 of linkpoint at 120,120
        whenever(motionEvent.x).thenReturn(121f)
        whenever(motionEvent.y).thenReturn(121f)
        
        assertTrue(linkPoint.pointHitTest(motionEvent))
    }

    @Test
    fun `test point hit test when point is outside radius`() {
        // Point at 135,135 is outside radius 10 of linkpoint at 120,120
        whenever(motionEvent.x).thenReturn(135f)
        whenever(motionEvent.y).thenReturn(135f)
        
        assertFalse(linkPoint.pointHitTest(motionEvent))
    }

    @Test
    fun `test point hit test returns false when connector is set`() {
        // Set a connector
        linkPoint.setConnector(connector)
        
        // Even if point is within radius, should return false
        whenever(motionEvent.x).thenReturn(121f)
        whenever(motionEvent.y).thenReturn(121f)
        
        assertFalse(linkPoint.pointHitTest(motionEvent))
    }

    @Test
    fun `test setConnector and then set to null`() {
        // Set a connector
        linkPoint.setConnector(connector)
        
        // Test that a point hit test returns false (connector is set)
        whenever(motionEvent.x).thenReturn(121f)
        whenever(motionEvent.y).thenReturn(121f)
        assertFalse(linkPoint.pointHitTest(motionEvent))
        
        // Clear the connector
        linkPoint.setConnector(null)
        
        // Now the point hit test should work
        assertTrue(linkPoint.pointHitTest(motionEvent))
    }
} 