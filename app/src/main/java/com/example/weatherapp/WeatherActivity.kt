package com.example.weatherapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.data.model.CityResponse
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.network.RetrofitInstance
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WeatherActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val cityName = intent.getStringExtra("CITY_NAME") ?: return@setContent

            WeatherScreen(cityName)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun WeatherScreen(cityName: String) {
        var weatherData by remember { mutableStateOf(emptyList<WeatherDay>()) }
        var isLoading by remember { mutableStateOf(true) }

        // для корутины
        LaunchedEffect(cityName) {
            fetchWeatherData(cityName) { result ->
                weatherData = result
                isLoading = false
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 42.dp, end = 16.dp, bottom = 50.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                Text(
                    text = "Loading...",
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = cityName,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                weatherData.forEach { day ->
                    WeatherCard(day)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { finish() }) {
                Text("Back")
            }
        }
    }

    @Composable
    fun WeatherCard(day: WeatherDay) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = day.description)
                Text(text = "Midnight: ${day.midnightTemperature}°C")
                Text(text = "Noon: ${day.noonTemperature}°C")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fetchWeatherData(cityName: String, callback: (List<WeatherDay>) -> Unit) {
        lifecycleScope.launch {
            try {
                val cityResponse = RetrofitInstance.cityApi.getCityCoordinates(cityName)
                if (cityResponse.isSuccessful && cityResponse.body() != null) {
                    val coordinatesList = cityResponse.body() as List<CityResponse>
                    if (coordinatesList.isNotEmpty()) {
                        val coordinates = coordinatesList[0]
                        val latitude = coordinates.latitude
                        val longitude = coordinates.longitude

                        val weatherResponse = RetrofitInstance.weatherApi.getWeatherForecast(latitude, longitude)
                        if (weatherResponse.isSuccessful && weatherResponse.body() != null) {
                            displayWeather(weatherResponse.body()!!, callback)
                        } else {
                            callback(emptyList())
                        }
                    } else {
                        callback(emptyList())
                    }
                } else if (!cityResponse.isSuccessful) {
                    callback(emptyList())
                } else if (cityResponse.body() == null) {
                    callback(emptyList())
                }
            } catch (e: Exception) {
                callback(emptyList())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayWeather(weatherResponse: WeatherResponse, callback: (List<WeatherDay>) -> Unit) {
        val temperatures = weatherResponse.hourly.temperature_2m
        val selectedTemperatures = mutableListOf<WeatherDay>()

        val currentDate = LocalDate.now()
        val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM")

        for (i in 0 until 7) {
            val midnightTemperature = temperatures[i * 24]
            val noonTemperature = temperatures[i * 24 + 12]

            // дату для текущего дня + i
            val date = currentDate.plusDays(i.toLong()).format(dateFormatter)

            selectedTemperatures.add(WeatherDay(date, midnightTemperature, noonTemperature))
        }

        callback(selectedTemperatures)
    }
}

data class WeatherDay(val description: String, val midnightTemperature: Double, val noonTemperature: Double)