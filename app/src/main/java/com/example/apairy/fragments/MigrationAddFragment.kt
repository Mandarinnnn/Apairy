package com.example.apairy.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.apairy.MainActivity
import com.example.apairy.R
import com.example.apairy.databinding.FragmentHiveAddBinding
import com.example.apairy.databinding.FragmentHiveListBinding
import com.example.apairy.databinding.FragmentMigrationAddBinding
import com.example.apairy.models.Hive
import com.example.apairy.models.HiveViewModel
import com.example.apairy.models.LocationViewModel
import com.example.apairy.models.Migration
import com.example.apairy.models.MigrationViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class MigrationAddFragment : Fragment(), MenuProvider {

    private var _binding: FragmentMigrationAddBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var migrationViewModel: MigrationViewModel
    private lateinit var addMigrationView: View
    private lateinit var locationViewModel: LocationViewModel

    private val args: MigrationAddFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMigrationAddBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner, Lifecycle.State.RESUMED)

        locationViewModel = ViewModelProvider(requireActivity()).get(LocationViewModel::class.java)

        locationViewModel.selectedLocation.observe(viewLifecycleOwner) { location ->
            // Обновите UI с полученными данными о местоположении
            location?.let { nonNullLocation ->
                // Обновите UI с полученными данными о местоположении
                val (latitude, longitude) = nonNullLocation
                // Например, присвойте значения editText
                binding.enMigrationLatitude.setText(latitude.toString())
                binding.enMigrationLongitude.setText(longitude.toString())
            }
        }


        migrationViewModel = ViewModelProvider(this).get(MigrationViewModel::class.java)
        addMigrationView = view


        binding.etMigrationStartDate.setOnClickListener {
            showDatePicker(binding.etMigrationStartDate)
        }

        binding.etMigrationEndDate.setOnClickListener {
            showDatePicker(binding.etMigrationEndDate)
        }


        binding.btnMap.setOnClickListener{
            if( binding.enMigrationLatitude.text.toString().isNotEmpty() && binding.enMigrationLongitude.text.toString().isNotEmpty()){
                locationViewModel.setSelectedLocation(
                    binding.enMigrationLatitude.text.toString().toDouble(),
                    binding.enMigrationLongitude.text.toString().toDouble()
                )
            }
            it.findNavController().navigate(R.id.action_migrationAddFragment_to_mapsFragment)
        }

    }

    private fun saveMigrationToDB(view: View){
        val title = binding.etMigrationName.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(addMigrationView.context,"Пожалуйста, введите данные", Toast.LENGTH_SHORT).show()
            return
        }

        val honeyPlant = binding.etMigrationHoneyPlant.text.toString().takeIf { it.isNotEmpty() }
        val startDate = binding.etMigrationStartDate.text.toString().takeIf { it.isNotEmpty() }
        val endDate = binding.etMigrationEndDate.text.toString().takeIf { it.isNotEmpty() }
        val latitude = binding.enMigrationLatitude.text.toString().toDoubleOrNull()
        val longitude = binding.enMigrationLongitude.text.toString().toDoubleOrNull()
        val note = binding.etMigrationNote.text.toString().takeIf { it.isNotEmpty() }

        val migration = Migration(
            null, title, honeyPlant, startDate, endDate, latitude, longitude,  note
        )
        migrationViewModel.insertMigration(migration)
        Toast.makeText(addMigrationView.context,"Кочевка добавлена", Toast.LENGTH_SHORT).show()
        view.findNavController().popBackStack(R.id.migrationListFragment, false)

    }




    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val selectedDate = "${dayOfMonth}.${month + 1}.${year}" // Пример формата даты
                editText.setText(selectedDate)
            }, year, month, day
        )
        datePickerDialog.show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.hive_add_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.saveHiveMenu -> {
                saveMigrationToDB(addMigrationView)
                locationViewModel.clearSelectedLocation()
                true
            }
            else -> false
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