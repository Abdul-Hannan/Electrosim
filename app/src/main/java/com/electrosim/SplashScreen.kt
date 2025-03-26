package com.electrosim

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent

class SplashScreen : Activity() {

    protected var splashTime = 5000
    private lateinit var splashThread: Thread

    /** Called when the activity is first created.  */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        val splashScreen = this

        // Thread for displaying the SplashScreen
        splashThread = object : Thread() {
            override fun run() {
                try {
                    synchronized(this) {
                        wait(splashTime.toLong())
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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            synchronized(splashThread) {
                splashThread.notifyAll()
            }
        }
        return true
    }
} 