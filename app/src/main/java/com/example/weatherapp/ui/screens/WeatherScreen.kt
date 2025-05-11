package com.example.weatherapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.data.CurrentWeather
import com.example.weatherapp.data.WeatherDay
import com.example.weatherapp.ui.components.CurrentWeatherCard
import com.example.weatherapp.ui.components.WeatherCard
import com.example.weatherapp.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherScreen(
    cityName: String,
    weatherViewModel: WeatherViewModel,
) {
    val weatherResponse by weatherViewModel.weatherData.collectAsState()
    val isLoading by weatherViewModel.isLoading.collectAsState()

    var currentCityName by remember { mutableStateOf("") }

    LaunchedEffect(cityName) {
        if (currentCityName != cityName) {
            currentCityName = cityName
            weatherViewModel.fetchWeatherData(cityName)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = cityName,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color(0xFF9569CC),
                scrolledContainerColor = Color.Gray,
                navigationIconContentColor = Color.Transparent,
                titleContentColor = Color.White,
                actionIconContentColor = Color.Transparent,
            ),
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(start=16.dp, top=16.dp, end=16.dp, bottom=55.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (isLoading) {
                item {
                    Text(
                        text="Loading...",
                        fontSize=35.sp,
                        fontWeight=FontWeight.Bold,
                        modifier=Modifier.padding(top=15.dp),
                        color=Color.Gray,
                        textAlign=TextAlign.Center,
                    )
                }
            } else {
                if (weatherResponse.isNotEmpty()) {
                    items(weatherResponse) { day ->
                        when (day) {
                            is WeatherDay -> {
                                WeatherCard().Display(day)
                            }
                            is CurrentWeather -> {
                                CurrentWeatherCard().Display(day)
                            }
                        }
                        Spacer(modifier=Modifier.height(8.dp))
                    }
                } else {
                    item {
                        Text(
                            text="City $cityName is not found.",
                            fontSize=35.sp,
                            fontWeight=FontWeight.Bold,
                            modifier=Modifier.padding(top=15.dp),
                            color=Color(0xFFD75B5B),
                            textAlign=TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}
