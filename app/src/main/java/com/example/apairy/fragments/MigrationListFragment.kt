package com.example.apairy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.apairy.R
import com.example.apairy.adapter.HiveAdapter
import com.example.apairy.adapter.MigrationAdapter
import com.example.apairy.adapter.TheoryAdapter
import com.example.apairy.databinding.FragmentMigrationAddBinding
import com.example.apairy.databinding.FragmentMigrationListBinding
import com.example.apairy.models.HiveViewModel
import com.example.apairy.models.MigrationViewModel
import androidx.appcompat.widget.SearchView
import com.example.apairy.MainActivity


class MigrationListFragment : Fragment(),MenuProvider, SearchView.OnQueryTextListener {

    private var _binding: FragmentMigrationListBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var migrationViewModel: MigrationViewModel
    private lateinit var adapter: MigrationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMigrationListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner, Lifecycle.State.RESUMED)



        binding.rvMigrationList.layoutManager = LinearLayoutManager(requireContext())
        adapter = MigrationAdapter()
        binding.rvMigrationList.adapter = adapter

        migrationViewModel = ViewModelProvider(this).get(MigrationViewModel::class.java)

        migrationViewModel.getAllMigrations.observe(viewLifecycleOwner,){ list ->
            list?.let{
                adapter.updateMigrationList(list)
            }
        }

        binding.floatingActionButton.setOnClickListener{

            it.findNavController().navigate(R.id.action_migrationListFragment_to_migrationAddFragment)

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
        TODO("Not yet implemented")
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText != null){
            adapter.filterMigrationList(newText)
        }
        return true
    }
}