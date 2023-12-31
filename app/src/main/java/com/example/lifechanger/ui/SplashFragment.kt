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
import androidx.navigation.fragment.findNavController
import com.example.lifechanger.MainActivity
import com.example.lifechanger.R
import com.example.lifechanger.SharedViewModel
import com.example.lifechanger.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private val viewmodel: SharedViewModel by activityViewModels()
    private lateinit var binding: FragmentSplashBinding

    private val handler = Handler(Looper.getMainLooper())
    private var isAnimationRunning = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animateLogo()

        // get language status
        val targetLang = viewmodel.getTargetLanguage()

        // display quotes based on language status
        if (targetLang == "de") {
            viewmodel.getRandomQuote().observe(viewLifecycleOwner) { randomQuote ->
                if (randomQuote != null) {
                    // fill textview with random, animated quote
                    animateText(binding.quoteTV, randomQuote.quote)
                }
            }
        } else {
            viewmodel.getRandomQuote().observe(viewLifecycleOwner) { randomQuote ->
                if (randomQuote != null) {
                    // fill textview with random, animated, translated quote
                    animateText(binding.quoteTV, randomQuote.quoteTranslated)
                }
            }
        }
    }

    private fun animateLogo() {
        binding.logoIV.apply {
            scaleX = 0f
            scaleY = 0f
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
        if (isAnimationRunning) {
            return
        }

        isAnimationRunning = true
        var index = 0

        handler.postDelayed(object : Runnable {
            override fun run() {
                if (index < text.length) {
                    val currentText = text.substring(0, index + 1)
                    textView.text = currentText
                    index++
                    handler.postDelayed(this, 100)
                } else {
                    isAnimationRunning = false
                    Handler(Looper.getMainLooper()).postDelayed({
                        navigateToHomeFragment()
                    }, 2000)
                }
            }
        }, 1000)
    }

    private fun navigateToHomeFragment() {
        val navController = findNavController()
        navController.navigate(
            SplashFragmentDirections.actionSplashFragmentToHomeFragment()
        )
    }

    override fun onResume() {
        super.onResume()
        // hide toolbar and bottom menu on SplashFragment
        (activity as MainActivity?)?.findViewById<View>(R.id.toolBar)?.visibility = View.GONE
        (activity as MainActivity?)?.findViewById<View>(R.id.toolbarTV)?.visibility = View.GONE
        (activity as MainActivity?)?.findViewById<View>(R.id.toolbarBackBTN)?.visibility = View.GONE
        (activity as MainActivity?)?.findViewById<View>(R.id.bottomNav)?.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // show toolbar and bottom menu when SplashFragment is destroyed
        (activity as MainActivity?)?.findViewById<View>(R.id.toolBar)?.visibility = View.VISIBLE
        (activity as MainActivity?)?.findViewById<View>(R.id.toolbarTV)?.visibility = View.VISIBLE
        (activity as MainActivity?)?.findViewById<View>(R.id.toolbarBackBTN)?.visibility = View.VISIBLE
        (activity as MainActivity?)?.findViewById<View>(R.id.bottomNav)?.visibility = View.VISIBLE
    }
}