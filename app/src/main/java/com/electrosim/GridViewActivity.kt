package com.electrosim

import com.electrosim.adapter.ImageAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import com.electrosim.databinding.Main2Binding

class GridViewActivity : Activity() {
    private lateinit var binding: Main2Binding

    companion object {
        private val MOBILE_OS = arrayOf("circuit1", "circuit2", "circuit3", "circuit2")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize ViewBinding
        binding = Main2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.gridView1.adapter = ImageAdapter(this, MOBILE_OS)

        binding.gridView1.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
            val intent = Intent(baseContext, CircuitDrawer::class.java)
            startActivity(intent)
        }
    }
} 