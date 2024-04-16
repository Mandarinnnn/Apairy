package com.example.apairy.models

data class Weather(
    val name: String,
    val temperature: Double,
    val windSpeed: Double,
    val humidity: Int,
    val pressure: Int
)
