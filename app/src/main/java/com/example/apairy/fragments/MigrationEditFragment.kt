package com.example.apairy.fragments

import android.app.DatePickerDialog
import android.net.Uri
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.apairy.MainActivity
import com.example.apairy.R
import com.example.apairy.databinding.FragmentMigrationEditBinding
import com.example.apairy.models.Hive
import com.example.apairy.models.LocationViewModel
import com.example.apairy.models.Migration
import com.example.apairy.models.MigrationViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class MigrationEditFragment : Fragment(),MenuProvider {
    private var _binding: FragmentMigrationEditBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var migrationViewModel: MigrationViewModel
    private lateinit var locationViewModel: LocationViewModel

    private lateinit var currentMigration: Migration

    private val args: MigrationEditFragmentArgs by navArgs()

    private var menuSaved = false

    private lateinit var editMigrationView: View

    private lateinit var editMenuItem: MenuItem
    private lateinit var saveMenuItem: MenuItem


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMigrationEditBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner, Lifecycle.State.RESUMED)

        migrationViewModel = ViewModelProvider(this).get(MigrationViewModel::class.java)

        editMigrationView = view

        currentMigration = args.migration!!
        binding.etMigrationName.setText(currentMigration.name)
        binding.etMigrationNote.setText(currentMigration.note)
        binding.etMigrationHoneyPlant.setText(currentMigration.honeyPlant)
        binding.etMigrationStartDate.setText(currentMigration.startDate)
        binding.etMigrationEndDate.setText(currentMigration.endDate)
        binding.enMigrationLongitude.setText(currentMigration.longitude?.toString() ?: "")
        binding.enMigrationLatitude.setText(currentMigration.latitude?.toString() ?: "")


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
            menuSaved = true
            it.findNavController().navigate(R.id.action_migrationEditFragment_to_mapsFragment)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.edit_menu, menu)

        (activity as? MainActivity)?.allocateTitle("")
    }


    private fun updateMigrationInDB(view: View){
        val title = binding.etMigrationName.text.toString()
        val honeyPlant = binding.etMigrationHoneyPlant.text.toString()

        val startDate = binding.etMigrationStartDate.text.toString()
        val endDate = binding.etMigrationEndDate.text.toString()


        val latitude = binding.enMigrationLatitude.text.toString().toDouble()
        val longitude = binding.enMigrationLongitude.text.toString().toDouble()

        val note = binding.etMigrationNote.text.toString()

        if (title.isNotEmpty()){
            val migration = Migration(
                currentMigration.id, title, honeyPlant, startDate, endDate, latitude, longitude,  note
            )
            migrationViewModel.updateMigration(migration)
            Toast.makeText(editMigrationView.context,"Кочевка изменена", Toast.LENGTH_SHORT).show()
            //view.findNavController().popBackStack(R.id.migrationListFragment, false)
        }else{
            Toast.makeText(editMigrationView.context,"Пожалуйста, введите данные", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteMigration(){
        migrationViewModel.deleteMigration(currentMigration)
        editMigrationView.findNavController().popBackStack()
    }




    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            android.R.id.home -> {
                findNavController().popBackStack()
                true
            }
            R.id.action_save -> {
                updateMigrationInDB(editMigrationView)
                true
            }

            R.id.action_delete -> {
                deleteMigration()
                true
            }
            else -> false
        }
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
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
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