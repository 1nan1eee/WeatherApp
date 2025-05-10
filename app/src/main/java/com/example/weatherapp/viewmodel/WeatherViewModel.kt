package com.example.weatherapp.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.WeatherDay
import com.example.weatherapp.data.model.CityResponse
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.network.RetrofitInstance
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class WeatherViewModel : ViewModel() {

    private val _weatherData = MutableLiveData<List<WeatherDay>>()
    val weatherData: LiveData<List<WeatherDay>> get() = _weatherData

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

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
        val selectedTemperatures = mutableListOf<WeatherDay>()

        val currentDate = LocalDate.now()
        val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM", Locale.ENGLISH)

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
