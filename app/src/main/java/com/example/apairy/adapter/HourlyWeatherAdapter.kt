package com.example.apairy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.apairy.R
import com.example.apairy.models.CurrentWeatherHourly
import com.example.apairy.models.HiveState

class HourlyWeatherAdapter: RecyclerView.Adapter<HourlyWeatherAdapter.HourlyWeatherViewHolder>() {
    private val HourlyWeatherList = ArrayList<CurrentWeatherHourly>()
    private val fullList = ArrayList<CurrentWeatherHourly>()


    class HourlyWeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time = itemView.findViewById<TextView>(R.id.tv_time)
        //val weather = itemView.findViewById<TextView>(R.id.iv_weather)
        val temperature = itemView.findViewById<TextView>(R.id.tv_temperature)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeatherViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HourlyWeatherViewHolder(inflater.inflate(R.layout.hourly_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return HourlyWeatherList.size
    }

    override fun onBindViewHolder(holder: HourlyWeatherViewHolder, position: Int) {
        val currentWeather = HourlyWeatherList[position]
        holder.time.text = currentWeather.time.substring(11)
       // holder.weather. =
        holder.temperature.text = currentWeather.temperature.toString()
    }

    fun updateHourlyWeatherList(newList: List<CurrentWeatherHourly>) {
        fullList.clear()
        fullList.addAll(newList)

        HourlyWeatherList.clear()
        HourlyWeatherList.addAll(fullList)
        notifyDataSetChanged()
    }
}