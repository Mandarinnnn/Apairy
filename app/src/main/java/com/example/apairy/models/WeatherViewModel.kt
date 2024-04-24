package com.example.apairy.models

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherViewModel(application: Application): AndroidViewModel(application){

    private val requestQueue: RequestQueue = Volley.newRequestQueue(application)

    private val _currentWeather = MutableLiveData<CurrentWeather>()
    val currentWeather: LiveData<CurrentWeather> = _currentWeather



    private val _futureWeather = MutableLiveData<List<FutureWeather>>()
    val futureWeather: LiveData<List<FutureWeather>> get() = _futureWeather



    private val _currentWeatherHourly = MutableLiveData<List<CurrentWeatherHourly>>()
    val currentWeatherHourly: LiveData<List<CurrentWeatherHourly>> get() = _currentWeatherHourly


    fun fetchWeatherData(latitude: Double, longitude: Double) {
       // val requestQueue: RequestQueue = Volley.newRequestQueue(application)

        val apiKey = "ec24bdf01615458c8b0131843241704"
        val days = 5

        val url = "https://api.weatherapi.com/v1/forecast.json?key=$apiKey&q=$latitude,$longitude&days=$days&aqi=no&alerts=no"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val current = response.getJSONObject("current")
                val location = response.getJSONObject("location")
                currentWeather(location, current)

                val forecast = response.getJSONObject("forecast")
                val forecastDay = forecast.getJSONArray("forecastday")

                val currentDay = forecastDay[0] as JSONObject
                val currentDayHourly = currentDay.getJSONArray("hour")

                currentWeatherHourly(currentDayHourly)
                futureWeather(forecastDay)
            },
            { error ->
                // Обработка ошибки
            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    private fun currentWeather(location: JSONObject, current: JSONObject){
        val name = location.getString("name")
        val temperature = current.getString("temp_c").toFloat()
        val windSpeed = current.getString("wind_kph").toFloat()
        val humidity = current.getString("humidity").toInt()
        val pressure = current.getString("pressure_mb").toFloat()
        val weather = current.getJSONObject("condition").getString("icon")

        val currentWeather = CurrentWeather(
            name, temperature, windSpeed, humidity, pressure, weather
        )

        _currentWeather.value = currentWeather
    }

    private fun currentWeatherHourly(weatherHourly: JSONArray){

        val list: ArrayList<CurrentWeatherHourly> = ArrayList()

        for (i in 0 until weatherHourly.length()) {

            val forHour = weatherHourly[i] as JSONObject
            val temperature = forHour.getString("temp_c").toFloat()
            val weather = forHour.getJSONObject("condition").getString("icon")
            val time = forHour.getString("time")

            val currentWeatherForHour = CurrentWeatherHourly(
                temperature, weather, time
            )
            list.add(currentWeatherForHour)
        }
        _currentWeatherHourly.value = list
    }


    private fun futureWeather(weatherForAllDays: JSONArray){
        val indices = listOf(6, 13, 18, 22)

        val list = ArrayList<FutureWeather>()


        for (i in 1 until weatherForAllDays.length()) {

            val forDay = weatherForAllDays[i] as JSONObject

            val forDayHourly = forDay.getJSONArray("hour") as JSONArray

            val date = forDay.getString("date")

            var morning: Pair<String, Float> = Pair("", 0f)
            var day: Pair<String, Float> = Pair("", 0f)
            var evening: Pair<String, Float> = Pair("", 0f)
            var night: Pair<String, Float> = Pair("", 0f)

            for (i in indices) {
                val forHour = forDayHourly[i] as JSONObject
                val temperature = forHour.getString("temp_c").toFloat()
                val weather = forHour.getJSONObject("condition").getString("icon")


                when(i){
                    6 ->  morning = Pair(weather, temperature)
                    13 -> day = Pair(weather, temperature)
                    18 -> evening = Pair(weather, temperature)
                    22 -> night = Pair(weather, temperature)
                }
            }

            val futureWeather = FutureWeather(
                date, morning, day, evening, night
            )

            list.add(futureWeather)

        }
        _futureWeather.value = list
    }

}