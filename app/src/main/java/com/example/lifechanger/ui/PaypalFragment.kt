package com.example.lifechanger.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lifechanger.MainActivity
import com.example.lifechanger.R
import com.example.lifechanger.SharedViewModel
import com.example.lifechanger.databinding.FragmentPaypalBinding


class PaypalFragment : Fragment() {

    private lateinit var binding: FragmentPaypalBinding
    private val viewmodel: SharedViewModel by activityViewModels()
    private val args: PaypalFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaypalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val donationCreator = args.donationCreator
        val donationAmount = args.donationAmount
        val donationId = args.donationId

        // set toolbar title
        (activity as MainActivity).updateToolbarTitle(R.string.paypalCheck)

        viewmodel.getPaypalEmailForDonation(donationId)
            .observe(viewLifecycleOwner) { paypalEmail ->
                val currency = "EUR"

                val paypalWebView = binding.paypalWebView
                // load Paypal page
                paypalWebView.settings.javaScriptEnabled = true
                paypalWebView.loadUrl("https://mediadesign.solutions/lifechanger?description=$donationCreator&amount=$donationAmount&currency=$currency&recipient_email=$paypalEmail")

                paypalWebView.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)

                        Log.d("WebViewURL", "Current URL: $url")

                        // check if URL contains this
                        if (url != null && url.contains("thankyou")) {
                            // if payment was successful, navigate to ThankYouFragment
                            val navController = findNavController()
                            navController.navigate(PaypalFragmentDirections.actionPaypalFragmentToThankYouFragment())
                        }
                    }
                }
            }

        // set OnClickListener do navigate back (when canceled)
        binding.cancelPayPalFAB.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}