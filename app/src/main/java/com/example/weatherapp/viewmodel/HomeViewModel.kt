package com.example.weatherapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    var searchQuery = mutableStateOf("")

    private val _cities = listOf(
        "Moscow", "Saint Petersburg", "Novosibirsk", "Ekaterinburg", "Kazan",
        "Krasnoyarsk", "Nizhniy Novgorod", "Chelyabinsk", "Ufa", "Samara",
        "Krasnodar", "Omsk", "Voronezh", "Perm", "Izhevsk"
    )
    private val _searchQuery = MutableStateFlow("")

    val filteredCities: StateFlow<List<String>> = _searchQuery.map { query: String ->
        _cities.filter { it.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
        viewModelScope.launch {
            _searchQuery.emit(query)
        }
    }
}
