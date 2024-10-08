package com.example.apairy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.apairy.database.ApiaryDatabase
import com.example.apairy.database.HiveRepository
import com.example.apairy.databinding.ActivityMainBinding
import com.example.apairy.fragments.HiveAddFragment
import com.example.apairy.fragments.HiveListFragment
import com.example.apairy.fragments.TheoryFragment
import com.example.apairy.models.HiveViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        navController = findNavController(R.id.fragment_container)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        //supportActionBar?.hide()
        val appBarConfiguration = AppBarConfiguration
            .Builder(
                R.id.hiveListFragment,
                R.id.migrationListFragment,
                R.id.theoryFragment,
                R.id.mistakeListFragment)
            .build()
        setupActionBarWithNavController(navController, appBarConfiguration)


        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_hives -> {
                    navController.navigate(R.id.hiveListFragment)
                    true
                }
                R.id.nav_statistics -> {
                    navController.navigate(R.id.migrationListFragment)
                    true
                }

                R.id.nav_theory -> {
                    navController.navigate(R.id.theoryFragment)
                    true
                }

                R.id.nav_smth1 -> {
                    navController.navigate(R.id.weatherFragment)
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    true
                }

                R.id.nav_smth2 -> {
                    navController.navigate(R.id.apiaryInfoFragment)
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


    fun allocateTitle(title: String){

        getSupportActionBar()?.let {
            getSupportActionBar()?.setTitle(title) }


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment_container)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}