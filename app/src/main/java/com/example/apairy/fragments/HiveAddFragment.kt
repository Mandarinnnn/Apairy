package com.example.apairy.fragments

import android.app.Activity
import android.content.Intent
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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.apairy.R
import com.example.apairy.databinding.FragmentHiveAddBinding
import com.example.apairy.databinding.FragmentHiveListBinding
import com.example.apairy.models.ApiaryInfoViewModel
import com.example.apairy.models.Hive
import com.example.apairy.models.HiveViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date


@AndroidEntryPoint
class HiveAddFragment : Fragment(), MenuProvider {

    private var _binding: FragmentHiveAddBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private val hiveViewModel: HiveViewModel by activityViewModels()
    //private lateinit var hiveViewModel: HiveViewModel
    private lateinit var addHiveView: View







    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHiveAddBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner, Lifecycle.State.RESUMED)


        //hiveViewModel = ViewModelProvider(this).get(HiveViewModel::class.java)
        addHiveView = view










    }



    private fun saveHiveToDB(view: View){
        val title = binding.etHiveName.text.toString()
        val note = binding.etHiveNote.text.toString()

        val frame = binding.enHiveFrame.text.toString()

        val queen = binding.etHiveQueen.text.toString()



        if (title.isNotEmpty() || note.isNotEmpty() || frame.isNotEmpty() || queen.isNotEmpty()){
            val hive = Hive(
                title, frame.toInt(), queen, note, false, true, false, false
            )
            hiveViewModel.insertHive(hive)
            Toast.makeText(addHiveView.context,"Улей добавлен", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.hiveListFragment, false)
        }else{
            Toast.makeText(addHiveView.context,"Пожалуйста, введите данные", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.hive_add_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.saveHiveMenu -> {
                saveHiveToDB(addHiveView)
                true
            }
            else -> false
        }
    }






    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}