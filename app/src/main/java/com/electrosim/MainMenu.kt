package com.electrosim

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.electrosim.databinding.Main3Binding

class MainMenu : Activity(), OnClickListener {
    private lateinit var binding: Main3Binding

    /** Called when the activity is first created.  */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize ViewBinding
        binding = Main3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Set click listeners
        binding.newButton.setOnClickListener(this)
        binding.saveButton.setOnClickListener(this)
        binding.aboutButton.setOnClickListener(this)
        binding.createButton.setOnClickListener(this)
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