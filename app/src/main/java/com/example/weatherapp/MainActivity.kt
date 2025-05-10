package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.ui.screens.HomeScreen
import com.example.weatherapp.ui.screens.HomeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        setContent {
            HomeScreen(viewModel).Display(this)
        }
    }
}
