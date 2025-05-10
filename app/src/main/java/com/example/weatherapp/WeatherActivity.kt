package com.example.weatherapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.ui.screens.WeatherScreen
import com.example.weatherapp.viewmodel.WeatherViewModel

class WeatherActivity : ComponentActivity() {

    private lateinit var weatherViewModel: WeatherViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val cityName = intent.getStringExtra("CITY_NAME") ?: return@setContent

            weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

            if (weatherViewModel.weatherData.value == null) {
                weatherViewModel.fetchWeatherData(cityName)
            }

            WeatherScreen().Display(cityName, weatherViewModel)
        }
    }
}
