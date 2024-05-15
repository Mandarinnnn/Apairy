package com.example.apairy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.replace
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.apairy.database.ApiaryDatabase
import com.example.apairy.database.HiveRepository
import com.example.apairy.databinding.ActivityMainBinding
import com.example.apairy.fragments.ApiaryInfoFragment
import com.example.apairy.fragments.HiveAddFragment
import com.example.apairy.fragments.HiveListFragment
import com.example.apairy.fragments.MenuFragment
import com.example.apairy.fragments.TheoryFragment
import com.example.apairy.fragments.WeatherFragment
import com.example.apairy.models.HiveViewModel
import com.example.apairy.models.MistakeViewModel
import com.example.apairy.models.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("")


        navController = findNavController(R.id.fragment_container)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        //bottomNavigationView.setupWithNavController(navController)

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        //supportActionBar?.hide()
        val appBarConfiguration = AppBarConfiguration
            .Builder(
                R.id.hiveListFragment,
                R.id.weatherFragment,
                R.id.theoryFragment,
                R.id.apiaryInfoFragment,
                R.id.menuFragment)
            .build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_hives -> {
                    navController.popBackStack(navController.graph.startDestinationId, false)
                    true
                }
                R.id.nav_weather -> {
                    navController.popBackStack(navController.graph.startDestinationId, false)
                    navController.navigate(R.id.weatherFragment)
                    true
                }

                R.id.nav_theory -> {
                    navController.popBackStack(navController.graph.startDestinationId, false)
                    navController.navigate(R.id.theoryFragment)
                    true
                }

                R.id.nav_info -> {
                    navController.popBackStack(navController.graph.startDestinationId, false)
                    navController.navigate(R.id.apiaryInfoFragment)
                    true
                }

                R.id.nav_menu -> {
                    navController.popBackStack(navController.graph.startDestinationId, false)
                    navController.navigate(R.id.menuFragment)
                    true
                }

                else -> false
            }
        }


    }
    fun hideBottomNavigationView() {
        bottomNavigationView.visibility = View.GONE
    }

    fun showBottomNavigationView() {
        bottomNavigationView.visibility = View.VISIBLE
    }

    fun changeBottomItem(){
        bottomNavigationView.selectedItemId = R.id.nav_hives
    }

    fun allocateTitle(title: String){

        supportActionBar?.let {
            supportActionBar?.setTitle(title) }


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment_container)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}