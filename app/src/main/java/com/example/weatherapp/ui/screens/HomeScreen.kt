package com.example.weatherapp.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.WeatherActivity
import com.example.weatherapp.ui.components.CityCard


class HomeScreen(private val viewModel: HomeViewModel) {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Display(context: Context) {
        var searchQuery by remember { viewModel.searchQuery }

        MaterialTheme {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Weather App",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TopAppBarColors(
                        containerColor = Color(0xFF9569CC),
                        scrolledContainerColor = Color.Gray,
                        navigationIconContentColor = Color.Transparent,
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.Transparent
                        )
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 55.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                ) {
                    Text(
                        text = "Choose a city:",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF463169)
                    )

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            viewModel.updateSearchQuery(it)
                            searchQuery = it
                        },
                        label = { Text("Search for a city") },
                        modifier = Modifier.fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(8.dp),
                        trailingIcon = {
                            IconButton(onClick = {
                                performSearch(context, searchQuery)
                            }) {
                                Icon(imageVector = Icons.Default.Search, contentDescription = null)
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy( // действие клавиатуры на кнопку поиска
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions( // обработка энтера
                            onSearch = {
                                performSearch(context, searchQuery)
                            }
                        ),

                    )

                    val filteredCities = viewModel.getFilteredCities()

                    CityList(filteredCities) { cityName ->
                        val intent = Intent(context, WeatherActivity::class.java).apply {
                            putExtra("CITY_NAME", cityName)
                        }
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

    private fun performSearch(context: Context, query: String) {
        if (query.isNotBlank()) {
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("CITY_NAME", query)
            }
            context.startActivity(intent)
        }
    }

    @Composable
    fun CityList(cities: List<String>, onCitySelected: (String) -> Unit) {
        LazyColumn {
            items(cities) { city ->
                CityCard().Display(city, onCitySelected)
            }
        }
    }
}
