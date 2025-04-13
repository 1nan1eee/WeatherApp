package com.example.weatherapp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CityApiService {
    @GET("v1/city")
    suspend fun getCityCoordinates(
        @Query("name") cityName: String,
        @Header("X-Api-Key") apiKey: String = "e7hkLX1XDxVyCkdl8vpt1A==xOOrgBGUtnWXbXgT"
    ): Response<List<CityResponse>>
}