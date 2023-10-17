package com.example.lifechanger.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lifechanger.MainActivity
import com.example.lifechanger.R
import com.example.lifechanger.SharedViewModel
import com.example.lifechanger.databinding.FragmentPaymentBinding

class PaymentFragment : Fragment() {

    private lateinit var binding: FragmentPaymentBinding
    private val viewmodel: SharedViewModel by activityViewModels()
    private val args: PaymentFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val donationTitle = args.donationTitle
        val donationCreator = args.donationCreator
        val donationId = args.donationId

        // set toolbar title
        (activity as MainActivity).updateToolbarTitle(R.string.payment)

        binding.creatorPayment.text = donationCreator
        binding.titlePayment.text = donationTitle

        binding.paypalBTN.setOnClickListener {
            // observe liveData object to get PayPal email address
            viewmodel.getPaypalEmailForDonation(donationId)
                .observe(viewLifecycleOwner) { paypalEmail ->
                    val amount = binding.addAmountTI.text.toString()
                    val currency = "EUR"
                    val paypalWebUrl =
                        "https://www.paypal.com/cgi-bin/webscr?cmd=_xclick&business=$paypalEmail&amount=$amount&currency_code=$currency"

                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(paypalWebUrl))
                    startActivity(browserIntent)
                }
        }

        // set OnClickListener do navigate back (when canceled)
        binding.cancelPaymentFAB.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}