package com.example.lifechanger.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.lifechanger.MainActivity
import com.example.lifechanger.SharedViewModel
import com.example.lifechanger.databinding.FragmentDonationDetailBinding

class DonationDetailFragment : Fragment() {

    private val viewmodel: SharedViewModel by activityViewModels()
    private lateinit var binding: FragmentDonationDetailBinding

    // receive arguments passed from previous fragment on navigation
    private val args: DonationDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDonationDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val donationIndex = args.donationIndex
        val category = args.category

        // select correct donation based on the category and index passed to display details in fragment
        viewmodel.getDonationByCategory(category)
            .observe(viewLifecycleOwner, Observer { donations ->
                if (donationIndex >= 0 && donationIndex < donations.size) {
                    val donation = donations[donationIndex]

                    // set toolbar title
                    (activity as MainActivity).updateToolbarTitleDetail(donation.title)

                    binding.donationCompanyDetailTV.text = donation.creator
                    binding.donationTitleDetailTV.text = donation.title

                    // use Coil to load images
                    binding.donationImageDetailIV.load(
                        donation.image.toUri().buildUpon().scheme("https").build()
                    )

                    binding.donationDescriptionDetailTV.text = donation.description


                    // TODO implement like function
//                    binding.favoriteBTN.setOnClickListener()


                    binding.donateNowFAB.setOnClickListener() {
                        val navController = findNavController()
                        navController.navigate(DonationDetailFragmentDirections.actionDonationDetailFragmentToPaymentFragment(donation.title, donation.creator))
                    }
                }
            })
    }
}