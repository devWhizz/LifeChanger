package com.example.lifechanger.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
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
    ): View {

        // hide toolbar and bottom menu on SplashFragment
        (activity as MainActivity?)?.findViewById<View>(R.id.toolBar)?.visibility = View.GONE
        (activity as MainActivity?)?.findViewById<View>(R.id.bottomNav)?.visibility = View.GONE

        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animateLogo()

        Handler(Looper.getMainLooper()).postDelayed({
            viewmodel.getRandomQuote().observe(viewLifecycleOwner, Observer { randomQuote ->
                if (randomQuote != null) {
                    // fill textview with random, animated quote
                    animateText(binding.quoteTV, randomQuote.quote)
                }
            })
        }, 2000)
    }

    private fun animateLogo() {
        binding.logoIV.apply {
            scaleX = 0.5f
            scaleY = 0.5f
            alpha = 0f

            animate().apply {
                duration = 2000
                scaleX(1.0f)
                scaleY(1.0f)
                alpha(1.0f)
            }
        }
    }

    private fun animateText(textView: TextView, text: String) {
        val handler = Handler(Looper.getMainLooper())
        var index = 0

        handler.postDelayed(object : Runnable {
            override fun run() {
                if (index < text.length) {
                    val currentText = text.substring(0, index + 1)
                    textView.text = currentText
                    index++
                    handler.postDelayed(this, 100)
                } else {
                    // Starten der Navigation nach 2000 Millisekunden (2 Sekunden)
                    Handler(Looper.getMainLooper()).postDelayed({
                        navigateToHomeFragment()
                    }, 2000)
                }
            }
        }, 100)
    }

    private fun navigateToHomeFragment() {
        val navController = findNavController()
        val options = NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_in_left)
            .build()
        navController.navigate(
            SplashFragmentDirections.actionSplashFragmentToHomeFragment(),
            options
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // show toolbar and bottom menu when Splashfragment is destroyed
        (activity as MainActivity?)?.findViewById<View>(R.id.toolBar)?.visibility = View.VISIBLE
        (activity as MainActivity?)?.findViewById<View>(R.id.bottomNav)?.visibility = View.VISIBLE
    }

}