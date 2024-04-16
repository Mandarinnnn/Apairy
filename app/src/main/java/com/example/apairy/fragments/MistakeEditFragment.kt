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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.apairy.MainActivity
import com.example.apairy.R
import com.example.apairy.databinding.FragmentMistakeAddBinding
import com.example.apairy.databinding.FragmentMistakeEditBinding
import com.example.apairy.models.LocationViewModel
import com.example.apairy.models.Migration
import com.example.apairy.models.MigrationViewModel
import com.example.apairy.models.Mistake
import com.example.apairy.models.MistakeViewModel

class MistakeEditFragment : Fragment(),MenuProvider {

    private var _binding: FragmentMistakeEditBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var currentMistake: Mistake
    private lateinit var mistakeViewModel: MistakeViewModel
    private lateinit var editMistakeView: View
    private val args: MistakeEditFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMistakeEditBinding.inflate(inflater, container, false)
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


        mistakeViewModel = ViewModelProvider(this).get(MistakeViewModel::class.java)

        editMistakeView = view

        currentMistake = args.mistake!!
        binding.etMistakeName.setText(currentMistake.name)
        binding.etMistakeSolution.setText(currentMistake.solution)


        val yearsArray = resources.getStringArray(R.array.years_array)

        val defaultYearIndex = yearsArray.indexOf( currentMistake.year)
        binding.yearSpinner.setSelection(defaultYearIndex)

    }

    private fun updateMistake(view: View){
        val title = binding.etMistakeName.text.toString().trim()
        val year = binding.yearSpinner.selectedItem.toString()

        if (title.isEmpty()) {
            Toast.makeText(editMistakeView.context,"Пожалуйста, введите данные", Toast.LENGTH_SHORT).show()
            return
        }

        val solution = binding.etMistakeSolution.text.toString().takeIf { it.isNotEmpty() }



        val mistake = Mistake(
            currentMistake.id, title, solution, year
        )
        mistakeViewModel.updateMistake(mistake)
        Toast.makeText(editMistakeView.context,"Информация добавлена", Toast.LENGTH_SHORT).show()
        view.findNavController().popBackStack()

    }




    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.edit_menu, menu)

        (activity as? MainActivity)?.allocateTitle("")
    }

    private fun deleteMistake(){
        mistakeViewModel.deleteMistake(currentMistake)
        editMistakeView.findNavController().popBackStack()
    }


    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            android.R.id.home -> {
                findNavController().popBackStack()
                true
            }
            R.id.action_save -> {
                updateMistake(editMistakeView)
                true
            }

            R.id.action_delete -> {
                deleteMistake()
                true
            }
            else -> false
        }
    }
}