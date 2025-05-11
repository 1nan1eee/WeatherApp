package com.example.weatherapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.ui.screens.HomeScreen
import com.example.weatherapp.ui.screens.WeatherScreen
import com.example.weatherapp.viewmodel.HomeViewModel
import com.example.weatherapp.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            val homeViewModel= ViewModelProvider(this)[HomeViewModel::class.java]
            val weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    HomeScreen(homeViewModel, navController)
                }
                composable("weather/{cityName}") { backStackEntry ->
                    val cityName = backStackEntry.arguments?.getString("cityName")
                    WeatherScreen(cityName ?: "", weatherViewModel)
                }
            }
        }
    }
}
