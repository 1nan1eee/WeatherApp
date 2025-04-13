package com.example.weatherapp.data.network

import com.example.weatherapp.data.api.CityApiService
import com.example.weatherapp.data.api.WeatherApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL_CITY = "https://api.api-ninjas.com/"
    private const val BASE_URL_WEATHER = "https://api.open-meteo.com/v1/"

    private val retrofitCity by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_CITY)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofitWeather by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_WEATHER)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val cityApi: CityApiService by lazy { retrofitCity.create(CityApiService::class.java) }
    val weatherApi: WeatherApiService by lazy { retrofitWeather.create(WeatherApiService::class.java) }
}