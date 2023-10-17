package com.example.lifechanger.ui

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.example.lifechanger.MainActivity
import com.example.lifechanger.R
import com.example.lifechanger.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set toolbar title
        (activity as MainActivity).updateToolbarTitle(R.string.settings)

        val darkModeSwitch = binding.darkModeSwitch
        darkModeSwitch.isChecked = isDarkModeEnabled()

        // set clicklistener for switch toggle
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            saveDarkModeStatus(isChecked)
        }
    }

    private fun isDarkModeEnabled(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }
    private fun saveDarkModeStatus(isDarkModeEnabled: Boolean) {
        val sharedPrefs = requireContext().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean("darkModeEnabled", isDarkModeEnabled)
        editor.apply()
    }

}