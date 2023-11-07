package com.example.lifechanger.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.lifechanger.MainActivity
import com.example.lifechanger.R
import com.example.lifechanger.databinding.FragmentThankYouBinding

class ThankYouFragment : Fragment() {

    private lateinit var binding: FragmentThankYouBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThankYouBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set toolbar title
        (activity as MainActivity).updateToolbarTitle(R.string.thankyou)

        binding.backToHomeBTN.setOnClickListener {
            val navController = findNavController()
            navController.navigate(ThankYouFragmentDirections.actionThankYouFragmentToHomeFragment())
        }
    }
}