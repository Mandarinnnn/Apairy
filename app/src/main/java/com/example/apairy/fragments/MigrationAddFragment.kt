package com.example.apairy.fragments

import android.app.DatePickerDialog
import android.content.Intent
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
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.apairy.MainActivity
import com.example.apairy.R
import com.example.apairy.databinding.FragmentMigrationAddBinding
import com.example.apairy.models.LocationViewModel
import com.example.apairy.models.Migration
import com.example.apairy.models.MigrationViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.Calendar


@AndroidEntryPoint
class MigrationAddFragment : Fragment(), MenuProvider {

    private var _binding: FragmentMigrationAddBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private val migrationViewModel: MigrationViewModel by activityViewModels()

    private val imgContract = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
        it?.let{
            binding.ivMigration.setImageURI(it)
            binding.ivMigration.visibility = View.VISIBLE
            binding.ivMigration.tag = it.toString()
            requireActivity().contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

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


        //migrationViewModel = ViewModelProvider(this).get(MigrationViewModel::class.java)
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

        if (title.isEmpty() || binding.etMigrationHiveCount.text.toString().isEmpty()
            || binding.etMigrationStartDate.text.toString().isEmpty()) {
            Toast.makeText(addMigrationView.context,"Пожалуйста, введите данные", Toast.LENGTH_SHORT).show()
            return
        }

        val hiveCount = binding.etMigrationHiveCount.text.toString().toInt()
        val startDate = binding.etMigrationStartDate.text.toString()
        val endDate = binding.etMigrationEndDate.text.toString().takeIf { it.isNotEmpty() }
        val latitude = binding.enMigrationLatitude.text.toString().toDoubleOrNull()
        val longitude = binding.enMigrationLongitude.text.toString().toDoubleOrNull()
        val note = binding.etMigrationNote.text.toString().takeIf { it.isNotEmpty() }

        var imageURI: String?

        try {
            imageURI = binding.ivMigration.getTag().toString()
        } catch (e: Exception){
            imageURI = null
        }

        val migration = Migration(
            title, hiveCount, startDate, endDate, latitude, longitude,  note, imageURI,
            false, false, false)

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
        menuInflater.inflate(R.menu.add_migration_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.action_save_migr -> {
                saveMigrationToDB(addMigrationView)
                locationViewModel.clearSelectedLocation()
                true
            }
            R.id.action_photo_migr-> {
                imgContract.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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