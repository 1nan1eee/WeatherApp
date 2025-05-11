package com.example.weatherapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.ui.screens.WeatherScreen
import com.example.weatherapp.viewmodel.WeatherViewModel

class WeatherActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

            MaterialTheme {
                Surface {
                    WeatherScreen(cityName = "Ваш город", weatherViewModel = weatherViewModel)
                }
            }
        }
    }
}
