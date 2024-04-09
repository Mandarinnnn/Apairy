package com.example.apairy.fragments

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
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.apairy.MainActivity
import com.example.apairy.R
import com.example.apairy.databinding.FragmentHiveAddBinding
import com.example.apairy.databinding.FragmentHiveEditBinding
import com.example.apairy.models.Hive
import com.example.apairy.models.HiveViewModel
import java.text.SimpleDateFormat
import java.util.Date

class HiveEditFragment : Fragment(), MenuProvider {

    private var _binding: FragmentHiveEditBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var hiveViewModel: HiveViewModel

    private lateinit var currentHive: Hive

    private val args: HiveEditFragmentArgs by navArgs()

    private var menuSaved = false

    private lateinit var editMenuItem: MenuItem
    private lateinit var saveMenuItem: MenuItem
    private lateinit var editHiveView: View

    private val imgContract = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
        binding.ivPickHive.setImageURI(it)
        binding.ivPickHive.visibility = View.VISIBLE
        binding.ivPickHive.tag = it.toString()
        it?.let{
            requireActivity().contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHiveEditBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner, Lifecycle.State.RESUMED)

        hiveViewModel = ViewModelProvider(this).get(HiveViewModel::class.java)
        editHiveView = view


        currentHive = args.hive!!
        binding.etHiveNomer.setText(currentHive.name)
        binding.etHiveZametki.setText(currentHive.note)
        binding.enHiveMed.setText(currentHive.honey.toString())
        binding.enHiveSila.setText(currentHive.strength.toString())
        binding.enHiveRamki.setText(currentHive.frame.toString())
        binding.enHiveVes.setText(currentHive.weight.toString())
        binding.etHiveQueen.setText(currentHive.queen)

        binding.ivPickHive.tag = currentHive.imageURI
        binding.ivPickHive.setImageURI(Uri.parse(currentHive.imageURI))
        binding.ivPickHive.visibility = View.VISIBLE

        binding.btnPickHive.setOnClickListener{
            menuSaved = true
            imgContract.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }



    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.edit_menu, menu)
             editMenuItem = menu.findItem(R.id.action_save)
        saveMenuItem = menu.findItem(R.id.action_save)



        if(menuSaved){
            editMenuItem.isVisible = false
            saveMenuItem.isVisible = true
        }
    }

    private fun updateHiveInDB(view: View){
        val title = binding.etHiveNomer.text.toString()
        val note = binding.etHiveZametki.text.toString()

        val frame = binding.enHiveRamki.text.toString().toInt()
        val honey = binding.enHiveMed.text.toString().toInt()
        val strength = binding.enHiveSila.text.toString().toInt()
        val weight = binding.enHiveVes.text.toString().toInt()

        val queen = binding.etHiveQueen.text.toString()
        val imageURI = binding.ivPickHive.getTag().toString()

        if (title.isNotEmpty()){
            val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")
            val hive = Hive(
                currentHive.id, title, frame, honey, strength, weight, note, formatter.format(Date()), queen, imageURI
            )
            hiveViewModel.updateHive(hive)
            Toast.makeText(editHiveView.context,"Улей изменен", Toast.LENGTH_SHORT).show()
                //view.findNavController().popBackStack(R.id.hiveListFragment, false)
        }else{
            Toast.makeText(editHiveView.context,"Пожалуйста, введите данные", Toast.LENGTH_SHORT).show()
        }
    }

    private fun makeEnable(){
        binding.btnPickHive.isEnabled = true
        binding.etHiveNomer.isEnabled = true
        binding.etHiveZametki.isEnabled = true
        binding.enHiveMed.isEnabled = true
        binding.enHiveSila.isEnabled = true
        binding.enHiveRamki.isEnabled = true
        binding.enHiveVes.isEnabled = true
        binding.etHiveQueen.isEnabled = true
    }

    private fun makeDisable(){
        binding.btnPickHive.isEnabled = false
        binding.etHiveNomer.isEnabled = false
        binding.etHiveZametki.isEnabled = false
        binding.enHiveMed.isEnabled = false
        binding.enHiveSila.isEnabled = false
        binding.enHiveRamki.isEnabled = false
        binding.enHiveVes.isEnabled = false
        binding.etHiveQueen.isEnabled = false
    }





    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
//            R.id.action_edit -> {
//                editMenuItem.isVisible = false
//                makeEnable()
//                // Сделать элемент "Сохранить" видимым
//                saveMenuItem.isVisible = true
//
//                true
//            }

            R.id.action_save -> {
                updateHiveInDB(editHiveView)
                saveMenuItem.isVisible = false
                // Сделать элемент "Сохранить" видимым
                editMenuItem.isVisible = true
                makeDisable()
                true
            }
            else -> false
        }
    }


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }





    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.hideBottomNavigationView()
    }

    override fun onPause() {
        super.onPause()
        (activity as? MainActivity)?.showBottomNavigationView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}