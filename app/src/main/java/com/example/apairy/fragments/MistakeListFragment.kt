package com.example.apairy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apairy.R
import com.example.apairy.adapter.MigrationAdapter
import com.example.apairy.adapter.MistakeAdapter
import com.example.apairy.databinding.FragmentMigrationListBinding
import com.example.apairy.databinding.FragmentMistakeListBinding
import com.example.apairy.models.MigrationViewModel
import com.example.apairy.models.MistakeViewModel


class MistakeListFragment : Fragment(), MenuProvider {


    private var _binding: FragmentMistakeListBinding? = null
    private val binding get() = _binding!!

    private lateinit var mistakeViewModel: MistakeViewModel
    private lateinit var adapter: MistakeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMistakeListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner, Lifecycle.State.RESUMED)



        binding.rvMistakeList.layoutManager = LinearLayoutManager(requireContext())
        adapter = MistakeAdapter()
        binding.rvMistakeList.adapter = adapter

        mistakeViewModel = ViewModelProvider(this).get(MistakeViewModel::class.java)

        mistakeViewModel.getAllMistakes.observe(viewLifecycleOwner,){ list ->
            list?.let{
                adapter.updateMistakeList(list)
            }
        }

        binding.floatingActionButton.setOnClickListener{

            it.findNavController().navigate(R.id.action_mistakeListFragment_to_mistakeAddFragment)

        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.hive_home_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        TODO("Not yet implemented")
    }


}