package com.electrosim

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener

class MainMenu : Activity(), OnClickListener {

    /** Called when the activity is first created.  */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main3)
        val continueButton = findViewById<View>(R.id.new_button)
        continueButton.setOnClickListener(this)
        val newButton = findViewById<View>(R.id.save_button)
        newButton.setOnClickListener(this)
        val aboutButton = findViewById<View>(R.id.about_button)
        aboutButton.setOnClickListener(this)
        val exitButton = findViewById<View>(R.id.create_button)
        exitButton.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.about_button -> {
                val i = Intent(this, About::class.java)
                startActivity(i)
            }
            R.id.new_button -> {
                val a = Intent(this, Help::class.java)
                startActivity(a)
            }
            R.id.save_button -> {
                val b = Intent(this, GridViewActivity::class.java)
                startActivity(b)
            }
            R.id.create_button -> {
                val c = Intent(this, CircuitDrawer::class.java)
                startActivity(c)
            }
        }
    }
} 