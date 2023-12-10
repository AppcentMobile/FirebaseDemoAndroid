package com.appcent.android.firebasedemo.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.appcent.android.firebasedemo.R
import com.appcent.android.firebasedemo.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun hideProgress() {
        binding.progressLayout.isVisible = false
    }

    fun showProgress() {
        binding.progressLayout.isVisible = true
    }
}