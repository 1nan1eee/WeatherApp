package com.example.weatherapp.data.model

data class CityResponse(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val population: Int,
    val is_capital: Boolean
)
