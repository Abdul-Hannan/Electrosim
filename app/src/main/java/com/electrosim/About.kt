package com.electrosim

import android.app.Activity
import android.os.Bundle
import com.electrosim.databinding.AboutBinding

class About : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = AboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
} 