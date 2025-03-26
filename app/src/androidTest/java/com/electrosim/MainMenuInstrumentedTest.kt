package com.electrosim

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainMenuInstrumentedTest {

    @Test
    fun testMainMenuButtons() {
        // Launch the main menu activity
        ActivityScenario.launch(MainMenu::class.java).use {
            // Verify that the start button is displayed
            onView(withId(R.id.startbtn))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.start)))
            
            // Verify that the help button is displayed
            onView(withId(R.id.helpbtn))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.help)))
            
            // Verify that the about button is displayed
            onView(withId(R.id.aboutbtn))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.about)))
        }
    }
    
    @Test
    fun testHelpButtonOpensHelpActivity() {
        // Launch the main menu activity
        ActivityScenario.launch(MainMenu::class.java).use {
            // Click on the help button
            onView(withId(R.id.helpbtn)).perform(click())
            
            // Check that we've navigated to the Help activity
            // This will implicitly wait for the Help activity to appear
            onView(withId(R.id.helptext))
                .check(matches(isDisplayed()))
        }
    }
    
    @Test
    fun testAboutButtonOpensAboutActivity() {
        // Launch the main menu activity
        ActivityScenario.launch(MainMenu::class.java).use {
            // Click on the about button
            onView(withId(R.id.aboutbtn)).perform(click())
            
            // Check that we've navigated to the About activity
            // This will implicitly wait for the About activity to appear
            onView(withId(R.id.abouttext))
                .check(matches(isDisplayed()))
        }
    }
} 