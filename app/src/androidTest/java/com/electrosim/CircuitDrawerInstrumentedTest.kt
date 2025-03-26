package com.electrosim

import android.content.Intent
import android.view.MotionEvent
import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CircuitDrawerInstrumentedTest {

    @Before
    fun setup() {
        // Clear any application state if needed
    }

    @Test
    fun testCircuitDrawerLaunchesCorrectly() {
        // Create intent with the necessary extras
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            CircuitDrawer::class.java
        ).apply {
            // Add any required extras
        }

        // Launch CircuitDrawer
        ActivityScenario.launch<CircuitDrawer>(intent).use { scenario ->
            // Verify that the circuit drawer surface is displayed
            onView(withId(R.id.drawingarea))
                .check(matches(isDisplayed()))
            
            // Verify that the toolbar/buttons are displayed
            onView(withId(R.id.tools))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun testToolSelectionAndElementPlacement() {
        // Launch CircuitDrawer
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            CircuitDrawer::class.java
        )
        ActivityScenario.launch<CircuitDrawer>(intent).use { scenario ->
            // Click on the AND gate tool button
            onView(withId(R.id.andgate))
                .perform(click())
            
            // Verify the element selection dialog appears
            onView(withId(R.id.gatedialog))
                .check(matches(isDisplayed()))
            
            // Select a 2-input AND gate
            onView(withText("2 Input"))
                .perform(click())
            
            // Verify the gate is selected (implementation-dependent)
            // This would require custom verification based on app state
            
            // Now simulate touching the drawing area to place the gate
            onView(withId(R.id.drawingarea))
                .perform(clickInMiddleOfView())
                
            // Could add verification that element was placed - this depends on app implementation
        }
    }
    
    @Test
    fun testConnectionCreation() {
        // Launch CircuitDrawer with pre-populated elements (if possible)
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            CircuitDrawer::class.java
        )
        ActivityScenario.launch<CircuitDrawer>(intent).use { scenario ->
            // First place a power source (VCC)
            onView(withId(R.id.vcc))
                .perform(click())
            onView(withId(R.id.drawingarea))
                .perform(clickXY(100, 100))
                
            // Then place an AND gate
            onView(withId(R.id.andgate))
                .perform(click())
            onView(withText("2 Input"))
                .perform(click())
            onView(withId(R.id.drawingarea))
                .perform(clickXY(300, 100))
                
            // Now try to connect them:
            // Touch the output of VCC
            onView(withId(R.id.drawingarea))
                .perform(touchXY(150, 100))
            
            // Drag to input of AND gate
            onView(withId(R.id.drawingarea))
                .perform(dragFromTo(150, 100, 250, 80))
                
            // Verification would be implementation-specific
        }
    }
    
    @Test
    fun testDeletionMode() {
        // Launch CircuitDrawer
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            CircuitDrawer::class.java
        )
        ActivityScenario.launch<CircuitDrawer>(intent).use { scenario ->
            // First place a component
            onView(withId(R.id.vcc))
                .perform(click())
            onView(withId(R.id.drawingarea))
                .perform(clickXY(200, 200))
            
            // Enable deletion mode
            onView(withId(R.id.delete))
                .perform(click())
            
            // Delete the component
            onView(withId(R.id.drawingarea))
                .perform(clickXY(200, 200))
                
            // Verification would depend on app state
        }
    }
    
    @Test
    fun testMenuFunctionality() {
        // Launch CircuitDrawer
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            CircuitDrawer::class.java
        )
        ActivityScenario.launch<CircuitDrawer>(intent).use { scenario ->
            // Test menu button
            onView(withId(R.id.menubtn))
                .check(matches(isDisplayed()))
                .perform(click())
            
            // Check that menu options appear
            // Implementation dependent - would need to match specific menu items
        }
    }
    
    @Test
    fun testZoomPanFunctionality() {
        // Launch CircuitDrawer
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            CircuitDrawer::class.java
        )
        ActivityScenario.launch<CircuitDrawer>(intent).use { scenario ->
            // This is difficult to test with Espresso as it involves complex gestures
            // Typically would require custom touch actions or use of UiAutomator
            
            // Example of a pan gesture (but may need adjusting for app specifics)
            onView(withId(R.id.drawingarea))
                .perform(dragFromTo(200, 200, 400, 400))
        }
    }
    
    // Custom ViewAction to click in the middle of a view
    private fun clickInMiddleOfView(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isDisplayed()
            }

            override fun getDescription(): String {
                return "Click in middle of view"
            }

            override fun perform(uiController: UiController, view: View) {
                val x = view.width / 2
                val y = view.height / 2
                
                val downTime = System.currentTimeMillis()
                val eventTime = downTime + 100
                
                val motionEventDown = MotionEvent.obtain(
                    downTime, eventTime, MotionEvent.ACTION_DOWN, x.toFloat(), y.toFloat(), 0
                )
                val motionEventUp = MotionEvent.obtain(
                    downTime, eventTime + 100, MotionEvent.ACTION_UP, x.toFloat(), y.toFloat(), 0
                )
                
                view.dispatchTouchEvent(motionEventDown)
                uiController.loopMainThreadForAtLeast(100)
                view.dispatchTouchEvent(motionEventUp)
            }
        }
    }
    
    // Custom ViewAction to click at specific coordinates
    private fun clickXY(x: Int, y: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isDisplayed()
            }

            override fun getDescription(): String {
                return "Click at position ($x, $y)"
            }

            override fun perform(uiController: UiController, view: View) {
                val downTime = System.currentTimeMillis()
                val eventTime = downTime + 100
                
                val motionEventDown = MotionEvent.obtain(
                    downTime, eventTime, MotionEvent.ACTION_DOWN, x.toFloat(), y.toFloat(), 0
                )
                val motionEventUp = MotionEvent.obtain(
                    downTime, eventTime + 100, MotionEvent.ACTION_UP, x.toFloat(), y.toFloat(), 0
                )
                
                view.dispatchTouchEvent(motionEventDown)
                uiController.loopMainThreadForAtLeast(100)
                view.dispatchTouchEvent(motionEventUp)
            }
        }
    }
    
    // Custom ViewAction to touch at specific coordinates
    private fun touchXY(x: Int, y: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isDisplayed()
            }

            override fun getDescription(): String {
                return "Touch at position ($x, $y)"
            }

            override fun perform(uiController: UiController, view: View) {
                val downTime = System.currentTimeMillis()
                val eventTime = downTime + 100
                
                val motionEventDown = MotionEvent.obtain(
                    downTime, eventTime, MotionEvent.ACTION_DOWN, x.toFloat(), y.toFloat(), 0
                )
                
                view.dispatchTouchEvent(motionEventDown)
                uiController.loopMainThreadForAtLeast(100)
            }
        }
    }
    
    // Custom ViewAction to drag from one position to another
    private fun dragFromTo(fromX: Int, fromY: Int, toX: Int, toY: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isDisplayed()
            }

            override fun getDescription(): String {
                return "Drag from ($fromX, $fromY) to ($toX, $toY)"
            }

            override fun perform(uiController: UiController, view: View) {
                val downTime = System.currentTimeMillis()
                
                // Initial touch
                val eventTimeDown = downTime
                val motionEventDown = MotionEvent.obtain(
                    downTime, eventTimeDown, MotionEvent.ACTION_DOWN, fromX.toFloat(), fromY.toFloat(), 0
                )
                view.dispatchTouchEvent(motionEventDown)
                uiController.loopMainThreadForAtLeast(100)
                
                // Move events (simulate drag)
                val steps = 10
                for (i in 1 until steps) {
                    val ratio = i.toFloat() / steps
                    val eventTimeMove = downTime + (100 * i)
                    val x = fromX + (toX - fromX) * ratio
                    val y = fromY + (toY - fromY) * ratio
                    
                    val motionEventMove = MotionEvent.obtain(
                        downTime, eventTimeMove, MotionEvent.ACTION_MOVE, x, y, 0
                    )
                    view.dispatchTouchEvent(motionEventMove)
                    uiController.loopMainThreadForAtLeast(20)
                }
                
                // Final position and release
                val eventTimeUp = downTime + 1000
                val motionEventUp = MotionEvent.obtain(
                    downTime, eventTimeUp, MotionEvent.ACTION_UP, toX.toFloat(), toY.toFloat(), 0
                )
                view.dispatchTouchEvent(motionEventUp)
                uiController.loopMainThreadForAtLeast(100)
            }
        }
    }
} 