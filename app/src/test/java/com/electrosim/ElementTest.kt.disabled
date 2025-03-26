package com.electrosim

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.MotionEvent
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
class ElementTest {

    @Mock
    private lateinit var container: Container

    @Mock
    private lateinit var bitmap: Bitmap

    @Mock
    private lateinit var canvas: Canvas

    @Mock
    private lateinit var motionEvent: MotionEvent

    private lateinit var testElement: TestElement

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        
        // Set up bitmap mocks
        whenever(bitmap.width).thenReturn(100)
        whenever(bitmap.height).thenReturn(100)
        
        // Set up motion event mocks
        whenever(motionEvent.x).thenReturn(150f)
        whenever(motionEvent.y).thenReturn(150f)
        
        testElement = TestElement(container)
    }

    @Test
    fun `test initialization of element`() {
        testElement.testInitialize(bitmap, 200f, 200f)
        
        assertEquals(200f, testElement.getX())
        assertEquals(200f, testElement.getY())
    }
    
    @Test
    fun `test inside rect check when point is inside`() {
        testElement.testInitialize(bitmap, 150f, 150f)
        // x = 150, y = 150, width = 100, height = 100
        // mX = 100, mY = 100 after calculation
        
        // Check a point inside the element (120, 120)
        whenever(motionEvent.x).thenReturn(120f)
        whenever(motionEvent.y).thenReturn(120f)
        
        assertTrue(testElement.insideRectCheck(120f, 120f))
    }
    
    @Test
    fun `test inside rect check when point is outside`() {
        testElement.testInitialize(bitmap, 150f, 150f)
        
        // Check a point outside the element (250, 250)
        assertFalse(testElement.insideRectCheck(250f, 250f))
    }
    
    @Test
    fun `test translate changes position correctly`() {
        testElement.testInitialize(bitmap, 100f, 100f)
        
        testElement.translate(50f, 50f)
        
        assertEquals(150f, testElement.getX())
        assertEquals(150f, testElement.getY())
    }
    
    @Test
    fun `test select and deselect changes hover state`() {
        testElement.testInitialize(bitmap, 150f, 150f)
        
        // Test select
        testElement.select(motionEvent)
        assertTrue(testElement.isHovering())
        
        // Test deselect
        testElement.deSelect(motionEvent)
        assertFalse(testElement.isHovering())
    }

    // A test implementation of Element for testing
    private inner class TestElement(container: Container) : Element(container) {
        
        fun testInitialize(bitmap: Bitmap, x: Float, y: Float): ElementInterface {
            return initiailizeElement(bitmap, x, y)
        }
        
        fun isHovering(): Boolean {
            return hover
        }
        
        override fun createNew(): ElementInterface {
            return this
        }
    }
} 