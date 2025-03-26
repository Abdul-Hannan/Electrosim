package com.electrosim

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {

    @Before
    fun setup() {
        // Initialize Intents before each test
        Intents.init()
    }

    @After
    fun tearDown() {
        // Release Intents after each test
        Intents.release()
    }

    @Test
    fun testAppContext() {
        // Context of the app under test
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assert(appContext.packageName.contains("electrosim"))
    }

    @Test
    fun testMainActivityLaunchesCorrectly() {
        // Launch the main activity
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            // Verify that the main views are displayed
            onView(withId(R.id.newcircuit))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.new_circuit)))

            onView(withId(R.id.loadcircuit))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.load_circuit)))

            onView(withId(R.id.help))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.help)))
        }
    }

    @Test
    fun testNewCircuitButtonOpensCircuitDrawer() {
        // Launch the main activity
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            // Click on the New Circuit button
            onView(withId(R.id.newcircuit)).perform(click())

            // Verify that CircuitDrawer activity is launched
            intended(hasComponent(CircuitDrawer::class.java.name))
        }
    }

    @Test
    fun testLoadCircuitButtonOpensFileChooser() {
        // Launch the main activity
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            // Click on the Load Circuit button
            onView(withId(R.id.loadcircuit)).perform(click())

            // This test will depend on the implementation of file chooser
            // If it's a builtin intent, it's harder to test
            // If it's a custom activity, verify it was launched:
            // intended(hasComponent(FileChooserActivity::class.java.name))
            
            // For now we'll just verify the button is clickable
            onView(withId(R.id.loadcircuit))
                .check(matches(isClickable()))
        }
    }

    @Test
    fun testHelpButtonOpensHelpActivity() {
        // Launch the main activity
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            // Click on the Help button
            onView(withId(R.id.help)).perform(click())

            // Verify that HelpActivity is launched
            intended(hasComponent(HelpActivity::class.java.name))
        }
    }

    @Test
    fun testUIElementsDisplayed() {
        // Launch the main activity
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            // Check that the app name is displayed in the title
            onView(withId(R.id.appname))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.app_name)))

            // Check that the main image is displayed
            onView(withId(R.id.appicon))
                .check(matches(isDisplayed()))
        }
    }
} 