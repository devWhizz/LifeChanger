package com.example.lifechanger.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lifechanger.MainActivity
import com.example.lifechanger.R
import com.example.lifechanger.SharedViewModel
import com.example.lifechanger.databinding.FragmentPaymentBinding
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import com.paypal.android.sdk.payments.PaymentConfirmation
import java.math.BigDecimal

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

        val donationCreator = args.donationCreator
        val donationId = args.donationId

        // set toolbar title
        (activity as MainActivity).updateToolbarTitle(R.string.payment)

        viewmodel.getDonationById(donationId)
            .observe(viewLifecycleOwner) { donation ->
                if (donation != null) {

                    // get language status
                    val targetLang = viewmodel.getTargetLanguage()
                    Log.d("Translation", "Target language is: $targetLang")

                    if (targetLang == "en") {
                        // settings to translate donation title with deepL API
                        viewmodel.translateDonationTitle(donation, targetLang)
                            .observe((view.context as LifecycleOwner)) { translatedTitle ->
                                binding.titlePaymentTV.text = translatedTitle
                            }
                    } else {
                        binding.titlePaymentTV.text = donation.title
                    }

                    binding.creatorPaymentTV.text = donationCreator

                    binding.paypalBTN.setOnClickListener {

                        val amountString = binding.addAmountTI.text.toString()
                        val amount = if (amountString.isNotEmpty()) {
                            BigDecimal(amountString)
                        } else {
                            showErrorToastAmount()
                            return@setOnClickListener
                        }

                        Log.d("PayPal", "Starting payment via PayPal")

                        // create PayPalConfiguration object
                        val config = PayPalConfiguration()
                            // use ENVIRONMENT_SANDBOX for testing
                            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                            .clientId("AUqoYVro8Er0LbE3fm0Fu8FML8r9QwiGkOjIMRvrhq0JXpYxHNIvk4GleGXNgm1yG4-N1EiNwoCWLgsj")

                        val paypalIntent = Intent(activity, PayPalService::class.java)
                        paypalIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
                        activity?.startService(paypalIntent)

                        // start PayPal payment
                        viewmodel.getPaypalEmailForDonation(donationId)
                            .observe(viewLifecycleOwner) { paypalEmail ->
                                if (paypalEmail != null) {
                                    val payment = PayPalPayment(
                                        amount,
                                        "EUR",
                                        donation.title,
                                        PayPalPayment.PAYMENT_INTENT_SALE
                                    )

                                    val intent =
                                        Intent(requireContext(), PaymentActivity::class.java)
                                    intent.putExtra(
                                        PayPalService.EXTRA_PAYPAL_CONFIGURATION,
                                        config
                                    )
                                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment)
                                    startActivityForResult(intent, PAYPAL_REQUEST_CODE, null)
                                } else {
                                    showErrorToast()
                                }
                            }
                    }
                    // set OnClickListener do navigate back (when canceled)
                    binding.cancelPaymentFAB.setOnClickListener {
                        findNavController().navigateUp()
                    }
                }
            }
    }

    companion object {
        const val PAYPAL_REQUEST_CODE = 1001 // Just an example
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("PayPal", "onActivityResult was started")
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("PayPal", "Payment successful")
                val confirm =
                    data?.getParcelableExtra<PaymentConfirmation>(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
                if (confirm != null) {
                    Log.d("PayPal", "Confirmation of successful payment: $confirm")
                    // payment successful
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("PayPal", "Payment cancelled")
                // payment canceled
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.d("PayPal", "Configuration error")
                // configuration error
            }
        }
    }

    private fun showErrorToast() {
        Toast.makeText(
            requireContext(),
            (getString(R.string.errorMessageCofiguration)),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showErrorToastAmount() {
        Toast.makeText(
            requireContext(),
            (getString(R.string.errorMessagePayment)),
            Toast.LENGTH_SHORT
        ).show()
    }

}