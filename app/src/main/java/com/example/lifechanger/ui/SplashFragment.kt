package com.example.lifechanger.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.lifechanger.MainActivity
import com.example.lifechanger.R
import com.example.lifechanger.SharedViewModel
import com.example.lifechanger.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private val viewmodel: SharedViewModel by activityViewModels()
    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        // hide toolbar and bottom menu on SplashFragment
        (activity as MainActivity?)?.findViewById<View>(R.id.toolBar)?.visibility = View.GONE
        (activity as MainActivity?)?.findViewById<View>(R.id.bottomNav)?.visibility = View.GONE

        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel.getRandomQuote().observe(viewLifecycleOwner, Observer { randomQuote ->
            if (randomQuote != null) {
                // Aktualisieren Sie die Ansicht mit dem zufälligen Zitat
                binding.quoteTV.text = randomQuote.quote
            }
        })

        binding.nextBTN.setOnClickListener() {
            val navController = findNavController()
            navController.navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        // show toolbar and bottom menu when Splashfragment is destroyed
        (activity as MainActivity?)?.findViewById<View>(R.id.toolBar)?.visibility = View.VISIBLE
        (activity as MainActivity?)?.findViewById<View>(R.id.bottomNav)?.visibility = View.VISIBLE
    }
}