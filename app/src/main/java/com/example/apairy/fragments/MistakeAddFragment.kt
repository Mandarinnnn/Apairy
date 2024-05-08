package com.example.apairy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.apairy.MainActivity
import com.example.apairy.R
import com.example.apairy.databinding.FragmentMistakeAddBinding
import com.example.apairy.models.MigrationViewModel
import com.example.apairy.models.Mistake
import com.example.apairy.models.MistakeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MistakeAddFragment : Fragment(), MenuProvider {
    private var _binding: FragmentMistakeAddBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val mistakeViewModel: MistakeViewModel by activityViewModels()
    private lateinit var addMistakeView: View



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMistakeAddBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner, Lifecycle.State.RESUMED)


        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.years_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Указание внешнего вида выпадающего списка
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Применение адаптера к Spinner
            binding.yearSpinner.adapter = adapter
        }


       // mistakeViewModel = ViewModelProvider(this).get(MistakeViewModel::class.java)
        addMistakeView = view



    }


    private fun saveMistake(view: View){
        val title = binding.etMistakeName.text.toString().trim()
        val year = binding.yearSpinner.selectedItem.toString()

        if (title.isEmpty()) {
            Toast.makeText(addMistakeView.context,"Пожалуйста, введите данные", Toast.LENGTH_SHORT).show()
            return
        }

        val solution = binding.etMistakeSolution.text.toString().takeIf { it.isNotEmpty() }



        val mistake = Mistake(
            title, solution!!, year,true,false,false
        )
        mistakeViewModel.createMistake(mistake)
        Toast.makeText(addMistakeView.context,"Информация добавлена", Toast.LENGTH_SHORT).show()
        view.findNavController().popBackStack()

    }



    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.hive_add_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.saveHiveMenu -> {
                saveMistake(addMistakeView)
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