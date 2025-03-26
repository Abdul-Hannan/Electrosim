package com.electrosim

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import com.electrosim.databinding.SplashBinding

class SplashScreen : Activity() {

    protected var splashTime = 5000
    private lateinit var splashThread: Thread

    /** Called when the activity is first created.  */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = SplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val splashScreen = this

        // Thread for displaying the SplashScreen
        splashThread = object : Thread() {
            @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
            override fun run() {
                try {
                    synchronized(this) {
                        (this as java.lang.Object).wait(splashTime.toLong())
                    }
                } catch (e: InterruptedException) {
                    // Do nothing
                } finally {
                    finish()
                    val i = Intent()
                    i.setClass(splashScreen, MainMenu::class.java)
                    startActivity(i)
                }
            }
        }
        splashThread.start()
    }

    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            synchronized(splashThread) {
                (splashThread as java.lang.Object).notifyAll()
            }
        }
        return true
    }
} 