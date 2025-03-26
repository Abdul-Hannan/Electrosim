package com.electrosim

import android.view.MotionEvent
import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import org.hamcrest.Matcher

/**
 * Utility class providing custom view actions for instrumented tests
 */
object CustomViewActions {
    
    /**
     * Performs a click at the exact coordinates specified
     */
    fun clickXY(x: Int, y: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isDisplayed()
            }

            override fun getDescription(): String {
                return "Click at coordinates ($x, $y)"
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

    /**
     * Performs a drag operation from one set of coordinates to another
     */
    fun dragFromTo(fromX: Int, fromY: Int, toX: Int, toY: Int): ViewAction {
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
    
    /**
     * Performs a long press at the given coordinates
     */
    fun longPressAt(x: Int, y: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isDisplayed()
            }

            override fun getDescription(): String {
                return "Long press at coordinates ($x, $y)"
            }

            override fun perform(uiController: UiController, view: View) {
                val downTime = System.currentTimeMillis()
                
                val motionEventDown = MotionEvent.obtain(
                    downTime, downTime, MotionEvent.ACTION_DOWN, x.toFloat(), y.toFloat(), 0
                )
                view.dispatchTouchEvent(motionEventDown)
                
                // Hold for 1.5 seconds (standard long press time)
                uiController.loopMainThreadForAtLeast(1500)
                
                val eventTimeUp = downTime + 1500
                val motionEventUp = MotionEvent.obtain(
                    downTime, eventTimeUp, MotionEvent.ACTION_UP, x.toFloat(), y.toFloat(), 0
                )
                view.dispatchTouchEvent(motionEventUp)
            }
        }
    }
    
    /**
     * Performs a double-tap at the specified coordinates
     */
    fun doubleTapAt(x: Int, y: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isDisplayed()
            }

            override fun getDescription(): String {
                return "Double tap at coordinates ($x, $y)"
            }

            override fun perform(uiController: UiController, view: View) {
                val downTime = System.currentTimeMillis()
                
                // First tap
                val eventTime1 = downTime
                val motionEventDown1 = MotionEvent.obtain(
                    downTime, eventTime1, MotionEvent.ACTION_DOWN, x.toFloat(), y.toFloat(), 0
                )
                val motionEventUp1 = MotionEvent.obtain(
                    downTime, eventTime1 + 50, MotionEvent.ACTION_UP, x.toFloat(), y.toFloat(), 0
                )
                
                view.dispatchTouchEvent(motionEventDown1)
                uiController.loopMainThreadForAtLeast(50)
                view.dispatchTouchEvent(motionEventUp1)
                
                // Short pause between taps
                uiController.loopMainThreadForAtLeast(100)
                
                // Second tap
                val eventTime2 = downTime + 150
                val motionEventDown2 = MotionEvent.obtain(
                    downTime, eventTime2, MotionEvent.ACTION_DOWN, x.toFloat(), y.toFloat(), 0
                )
                val motionEventUp2 = MotionEvent.obtain(
                    downTime, eventTime2 + 50, MotionEvent.ACTION_UP, x.toFloat(), y.toFloat(), 0
                )
                
                view.dispatchTouchEvent(motionEventDown2)
                uiController.loopMainThreadForAtLeast(50)
                view.dispatchTouchEvent(motionEventUp2)
            }
        }
    }
    
    /**
     * Performs a multi-touch pinch gesture (for zoom out)
     */
    fun pinchClose(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isDisplayed()
            }

            override fun getDescription(): String {
                return "Pinch close gesture for zooming out"
            }

            override fun perform(uiController: UiController, view: View) {
                // Note: Multi-touch events are complex to simulate properly
                // This is a simplified version that might need refinement for actual use
                
                val centerX = view.width / 2f
                val centerY = view.height / 2f
                val downdTime = System.currentTimeMillis()
                
                // Start with fingers apart
                val startDistance = 300f
                
                // Simulate first pointer
                val x1Start = centerX - startDistance/2
                val y1Start = centerY
                
                // Simulate second pointer
                val x2Start = centerX + startDistance/2
                val y2Start = centerY
                
                // End positions (fingers together)
                val endDistance = 100f
                
                val x1End = centerX - endDistance/2
                val y1End = centerY
                
                val x2End = centerX + endDistance/2
                val y2End = centerY
                
                // Simulate a multi-touch pinch
                // This is very simplified and may not work on all devices
                // For proper multi-touch testing, consider UiAutomator
                val steps = 10
                for (i in 0 until steps) {
                    val ratio = i.toFloat() / steps
                    val eventTime = downdTime + i * 50
                    
                    val x1 = x1Start + (x1End - x1Start) * ratio
                    val y1 = y1Start + (y1End - y1Start) * ratio
                    
                    val x2 = x2Start + (x2End - x2Start) * ratio
                    
                    // Create pointer properties and coordinates
                    val properties = arrayOf(
                        MotionEvent.PointerProperties().apply { id = 0; toolType = MotionEvent.TOOL_TYPE_FINGER },
                        MotionEvent.PointerProperties().apply { id = 1; toolType = MotionEvent.TOOL_TYPE_FINGER }
                    )
                    
                    val pointerCoords = arrayOf(
                        MotionEvent.PointerCoords().apply { x = x1; y = y1; pressure = 1f; size = 1f },
                        MotionEvent.PointerCoords().apply { x = x2; y = y2Start; pressure = 1f; size = 1f }
                    )
                    
                    val action = if (i == 0) {
                        MotionEvent.ACTION_DOWN
                    } else if (i == steps - 1) {
                        MotionEvent.ACTION_UP
                    } else {
                        MotionEvent.ACTION_MOVE
                    }
                    
                    val motionEvent = MotionEvent.obtain(
                        downdTime, eventTime, action, 2, properties, pointerCoords, 
                        0, 0, 1f, 1f, 0, 0, MotionEvent.EDGE_FLAG_NONE, 0
                    )
                    
                    view.dispatchTouchEvent(motionEvent)
                    uiController.loopMainThreadForAtLeast(50)
                }
            }
        }
    }
} 