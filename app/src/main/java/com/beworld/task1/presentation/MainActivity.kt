package com.beworld.task1.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.beworld.task1.R
import com.beworld.task1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}