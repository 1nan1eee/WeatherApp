package com.example.weatherapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.data.CurrentWeather

class CurrentWeatherCard {
    @Composable
    fun Display(currentWeather: CurrentWeather) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardColors(
                containerColor = Color(0xFF9569CC),
                contentColor = Color(0xFFFF0000),
                disabledContentColor = Color(0xFF0036FF),
                disabledContainerColor = Color(0xFF36FF00)
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, top = 25.dp, end = 16.dp, bottom = 25.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = "Now",
                    color = Color.White,
                    fontSize = 45.sp
                )
                Text(
                    text = "${currentWeather.temperature}°C",
                    color = Color.White,
                    fontSize = 45.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
