package com.example.weatherapp.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    var searchQuery = mutableStateOf("")

    private val cities = listOf(
        "Moscow", "Saint Petersburg", "Novosibirsk", "Ekaterinburg", "Kazan",
        "Krasnoyarsk", "Nizhniy Novgorod", "Chelyabinsk", "Ufa", "Samara",
        "Krasnodar", "Omsk", "Voronezh", "Perm", "Izhevsk"
    )

    fun getFilteredCities(): List<String> {
        return cities.filter { city ->
            city.contains(searchQuery.value, ignoreCase = true)
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }
}
