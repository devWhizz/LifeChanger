package com.example.lifechanger

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.lifechanger.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // change color of status bar
        window.statusBarColor = this.getColor(R.color.main)

        // restore and set dark mode status from SharedPreferences
        val sharedPrefsDarkMode =
            getSharedPreferences("SharedPreferencesDarkMode", Context.MODE_PRIVATE)
        val isDarkModeEnabled = sharedPrefsDarkMode.getBoolean("darkModeEnabled", false)

        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // set App language
        if (isFirstRun()) {
            setAppLanguage("de")
        } else {
            // restore language setting from SharedPreferences
            restoreAppLanguage()
        }

        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        val db = Firebase.firestore

        // set up OnClickListener to navigate back to previous screen
        binding.toolbarBackBTN.setOnClickListener {
            val currentDestinationId = navController.currentDestination?.id
            if (currentDestinationId == R.id.categoryFragment) {
                // prevent navigating back to AddDonationFragment after creating a donation
                navController.navigate(R.id.homeFragment)
                return@setOnClickListener
            } else navController.navigateUp()
        }

        // set up BottomNavigationView with NavController
        binding.bottomNav.setupWithNavController(navController)

        val options = NavOptions.Builder()
            .setEnterAnim(R.anim.slide_up)
            .setExitAnim(R.anim.fade_out)
            .build()

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

                R.id.searchFragment -> {
                    navController.navigate(R.id.searchFragment, null, options)
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
                if (currentDestinationId == R.id.categoryFragment) {
                    // prevent navigating back to AddDonationFragment after creating a donation
                    navController.navigate(R.id.homeFragment)
                    return
                }
                navController.navigateUp()
                Log.d("handleBackPressed", "Back Button pressed")
            }
        })

    }

    // method to update toolbar titles
    fun updateToolbarTitle(stringResId: Int) {
        val newTitle = getString(stringResId)
        binding.toolbarTV.text = newTitle
    }

    fun updateToolbarTitleDetail(string: String) {
        binding.toolbarTV.text = string
    }

    private fun isFirstRun(): Boolean {
        val sharedPrefsLanguage =
            getSharedPreferences("SharedPreferencesLanguage", Context.MODE_PRIVATE)
        return !sharedPrefsLanguage.contains("isGermanLanguageSelected")
    }

    private fun restoreAppLanguage() {
        val sharedPrefsLanguage =
            getSharedPreferences("SharedPreferencesLanguage", Context.MODE_PRIVATE)
        val isGermanLanguageSelected =
            sharedPrefsLanguage.getBoolean("isGermanLanguageSelected", false)

        if (isGermanLanguageSelected) {
            setAppLanguage("de")
        } else {
            setAppLanguage("en")
        }
    }

    private fun setAppLanguage(languageCode: String) {
        val resources = resources
        val configuration = resources.configuration
        val displayMetrics = resources.displayMetrics
        val locale = Locale(languageCode)
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, displayMetrics)
    }
}