package com.example.lifechanger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.lifechanger.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        val db = Firebase.firestore

        // set up OnClickListener to navigate back to previous screen
        binding.toolbarBackBTN.setOnClickListener {
            navController.navigateUp()
        }

        // set up BottomNavigationView with NavController
        binding.bottomNav.setupWithNavController(navController)

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                }

                R.id.favoritesFragment -> {
                    navController.navigate(R.id.favoritesFragment)
                }

                R.id.settingsFragment -> {
                    navController.navigate(R.id.settingsFragment)
                }

                else -> {
                    navController.navigateUp()
                }
            }
            true
        }

        // setup OnBackPressedCallback method
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentDestinationId = navController.currentDestination?.id
                if (currentDestinationId == R.id.homeFragment) {
                    // prevent navigating back to SplashFragment
                    return
                }
                Log.d("handleBackPressed", "Back Button pressed")
                navController.navigateUp()
            }
        })
    }

    // function to update Toolbar titles
    fun updateToolbarTitle(stringResId: Int) {
        val newTitle = getString(stringResId)
        binding.toolbarTV.text = newTitle
    }

    fun updateToolbarTitleDetail(string: String) {
        binding.toolbarTV.text = string
    }
}