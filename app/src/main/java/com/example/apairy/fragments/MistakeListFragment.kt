package com.example.apairy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apairy.MainActivity
import com.example.apairy.R
import com.example.apairy.adapter.MigrationAdapter
import com.example.apairy.adapter.MistakeAdapter
import com.example.apairy.databinding.FragmentMigrationListBinding
import com.example.apairy.databinding.FragmentMistakeListBinding
import com.example.apairy.models.MigrationViewModel
import com.example.apairy.models.MistakeViewModel
import com.example.apairy.utils.isWifiEnabled
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MistakeListFragment : Fragment(), MenuProvider,SearchView.OnQueryTextListener {


    private var _binding: FragmentMistakeListBinding? = null
    private val binding get() = _binding!!

    private val mistakeViewModel: MistakeViewModel by activityViewModels()
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

        //mistakeViewModel = ViewModelProvider(this).get(MistakeViewModel::class.java)

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

        val searchItem = menu.findItem(R.id.searchMenu)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            android.R.id.home -> {
                findNavController().popBackStack()
                true
            }
            else -> false
        }
    }
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText != null){
            adapter.filterMistakeList(newText)
        }
        return true
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