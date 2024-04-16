package com.example.apairy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.apairy.databinding.FragmentApiaryInfoBinding
import com.example.apairy.databinding.FragmentWeatherBinding
import com.example.apairy.models.WeatherViewModel

class WeatherFragment : Fragment() {
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: WeatherViewModel

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

        viewModel.weatherData.observe(viewLifecycleOwner, Observer { weatherData ->
            // Обновление UI с данными о погоде
            binding.tvName.text = "${weatherData.name} °C"
            binding.tvTemp.text = "${weatherData.temperature} °C"
            binding.tvWind.text = "${weatherData.windSpeed} m/s"
            binding.tvHumidity.text = "${weatherData.humidity} %"
            binding.tvPressure.text = "${weatherData.pressure} hPa"
        })



        viewModel.fetchWeatherData(55.139186, 54.649784)
    }




}