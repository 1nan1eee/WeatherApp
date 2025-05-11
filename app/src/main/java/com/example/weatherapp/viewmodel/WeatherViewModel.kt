package com.example.weatherapp.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.CurrentWeather
import com.example.weatherapp.data.WeatherDay
import com.example.weatherapp.data.WeatherItem
import com.example.weatherapp.data.model.CityResponse
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class WeatherViewModel : ViewModel() {

    private val _weatherData = MutableStateFlow<List<WeatherItem>>(emptyList())
    val weatherData: StateFlow<List<WeatherItem>> get() = _weatherData

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchWeatherData(cityName: String) {
        _isLoading.value = true

        viewModelScope.launch {
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
                            displayWeather(weatherResponse.body()!!)
                        } else {
                            _weatherData.value = emptyList()
                        }
                    } else {
                        _weatherData.value = emptyList()
                    }
                } else {
                    _weatherData.value = emptyList()
                }
            } catch (e: Exception) {
                _weatherData.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayWeather(weatherResponse: WeatherResponse) {

        val temperatures = weatherResponse.hourly.temperature_2m
        val selectedTemperatures = mutableListOf<WeatherItem>()

        val currentDate = LocalDate.now()
        val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM", Locale.ENGLISH)

        val currentTime = LocalTime.now()
        val currentHour: Int = currentTime.hour

        selectedTemperatures.add(CurrentWeather(temperatures[currentHour]))

        for (i in 0 until 10) {
            val midnightTemperature = temperatures[i * 24]
            val noonTemperature = temperatures[i * 24 + 12]

            // дата для текущего дня + i
            val date = currentDate.plusDays(i.toLong()).format(dateFormatter)

            selectedTemperatures.add(WeatherDay(date, midnightTemperature, noonTemperature))
        }

        _weatherData.value = selectedTemperatures
    }
}
