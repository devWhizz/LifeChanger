package com.example.lifechanger.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.lifechanger.MainActivity
import com.example.lifechanger.R
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

        val donationId = args.donationId

        // select correct donation based on the category and index passed to display details in fragment
        viewmodel.getDonationById(donationId)
            .observe(viewLifecycleOwner) { donation ->
                if (donation != null) {
                    // get language status
                    val targetLang = viewmodel.getTargetLanguage()
                    Log.d("Translation", "Target language is: $targetLang")

                    if (targetLang == "en") {
                        // settings to translate category title, donation title and donation description with deepL API
                        viewmodel.translateText(donation.title, targetLang)
                            .observe(viewLifecycleOwner) { translatedCategory ->
                                (activity as MainActivity).updateToolbarTitleDetail(
                                    translatedCategory
                                )
                            }
                        viewmodel.translateDonationTitle(donation, targetLang)
                            .observe((view.context as LifecycleOwner)) { translatedTitle ->
                                binding.donationTitleDetailTV.text = translatedTitle
                            }
// ### NOT IN USE DUE TO CHARACTER SAVING ###
//                        viewmodel.translateDonationDescription(donation, targetLang)
//                            .observe((view.context as LifecycleOwner)) { translatedDescription ->
//                                binding.donationDescriptionDetailTV.text = translatedDescription
//                            }
                    } else {
                        (activity as MainActivity).updateToolbarTitleDetail(donation.title)
                        binding.donationTitleDetailTV.text = donation.title
//                        binding.donationDescriptionDetailTV.text = donation.description
                    }

                    binding.donationCompanyDetailTV.text = donation.creator
                    binding.donationDescriptionDetailTV.text = donation.description

                    // use Coil to load images
                    binding.donationImageDetailIV.load(
                        donation.image.toUri().buildUpon().scheme("https").build()
                    ) {
                        // show ProgressBar while image is loading
                        binding.loadingProgressBar.visibility = View.VISIBLE

                        listener(
                            onSuccess = { _, _ ->
                                // hide ProgressBar when image is loaded successfully
                                binding.loadingProgressBar.visibility = View.INVISIBLE
                            }
                        )
                    }

                    // set like status
                    if (viewmodel.isLiked(donation.id)) {
                        // item is liked
                        binding.favoriteBTN.setImageResource(R.drawable.favorite_icon)
                    } else {
                        // item isn't liked
                        binding.favoriteBTN.setImageResource(R.drawable.favorite_blank_icon)
                    }

                    // set clicklistener on like button
                    binding.favoriteBTN.setOnClickListener {
                        // change status
                        donation.isLiked = !donation.isLiked

                        // update symbol based on new status
                        if (donation.isLiked) {
                            binding.favoriteBTN.setImageResource(R.drawable.favorite_icon)
                            viewmodel.addToLikedDonations(donation)
                        } else {
                            binding.favoriteBTN.setImageResource(R.drawable.favorite_blank_icon)
                            viewmodel.removeFromLikedDonations(donation)
                        }
                    }

                    binding.donateNowFAB.setOnClickListener {
                        val navController = findNavController()
                        navController.navigate(
                            DonationDetailFragmentDirections.actionDonationDetailFragmentToPaymentFragment(
                                donation.title,
                                donation.creator,
                                donation.id
                            )
                        )
                    }
                }
            }
    }
}