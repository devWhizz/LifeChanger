package com.example.lifechanger.ui

import android.os.Bundle
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

                        val amount = binding.addAccountInfoTI.text.toString()

                        if (amount.isNotEmpty()) {
                            val navController = findNavController()
                            navController.navigate(
                                PaymentFragmentDirections.actionPaymentFragmentToPaypalFragment(
                                    donationId,
                                    donationCreator,
                                    amount
                                )
                            )

                        } else {
                            showErrorToastAmount()
                            return@setOnClickListener
                        }

                    }
                    // set OnClickListener do navigate back (when canceled)
                    binding.cancelPaymentFAB.setOnClickListener {
                        findNavController().navigateUp()
                    }
                }
            }
    }

    private fun showErrorToastAmount() {
        Toast.makeText(
            requireContext(),
            (getString(R.string.errorMessagePayment)),
            Toast.LENGTH_SHORT
        ).show()
    }

}