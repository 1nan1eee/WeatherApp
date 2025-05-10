package com.example.weatherapp.ui.components

import com.example.weatherapp.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.data.WeatherDay
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment

class WeatherCard {

    @Composable
    fun Display(day: WeatherDay) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = day.description,
                    color = Color(0xFF463169),
                    fontSize = 20.sp
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Column(
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.moon),
                            contentDescription = "Moon icon",
                            modifier = Modifier
                                .padding(end = 5.dp)
                                .height(25.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.sun),
                            contentDescription = "Sun icon",
                            modifier = Modifier
                                .height(38.dp)
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "${day.midnightTemperature}°C",
                            color = Color(0xFF463169),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${day.noonTemperature}°C",
                            color = Color(0xFF463169),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
