package com.example.apairy.fragments

import android.app.AlertDialog
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.apairy.MainActivity
import com.example.apairy.R
import com.example.apairy.models.LocationViewModel
import com.example.apairy.utils.ApiaryLocation

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private var currentMarker: Marker? = null

    private val args: MapsFragmentArgs by navArgs()

    private lateinit var apiaryLocation: ApiaryLocation

    private lateinit var fragment: String

    private lateinit var locationViewModel: LocationViewModel

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */


        if(fragment=="weather"){
            if(apiaryLocation.areCoordinatesSaved()){

                val loc = apiaryLocation.getLocation()

                val LatLng = LatLng(loc.first,
                    loc.second)


                val location = googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng)
                        .title("Пасека"))

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng, 10F))
            }
        }else{
            if(locationViewModel.isLocationSelected()){

                val LatLng = LatLng(locationViewModel.selectedLocation.value?.first!!,
                    locationViewModel.selectedLocation.value?.second!!)


                val location = googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng)
                        .title("Пасека"))

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng, 10F))
            }
        }



        googleMap.setOnMapLongClickListener { latLng ->
            currentMarker?.remove()
            // Создание метки
            val markerOptions = MarkerOptions()
                .position(latLng)
                .title("Моя метка")

            // Добавление метки на карту и сохранение ссылки на нее
            currentMarker = googleMap.addMarker(markerOptions)
            val latitude = latLng.latitude
            val longitude = latLng.longitude
            // Отображение диалога для выбора местоположения
            showLocationDialog(latitude, longitude)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        locationViewModel = ViewModelProvider(requireActivity()).get(LocationViewModel::class.java)

        try {
            fragment = args.fragment
        } catch (e: Exception){
            fragment = " "
        }
        apiaryLocation = ApiaryLocation(requireContext())
        mapFragment?.getMapAsync(callback)
    }


    private fun showLocationDialog(latitude: Double, longitude: Double) {
        // Создание диалогового окна
        val dialogBuilder = AlertDialog.Builder(requireContext())

        // Установка заголовка и сообщения
        dialogBuilder.setTitle("Выбрать местоположение")
            .setMessage("Вы хотите добавить эту метку?")

        // Добавление кнопок "Отмена" и "ОК"
        dialogBuilder.setPositiveButton("ОК") { dialog, which ->
            onLocationSelected(latitude,longitude)
                //findNavController().navigate(R.id.action_mapsFragment_to_migrationAddFragment)
            findNavController().popBackStack()
        }
        dialogBuilder.setNegativeButton("Отмена") { dialog, which ->
            // Удаление метки при отмене
            //currentMarker?.remove()
            //currentMarker = null
            //findNavController().navigate(R.id.action_mapsFragment_to_migrationAddFragment)
        }

        // Отображение диалогового окна
        dialogBuilder.show()
    }





    private fun onLocationSelected(latitude: Double, longitude: Double) {

        if(fragment=="weather"){
            apiaryLocation.saveLocation(latitude, longitude)
        }else{
            locationViewModel.setSelectedLocation(latitude, longitude)
        }

    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.hideBottomNavigationView()
    }

    override fun onPause() {
        super.onPause()
        (activity as? MainActivity)?.showBottomNavigationView()
    }

}