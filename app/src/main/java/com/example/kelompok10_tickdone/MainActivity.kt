package com.example.kelompok10_tickdone

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.kelompok10_tickdone.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager = supportFragmentManager

        FirebaseApp.initializeApp(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup toolbar
        setSupportActionBar(binding.toolbar)

        // Get the NavHostFragment and NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        // Define the top-level destinations in the app (no back button for these)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.addFragment)
        )

        // Set up the action bar (toolbar) with the NavController and AppBarConfiguration
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Handle bottom navigation
        binding.bottomNavigation.background = null
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> navController.navigate(R.id.homeFragment)
                R.id.bottom_time -> navController.navigate(R.id.timeFragment)
                R.id.bottom_profile -> navController.navigate(R.id.profileFragment)
                R.id.bottom_calender -> navController.navigate(R.id.calenderFragment)
                else -> false
            }
            true
        }

        binding.btnAdd.setOnClickListener {
            val currentDestination = navController.currentDestination?.id
            when (currentDestination) {
                R.id.homeFragment -> navController.navigate(R.id.action_homeFragment_to_addFragment)
                R.id.timeFragment -> navController.navigate(R.id.action_timeFragment_to_addFragment)
                R.id.calenderFragment -> navController.navigate(R.id.action_calenderFragment_to_addFragment)
                R.id.profileFragment -> navController.navigate(R.id.action_profileFragment_to_addFragment)
                else -> Toast.makeText(this, "Navigation not available from this screen", Toast.LENGTH_SHORT).show()
            }
        }


        // Optionally, set up the default fragment at startup
        navController.navigate(R.id.homeFragment) // Navigate to HomeFragment by default
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    private fun openFragment(fragment: Fragment){
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment)
        fragmentTransaction.commit()
    }
}
