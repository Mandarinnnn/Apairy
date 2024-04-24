package com.example.apairy.models

data class CurrentWeather(
    val name: String,
    val temperature: Float,
    val windSpeed: Float,
    val humidity: Int,
    val pressure: Float,
    val weather: String,
)
