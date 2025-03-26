package com.electrosim

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.CoreMatchers.containsString
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class SaveLoadCircuitTest {
    
    private val testCircuitName = "test_circuit_${System.currentTimeMillis()}"
    private var testCircuitFile: File? = null
    
    @Before
    fun setup() {
        // Ensure we have clean state for testing
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val filesDir = context.filesDir
        testCircuitFile = File(filesDir, "$testCircuitName.dat")
        
        // Delete test file if it exists from previous test run
        if (testCircuitFile?.exists() == true) {
            testCircuitFile?.delete()
        }
    }
    
    @After
    fun cleanup() {
        // Clean up test file
        testCircuitFile?.delete()
    }
    
    @Test
    fun testSaveCircuit() {
        // Create intent to launch CircuitDrawer
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            CircuitDrawer::class.java
        )
        
        ActivityScenario.launch<CircuitDrawer>(intent).use { scenario ->
            // First create a simple circuit - place a power source
            onView(withId(R.id.vcc)).perform(click())
            onView(withId(R.id.drawingarea)).perform(TouchActions.clickXY(100, 100))
            
            // Place an LED
            onView(withId(R.id.led)).perform(click())
            onView(withId(R.id.drawingarea)).perform(TouchActions.clickXY(300, 100))
            
            // Connect them
            onView(withId(R.id.drawingarea)).perform(
                TouchActions.dragFromTo(150, 100, 250, 100)
            )
            
            // Open the menu to save
            onView(withId(R.id.menubtn)).perform(click())
            
            // Click on save option
            onView(withText(R.string.save_circuit)).perform(click())
            
            // Enter test circuit name
            onView(withId(R.id.filename))
                .check(matches(isDisplayed()))
                .perform(replaceText(testCircuitName))
            
            // Click Save button
            onView(withId(R.id.save_button)).perform(click())
            
            // Verify save was successful by checking for success toast or dialog
            // This depends on implementation, but could look for toast message:
            onView(withText(containsString("saved")))
                .check(matches(isDisplayed()))
            
            // Verify file exists on disk
            assert(testCircuitFile?.exists() == true)
        }
    }
    
    @Test
    fun testLoadCircuit() {
        // First we need to ensure a circuit exists to load
        testSaveCircuit()
        
        // Now launch MainActivity to test loading
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            // Click on Load Circuit
            onView(withId(R.id.loadcircuit)).perform(click())
            
            // Find and click on our test circuit in the list
            // This depends on how your file chooser is implemented
            // If it's a ListView/RecyclerView of files:
            onView(withText(testCircuitName))
                .check(matches(isDisplayed()))
                .perform(click())
            
            // Verify CircuitDrawer opens with the loaded circuit
            // This could check for specific elements being present
            // or just verify we're in CircuitDrawer with some content
            onView(withId(R.id.drawingarea))
                .check(matches(isDisplayed()))
            
            // Could verify specifically that our test circuit components exist
            // This is implementation-specific but might look for elements at expected positions
        }
    }
    
    @Test
    fun testOverwriteExistingCircuit() {
        // First save a circuit
        testSaveCircuit()
        
        // Now try to save a different circuit with the same name
        val intent = Intent(
            ApplicationProvider.getApplicationContext(),
            CircuitDrawer::class.java
        )
        
        ActivityScenario.launch<CircuitDrawer>(intent).use { scenario ->
            // Create a different circuit
            onView(withId(R.id.andgate)).perform(click())
            onView(withText("2 Input")).perform(click())
            onView(withId(R.id.drawingarea)).perform(TouchActions.clickXY(200, 200))
            
            // Open menu and save
            onView(withId(R.id.menubtn)).perform(click())
            onView(withText(R.string.save_circuit)).perform(click())
            
            // Enter same name as before
            onView(withId(R.id.filename))
                .perform(replaceText(testCircuitName))
            
            // Click Save
            onView(withId(R.id.save_button)).perform(click())
            
            // Should get overwrite confirmation dialog
            onView(withText(containsString("exists")))
                .check(matches(isDisplayed()))
            
            // Confirm overwrite
            onView(withText(R.string.yes))
                .perform(click())
            
            // Verify success
            onView(withText(containsString("saved")))
                .check(matches(isDisplayed()))
        }
    }
    
    /**
     * Helper class for touch actions specific to this test
     */
    private object TouchActions {
        fun clickXY(x: Int, y: Int) = CustomViewActions.clickXY(x, y)
        
        fun dragFromTo(fromX: Int, fromY: Int, toX: Int, toY: Int) = 
            CustomViewActions.dragFromTo(fromX, fromY, toX, toY)
    }
} 