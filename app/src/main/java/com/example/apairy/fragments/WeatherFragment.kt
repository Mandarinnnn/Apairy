package com.example.apairy.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.apairy.MainActivity
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
import com.example.apairy.utils.ApiaryLocation
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherFragment : Fragment(), MenuProvider {
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: WeatherViewModel
    private lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter

    private lateinit var futureWeatherAdapter: FutureWeatherAdapter

    private lateinit var apiaryLocation: ApiaryLocation


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

        apiaryLocation = ApiaryLocation(requireContext())

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner, Lifecycle.State.RESUMED)

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


        binding.rvFutureWeather.layoutManager = LinearLayoutManager(requireContext())
        futureWeatherAdapter = FutureWeatherAdapter()
        binding.rvFutureWeather.setHasFixedSize(true)
        binding.rvFutureWeather.requestLayout()

        binding.rvFutureWeather.adapter =  futureWeatherAdapter

        viewModel.futureWeather.observe(viewLifecycleOwner,){ list ->
            list?.let{
                futureWeatherAdapter.updateFutureWeatherList(list)
            }
        }


        viewModel.currentWeather.observe(viewLifecycleOwner,){ currentWeather ->
            binding.tvName.text = "${currentWeather.name}"
            binding.tvTemp.text = "${currentWeather.temperature} °"
            binding.tvWind.text = "${currentWeather.windSpeed} км/ч"
            binding.tvHumidity.text = "${currentWeather.humidity} %"
            val pressure = (currentWeather.pressure * 0.750063755419211).toInt()
            binding.tvPressure.text = "$pressure мм рт. ст."

            Picasso.get().load("https:" + currentWeather.weather).into(binding.ivCurrentWeather)
        }


        fetchWeatherData()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.weather_menu, menu)

    }


    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.action_map -> {
                val direction = WeatherFragmentDirections.actionWeatherFragmentToMapsFragment("weather")
                findNavController().navigate(direction)
                true
            }
            else -> false
        }
    }

    override fun onResume() {
        super.onResume()
        fetchWeatherData()
    }


    private fun fetchWeatherData(){
        if(apiaryLocation.areCoordinatesSaved()){
            val location = apiaryLocation.getLocation()
            viewModel.fetchWeatherData(location.first, location.second)
        }else{
            binding.cvWeather.visibility = View.GONE
            binding.tvDontHaveCoord.visibility = View.VISIBLE
        }
    }

}