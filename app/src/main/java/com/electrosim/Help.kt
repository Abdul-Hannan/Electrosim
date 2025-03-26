package com.electrosim

import android.app.Activity
import android.os.Bundle
import com.electrosim.databinding.HelpBinding

class Help : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = HelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
} 