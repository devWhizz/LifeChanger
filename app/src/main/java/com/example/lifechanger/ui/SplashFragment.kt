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

        // display quote based on language status (nur 1 Observer + Fallback)
        viewmodel.getRandomQuote().observe(viewLifecycleOwner) { randomQuote ->
            // wenn Animation schon läuft, nicht nochmal starten (LiveData kann mehrfach feuern)
            if (isAnimationRunning) return@observe

            val rawText = if (targetLang.equals("DE", ignoreCase = true)) {
                randomQuote?.quote
            } else {
                randomQuote?.quoteTranslated
            }

            // Fallback falls null/leer (sonst kann es beim substring/Parcel krachen)
            val safeText = rawText?.takeIf { it.isNotBlank() }
                ?: if (targetLang.equals("DE", ignoreCase = true)) "Willkommen" else "Welcome"

            animateText(binding.quoteTV, safeText)
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
        if (isAnimationRunning) return

        val safeText = text.ifBlank { " " }

        isAnimationRunning = true
        var index = 0

        handler.postDelayed(object : Runnable {
            override fun run() {
                // Fragment/View evtl. schon weg → abbrechen
                if (!isAdded || view == null) return

                if (index < safeText.length) {
                    textView.text = safeText.substring(0, index + 1)
                    index++
                    handler.postDelayed(this, 100)
                } else {
                    isAnimationRunning = false
                    handler.postDelayed({
                        navigateToHomeFragment()
                    }, 2000)
                }
            }
        }, 1000)
    }

    private fun navigateToHomeFragment() {
        if (!isAdded || view == null) return
        findNavController().navigate(
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
        handler.removeCallbacksAndMessages(null)
        // show toolbar and bottom menu when SplashFragment is destroyed
        (activity as MainActivity?)?.findViewById<View>(R.id.toolBar)?.visibility = View.VISIBLE
        (activity as MainActivity?)?.findViewById<View>(R.id.toolbarTV)?.visibility = View.VISIBLE
        (activity as MainActivity?)?.findViewById<View>(R.id.toolbarBackBTN)?.visibility = View.VISIBLE
        (activity as MainActivity?)?.findViewById<View>(R.id.bottomNav)?.visibility = View.VISIBLE
    }
}