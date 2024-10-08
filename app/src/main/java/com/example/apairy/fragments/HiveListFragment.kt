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
import android.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.findFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.apairy.MainActivity
import com.example.apairy.R
import com.example.apairy.adapter.HiveAdapter
import com.example.apairy.databinding.FragmentHiveListBinding
import com.example.apairy.models.HiveViewModel


class HiveListFragment : Fragment(), SearchView.OnQueryTextListener, MenuProvider {


    private var _binding: FragmentHiveListBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    private lateinit var hiveAdapter: HiveAdapter
    private lateinit var hiveViewModel: HiveViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHiveListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner, Lifecycle.State.RESUMED)




        binding.rvHiveList.setHasFixedSize(true)
        binding.rvHiveList.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        hiveAdapter = HiveAdapter()
        binding.rvHiveList.adapter = hiveAdapter

        hiveViewModel = ViewModelProvider(this).get(HiveViewModel::class.java)

        hiveViewModel.getAllHives.observe(viewLifecycleOwner,){ list ->
            list?.let{
                hiveAdapter.updateHiveList(list)
            }
        }

        binding.floatingActionButton.setOnClickListener{

            it.findNavController().navigate(R.id.action_hiveListFragment_to_hiveAddFragment)

        }
    }





    override fun onDestroy() {
        super.onDestroy()
        (activity as? MainActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.hive_home_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        TODO("Not yet implemented")
    }

}