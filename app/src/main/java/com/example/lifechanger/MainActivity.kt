package com.example.lifechanger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.lifechanger.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Set up BottomNavigationView with NavController
//        binding.bottomNav.setupWithNavController(navController)

        // setup OnBackPressedCallback method
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("handleBackPressed", "Back Button pressed")
                navController.navigateUp()
            }
        })
    }

    // Function to update Toolbar titles
    fun updateToolbarTitle(stringResId: Int) {
        val newTitle = getString(stringResId)
        binding.toolbarTV.text = newTitle
    }

    fun updateToolbarTitleDetail(string: String) {
        val newTitle = string
        binding.toolbarTV.text = newTitle
    }
}