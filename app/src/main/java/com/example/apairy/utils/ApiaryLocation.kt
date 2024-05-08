package com.example.apairy.utils

import android.content.Context
import android.content.SharedPreferences

class ApiaryLocation(val context: Context) {

    private val sharedPreferences: SharedPreferences = context
        .getSharedPreferences("ApiaryLocation", Context.MODE_PRIVATE)

    companion object {
        private const val LATITUDE = "LATITUDE"
        private const val LONGITUDE = "LONGITUDE"
    }

    fun saveLocation(latitude: Double, longitude: Double){
        val editor = sharedPreferences.edit()
        editor.putFloat(LATITUDE, latitude.toFloat())
        editor.putFloat(LONGITUDE, longitude.toFloat())

        editor.apply()
    }


    fun getLocation(): Pair<Double, Double> {

        return Pair(
            sharedPreferences.getFloat(LATITUDE, 0f).toDouble(),
            sharedPreferences.getFloat(LONGITUDE, 0f).toDouble()
        )
    }

    fun areCoordinatesSaved(): Boolean {
        return sharedPreferences.contains(LATITUDE) && sharedPreferences.contains(LONGITUDE)
    }
}