package com.example.apairy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apairy.R
import com.example.apairy.models.CurrentWeatherHourly
import com.example.apairy.models.FutureWeather

class FutureWeatherAdapter: RecyclerView.Adapter<FutureWeatherAdapter.FutureWeatherViewHolder>() {
    private val FutureWeatherList = ArrayList<FutureWeather>()
    private val fullList = ArrayList<FutureWeather>()


    class FutureWeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date = itemView.findViewById<TextView>(R.id.tv_future_weather_date)
        val morning = itemView.findViewById<TextView>(R.id.tv_morning)
        val day = itemView.findViewById<TextView>(R.id.tv_day)
        val evening = itemView.findViewById<TextView>(R.id.tv_evening)
        val night = itemView.findViewById<TextView>(R.id.tv_night)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FutureWeatherViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FutureWeatherViewHolder(inflater.inflate(R.layout.future_weather_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return FutureWeatherList.size
    }

    override fun onBindViewHolder(holder: FutureWeatherViewHolder, position: Int) {
        val currentFutureWeather = FutureWeatherList[position]

        holder.date.text = currentFutureWeather.date
        holder.morning.text = currentFutureWeather.morning.second.toString()
        holder.day.text = currentFutureWeather.day.second.toString()
        holder.evening.text = currentFutureWeather.evening.second.toString()
        holder.night.text = currentFutureWeather.night.second.toString()
    }

    fun updateFutureWeatherList(newList: List<FutureWeather>) {
        fullList.clear()
        fullList.addAll(newList)

        FutureWeatherList.clear()
        FutureWeatherList.addAll(fullList)
        notifyDataSetChanged()
    }
}