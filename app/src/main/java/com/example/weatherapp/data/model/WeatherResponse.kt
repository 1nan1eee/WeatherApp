package com.example.weatherapp.data.model

data class WeatherResponse(val hourly: Hourly)

data class Hourly(val temperature_2m: List<Double>)