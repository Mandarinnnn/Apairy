package com.example.apairy.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class WeatherViewModel(application: Application): AndroidViewModel(application){

    private val requestQueue: RequestQueue = Volley.newRequestQueue(application)

    private val _weatherData = MutableLiveData<Weather>()
    val weatherData: LiveData<Weather> = _weatherData

    fun fetchWeatherData(latitude: Double, longitude: Double) {
        val apiKey = "a002e66aaa067ba6ec77a0e390a6b549"
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$apiKey&units=metric"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val main = response.getJSONObject("main")
                val name = response.getString("name")
                val wind = response.getJSONObject("wind")
                val temperature = main.getDouble("temp")
                val windSpeed = wind.getDouble("speed")
                val humidity = main.getInt("humidity")
                val pressure = main.getInt("pressure")

                val weatherData = Weather(name, temperature, windSpeed, humidity, pressure)
                _weatherData.postValue(weatherData)
            },
            { error ->
                // Обработка ошибки
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

}