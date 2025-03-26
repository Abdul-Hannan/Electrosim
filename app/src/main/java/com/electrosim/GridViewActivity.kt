package com.electrosim

import com.electrosim.adapter.ImageAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.GridView

class GridViewActivity : Activity() {
    private lateinit var gridView: GridView

    companion object {
        private val MOBILE_OS = arrayOf("circuit1", "circuit2", "circuit3", "circuit2")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main2)

        gridView = findViewById(R.id.gridView1)
        gridView.adapter = ImageAdapter(this, MOBILE_OS)

        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
            val intent = Intent(baseContext, CircuitDrawer::class.java)
            startActivity(intent)
        }
    }
} 