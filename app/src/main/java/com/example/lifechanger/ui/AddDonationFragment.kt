package com.example.lifechanger.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lifechanger.MainActivity
import com.example.lifechanger.R
import com.example.lifechanger.SharedViewModel
import com.example.lifechanger.data.model.Donation
import com.example.lifechanger.databinding.FragmentAddDonationBinding

class AddDonationFragment : Fragment() {

    private lateinit var binding: FragmentAddDonationBinding
    private val viewmodel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddDonationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set toolbar title
        (activity as MainActivity).updateToolbarTitle(R.string.addDonation)

        binding.addBTN.setOnClickListener {

            val id = viewmodel.getTableCount()

            // Extract value from inputfield and convert to Int
            val categoryText = binding.addDonationCategory.text.toString()
            val titleText = binding.addDonationTitle.text.toString()
            val descriptionText = binding.addDonationDescription.text.toString()
            val companyText = binding.addDonationCreator.text.toString()
            val imageUrl = binding.addDonationImage.text.toString()

            // Add created element to database
            viewmodel.insertToDb(
                Donation(
                    id,
                    categoryText,
                    titleText,
                    descriptionText,
                    companyText,
                    imageUrl
                )
            )

            // Navigate back (when fact added)
            findNavController().navigateUp()
        }

        // Set OnClickListener do navigate back (when canceled)
        binding.cancelBTN.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
