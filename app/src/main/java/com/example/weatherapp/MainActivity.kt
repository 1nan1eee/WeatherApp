package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp.ui.theme.WeatherAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApp()
        }
    }

    @Composable
    fun WeatherApp() {
        var cityName by remember { mutableStateOf("") }
        var weatherInfo by remember { mutableStateOf("") }
        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = cityName,
                onValueChange = { cityName = it },
                label = { Text("Введите название города") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                coroutineScope.launch {
                    fetchWeatherData(cityName) { result ->
                        weatherInfo = result
                    }
                }
            }) {
                Text("Получить погоду")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = weatherInfo)
        }
    }

    private fun fetchWeatherData(cityName: String, callback: (String) -> Unit) {
        lifecycleScope.launch {
            try {
                // Получаем координаты города
                val cityResponse = RetrofitInstance.cityApi.getCityCoordinates(cityName)
                if (cityResponse.isSuccessful && cityResponse.body() != null) {
                    val coordinatesList = cityResponse.body() as List<CityResponse>
                    if (coordinatesList.isNotEmpty()) {
                        val coordinates = coordinatesList[0] // Берем первый элемент из списка
                        // Теперь вы можете получить latitude и longitude
                        val latitude = coordinates.latitude
                        val longitude = coordinates.longitude

                        // Получаем прогноз погоды по координатам
                        val weatherResponse = RetrofitInstance.weatherApi.getWeatherForecast(latitude, longitude)
                        if (weatherResponse.isSuccessful && weatherResponse.body() != null) {
                            displayWeather(weatherResponse.body()!!, callback)
                        } else {
                            callback("Ошибка получения прогноза погоды")
                        }
                    } else {
                        callback("Город не найден")
                    }
                } else if (!cityResponse.isSuccessful) {
                    callback("Не удалось получить данные из Api Ninjas")
                } else if (cityResponse.body() == null) {
                    callback("Пришёл пустой ответ")
                }
            } catch (e: Exception) {
                callback("Скибиди")
            }
        }
    }

    private fun displayWeather(weatherResponse: WeatherResponse, callback: (String) -> Unit) {
        val temperatures = weatherResponse.hourly.temperature_2m.joinToString(", ")
        callback("Температуры на ближайшие дни:\n$temperatures")
    }
}

@Composable
fun WeatherInfoDisplay(weatherInfo: String, modifier: Modifier = Modifier) {
    Text(
        text = weatherInfo,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun WeatherInfoPreview() {
    WeatherAppTheme {
        WeatherInfoDisplay("Температуры на ближайшие дни:\n20°C, 22°C, 19°C")
    }
}