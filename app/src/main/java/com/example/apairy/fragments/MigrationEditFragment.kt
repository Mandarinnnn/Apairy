package com.example.apairy.fragments

import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Intent
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
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
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
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


@AndroidEntryPoint
class MigrationEditFragment : Fragment(),MenuProvider {
    private var _binding: FragmentMigrationEditBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val migrationViewModel: MigrationViewModel by activityViewModels()


    private lateinit var locationViewModel: LocationViewModel

    private lateinit var currentMigration: Migration

    private val args: MigrationEditFragmentArgs by navArgs()

    private var menuSaved = false

    private lateinit var editMigrationView: View

    private lateinit var editMenuItem: MenuItem
    private lateinit var saveMenuItem: MenuItem


    private val imgContract = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
        it?.let{
            binding.ivMigration.setImageURI(it)
            binding.ivMigration.visibility = View.VISIBLE
            binding.ivMigration.tag = it.toString()
            requireActivity().contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }


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

        //migrationViewModel = ViewModelProvider(this).get(MigrationViewModel::class.java)

        editMigrationView = view

        currentMigration = args.migration!!
        binding.etMigrationName.setText(currentMigration.name)
        binding.etMigrationNote.setText(currentMigration.note)
        binding.etMigrationHoneyPlant.setText(currentMigration.hiveCount.toString())
        binding.etMigrationStartDate.setText(currentMigration.startDate)
        binding.etMigrationEndDate.setText(currentMigration.endDate)
        binding.enMigrationLongitude.setText(currentMigration.longitude?.toString() ?: "")
        binding.enMigrationLatitude.setText(currentMigration.latitude?.toString() ?: "")



        binding.ivMigration.tag = currentMigration.imageURI

        if (currentMigration.imageURI != null){
            if(isImageFileExists(requireContext().contentResolver, Uri.parse(currentMigration.imageURI))){
                binding.ivMigration.setImageURI(Uri.parse(currentMigration.imageURI))
                binding.ivMigration.visibility = View.VISIBLE
            }
        }



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
        menuInflater.inflate(R.menu.edit_migration_menu, menu)

        (activity as? MainActivity)?.allocateTitle("")
    }


    private fun updateMigrationInDB(view: View){
        val title = binding.etMigrationName.text.toString()

        if (title.isEmpty() || binding.etMigrationHoneyPlant.text.toString().isEmpty()
            || binding.etMigrationStartDate.text.toString().isEmpty()) {
            Toast.makeText(editMigrationView.context,"Пожалуйста, введите данные", Toast.LENGTH_SHORT).show()
            return
        }


        val hiveCount = binding.etMigrationHoneyPlant.text.toString().toInt()
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


        if (title.isNotEmpty()){
            val migration = Migration(
               title, hiveCount, startDate, endDate, latitude, longitude,  note, imageURI,
                currentMigration.isLocallyNew, true, currentMigration.isLocallyDeleted,
                currentMigration.id
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
                locationViewModel.clearSelectedLocation()
                findNavController().popBackStack()
                true
            }
            R.id.action_save_migrr -> {
                updateMigrationInDB(editMigrationView)
                locationViewModel.clearSelectedLocation()
                true
            }

            R.id.action_delete_migrr -> {
                deleteMigration()
                true
            }
            R.id.action_photo_migrr-> {
                imgContract.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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



    fun isImageFileExists(contentResolver: ContentResolver, imageUri: Uri): Boolean {
        return try {
            requireActivity().contentResolver.takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val inputStream = contentResolver.openInputStream(imageUri)
            inputStream?.close()
            true
        } catch (e: Exception) {
            false
        }
    }

}