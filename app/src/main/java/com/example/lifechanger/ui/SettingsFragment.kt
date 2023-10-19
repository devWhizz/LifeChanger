package com.example.lifechanger.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
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

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set toolbar title
        (activity as MainActivity).updateToolbarTitle(R.string.settings)

        // setup dark mode toggle
        val darkModeSwitch: Switch = binding.darkModeSwitch
        darkModeSwitch.isChecked = isDarkModeEnabled()

        // set clicklistener for dark mode switch
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            saveDarkModeStatus(isChecked)
        }

        // setup language toggle
        val languageSwitch: Switch = binding.languageSwitch
        languageSwitch.isChecked = isGermanLanguageSelected()

        // set clicklistener for language switch
        languageSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setAppLanguage("de")
            } else {
                setAppLanguage("en")
            }
            saveLanguageStatus(isChecked)
            // restart activity to apply language changes
            requireActivity().recreate()
        }

        // setup bottom sheet
        val openBottomSheetButton = binding.aboutTV
        openBottomSheetButton.setOnClickListener {
            val bottomSheetFragment = AboutBottomSheetFragment()
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        }
    }

    private fun isDarkModeEnabled(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    private fun saveDarkModeStatus(isDarkModeEnabled: Boolean) {
        val sharedPrefs =
            requireContext().getSharedPreferences("SharedPreferencesDarkMode", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean("darkModeEnabled", isDarkModeEnabled)
        editor.apply()
    }

    private fun isGermanLanguageSelected(): Boolean {
        val selectedLanguage = getAppLanguage()
        return selectedLanguage == "de"
    }

    private fun getAppLanguage(): String {
        val sharedPrefs =
            requireContext().getSharedPreferences("SharedPreferencesLanguage", Context.MODE_PRIVATE)
        return sharedPrefs.getString("targetLanguage", "de") ?: "de"
    }

    private fun setAppLanguage(languageCode: String) {
        val sharedPrefs =
            requireContext().getSharedPreferences("SharedPreferencesLanguage", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putString("targetLanguage", languageCode)
        editor.apply()
    }

    private fun saveLanguageStatus(isGermanLanguageSelected: Boolean) {
        val sharedPrefs =
            requireContext().getSharedPreferences("SharedPreferencesLanguage", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean("isGermanLanguageSelected", isGermanLanguageSelected)
        editor.apply()
    }
}