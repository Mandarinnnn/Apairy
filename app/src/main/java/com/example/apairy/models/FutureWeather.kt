package com.example.apairy.models

data class FutureWeather (
    val date: String,
    val morning: Pair<String, Float>,
    val day: Pair<String, Float>,
    val evening: Pair<String, Float>,
    val night: Pair<String, Float>
)
