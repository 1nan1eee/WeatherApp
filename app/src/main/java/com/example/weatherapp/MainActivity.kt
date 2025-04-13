package com.example.weatherapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    private val cities = listOf(
        "Moscow", "Saint Petersburg", "Novosibirsk", "Ekaterinburg", "Kazan",
        "Krasnoyarsk", "Nizhniy Novgorod", "Chelyabinsk", "Ufa", "Samara",
        "Krasnodar", "Omsk", "Voronezh", "Perm", "Izhevsk"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApp()
        }
    }

    @Composable
    fun WeatherApp() {
        MaterialTheme() { // Убедитесь, что используете вашу цветовую схему
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, top = 42.dp, end = 16.dp, bottom = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Choose a city:",
                    modifier = Modifier.padding(bottom = 16.dp),
                    fontWeight = FontWeight.Bold
                )

                CityList(cities) { cityName ->
                    val intent = Intent(this@MainActivity, WeatherActivity::class.java).apply {
                        putExtra("CITY_NAME", cityName)
                    }
                    startActivity(intent)
                }
            }
        }
    }

    @Composable
    fun CityList(cities: List<String>, onCitySelected: (String) -> Unit) {
        LazyColumn {
            items(cities) { city ->
                CityCard(city, onCitySelected)
            }
        }
    }

    @Composable
    fun CityCard(city: String, onCitySelected: (String) -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { onCitySelected(city) },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = city, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}