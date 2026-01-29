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

        viewmodel.getPaypalEmailForDonation(donationId)
            .observe(viewLifecycleOwner) { paypalEmail ->
                val currency = "EUR"

                val paypalWebView = binding.paypalWebView

                // get language status
                val targetLang = viewmodel.getTargetLanguage()

                // get dark mode status
                val targetMode = viewmodel.isDarkModeEnabled()

                // load Paypal page
                paypalWebView.settings.javaScriptEnabled = true
                when {
                    targetLang == "DE" && !targetMode -> paypalWebView.loadUrl("https://mediadesign.solutions/lifechanger-de?description=$donationCreator&amount=$donationAmount&currency=$currency&recipient_email=$paypalEmail")
                    targetLang == "DE" && targetMode -> paypalWebView.loadUrl("https://mediadesign.solutions/lifechanger-de-dark?description=$donationCreator&amount=$donationAmount&currency=$currency&recipient_email=$paypalEmail")
                    targetLang == "EN" && !targetMode -> paypalWebView.loadUrl("https://mediadesign.solutions/lifechanger-en?description=$donationCreator&amount=$donationAmount&currency=$currency&recipient_email=$paypalEmail")
                    targetLang == "EN" && targetMode -> paypalWebView.loadUrl("https://mediadesign.solutions/lifechanger-en-dark?description=$donationCreator&amount=$donationAmount&currency=$currency&recipient_email=$paypalEmail")
                }
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

    override fun onResume() {
        super.onResume()
        // hide toolbarPaypalFragment
        (activity as MainActivity?)?.findViewById<View>(R.id.toolBar)?.visibility = View.GONE
        (activity as MainActivity?)?.findViewById<View>(R.id.toolbarTV)?.visibility = View.GONE
        (activity as MainActivity?)?.findViewById<View>(R.id.toolbarBackBTN)?.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // show toolbar when PaypalFragment is destroyed
        (activity as MainActivity?)?.findViewById<View>(R.id.toolBar)?.visibility = View.VISIBLE
        (activity as MainActivity?)?.findViewById<View>(R.id.toolbarTV)?.visibility = View.VISIBLE
        (activity as MainActivity?)?.findViewById<View>(R.id.toolbarBackBTN)?.visibility =
            View.VISIBLE
    }
}
