package com.example.apairy.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.apairy.R
import com.example.apairy.adapter.FutureWeatherAdapter
import com.example.apairy.adapter.HiveAdapter
import com.example.apairy.adapter.HourlyWeatherAdapter
import com.example.apairy.adapter.TheoryAdapter
import com.example.apairy.databinding.FragmentApiaryInfoBinding
import com.example.apairy.databinding.FragmentWeatherBinding
import com.example.apairy.models.CurrentWeather
import com.example.apairy.models.CurrentWeatherHourly
import com.example.apairy.models.FutureWeather
import com.example.apairy.models.WeatherViewModel
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherFragment : Fragment() {
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: WeatherViewModel
    private lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter

    private lateinit var futureWeatherAdapter: FutureWeatherAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        binding.rvHourly.setHasFixedSize(true)
        binding.rvHourly.layoutManager = StaggeredGridLayoutManager(1, LinearLayout.HORIZONTAL)
        hourlyWeatherAdapter = HourlyWeatherAdapter()
        binding.rvHourly.adapter = hourlyWeatherAdapter

        viewModel.currentWeatherHourly.observe(viewLifecycleOwner,){ list ->
            list?.let{
                hourlyWeatherAdapter.updateHourlyWeatherList(list)
            }
        }


        binding.rvFutureWeather.hasFixedSize()
        binding.rvFutureWeather.layoutManager = LinearLayoutManager(requireContext())
        futureWeatherAdapter = FutureWeatherAdapter()

        binding.rvFutureWeather.adapter =  futureWeatherAdapter

        viewModel.futureWeather.observe(viewLifecycleOwner,){ list ->
            list?.let{
                futureWeatherAdapter.updateFutureWeatherList(list)
            }
        }


        viewModel.currentWeather.observe(viewLifecycleOwner,){ currentWeather ->
            binding.tvName.text = "${currentWeather.name} °C"
            binding.tvTemp.text = "${currentWeather.temperature} °C"
            binding.tvWind.text = "${currentWeather.windSpeed} m/s"
            binding.tvHumidity.text = "${currentWeather.humidity} %"
            binding.tvPressure.text = "${currentWeather.pressure} hPa"

            Picasso.get().load("https:" + currentWeather.weather).into(binding.ivCurrentWeather)
        }


        viewModel.fetchWeatherData(55.3, 55.4)
       // fetchWeatherData(55.3, 55.4)
//        viewModel.weatherData.observe(viewLifecycleOwner, Observer { weatherData ->
//            // Обновление UI с данными о погоде
//            binding.tvName.text = "${weatherData.name} °C"
//            binding.tvTemp.text = "${weatherData.temperature} °C"
//            binding.tvWind.text = "${weatherData.windSpeed} m/s"
//            binding.tvHumidity.text = "${weatherData.humidity} %"
//            binding.tvPressure.text = "${weatherData.pressure} hPa"
//        })

       // viewModel.fetchWeatherData(55.139186, 54.649784)
    }





















}