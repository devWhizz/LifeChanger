package com.example.lifechanger.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lifechanger.MainActivity
import com.example.lifechanger.R
import com.example.lifechanger.SharedViewModel
import com.example.lifechanger.data.model.Donation
import com.example.lifechanger.databinding.FragmentAddDonationBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class AddDonationFragment : Fragment() {

    private lateinit var binding: FragmentAddDonationBinding
    private val viewmodel: SharedViewModel by activityViewModels()

    val firestore = FirebaseFirestore.getInstance()

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

        // set toolbar title
        (activity as MainActivity).updateToolbarTitle(R.string.addDonation)

        // declare variable to store selected RadioButton title
        var selectedCategoryTitle = ""

        // set ClickOnListener for RadioGroup
        binding.addDonationCategoryRB.setOnCheckedChangeListener { radioGroup, checkedId ->
            // check which option is selected
            when (checkedId) {
                R.id.radioClimate -> selectedCategoryTitle =
                    getString(R.string.categoryTitleClimate)

                R.id.radioWater -> selectedCategoryTitle = getString(R.string.categoryTitleWater)
                R.id.radioGarbage -> selectedCategoryTitle =
                    getString(R.string.categoryTitleGarbage)

                R.id.radioDisaster -> selectedCategoryTitle =
                    getString(R.string.categoryTitleDisaster)

                R.id.radioAnimalRights -> selectedCategoryTitle =
                    getString(R.string.categoryTitleAnimalRights)

                R.id.radioSpeciesProtection -> selectedCategoryTitle =
                    getString(R.string.categoryTitleSpeciesProtection)

                R.id.radioCats -> selectedCategoryTitle = getString(R.string.categoryTitleCats)
                R.id.radioDogs -> selectedCategoryTitle = getString(R.string.categoryTitleDogs)
                R.id.radioHorses -> selectedCategoryTitle = getString(R.string.categoryTitleHorses)
                R.id.radioKids -> selectedCategoryTitle = getString(R.string.categoryTitleKids)
                R.id.radioSeniors -> selectedCategoryTitle =
                    getString(R.string.categoryTitleSeniors)

                R.id.radioHomeless -> selectedCategoryTitle =
                    getString(R.string.categoryTitleHomeless)

                R.id.radioRefugees -> selectedCategoryTitle =
                    getString(R.string.categoryTitleRefugees)

                R.id.radioInclusion -> selectedCategoryTitle =
                    getString(R.string.categoryTitleInclusion)

                R.id.radioEducation -> selectedCategoryTitle =
                    getString(R.string.categoryTitleEducation)

                R.id.radioInfrastructure -> selectedCategoryTitle =
                    getString(R.string.categoryTitleInfrastructure)
            }
        }

        binding.addBTN.setOnClickListener {
            val selectedCategoryTitle = when {
                binding.radioClimate.isChecked -> getString(R.string.categoryTitleClimate)
                binding.radioWater.isChecked -> getString(R.string.categoryTitleWater)
                binding.radioGarbage.isChecked -> getString(R.string.categoryTitleGarbage)
                binding.radioDisaster.isChecked -> getString(R.string.categoryTitleDisaster)
                binding.radioAnimalRights.isChecked -> getString(R.string.categoryTitleAnimalRights)
                binding.radioSpeciesProtection.isChecked -> getString(R.string.categoryTitleSpeciesProtection)
                binding.radioCats.isChecked -> getString(R.string.categoryTitleCats)
                binding.radioDogs.isChecked -> getString(R.string.categoryTitleDogs)
                binding.radioHorses.isChecked -> getString(R.string.categoryTitleHorses)
                binding.radioKids.isChecked -> getString(R.string.categoryTitleKids)
                binding.radioSeniors.isChecked -> getString(R.string.categoryTitleSeniors)
                binding.radioHomeless.isChecked -> getString(R.string.categoryTitleHomeless)
                binding.radioRefugees.isChecked -> getString(R.string.categoryTitleRefugees)
                binding.radioInclusion.isChecked -> getString(R.string.categoryTitleInclusion)
                binding.radioEducation.isChecked -> getString(R.string.categoryTitleEducation)
                binding.radioInfrastructure.isChecked -> getString(R.string.categoryTitleInfrastructure)
                else -> ""
            }
            // verify that all required fields are filled in
            if (selectedCategoryTitle.isNotEmpty() &&
                binding.addDonationTitleTI.text!!.isNotBlank() &&
                binding.addDonationDescriptionTI.text!!.isNotBlank() &&
                binding.addDonationCreatorTI.text!!.isNotBlank() &&
                binding.addDonationImageTI.text!!.isNotBlank() &&
                binding.addAccountInfo.text!!.isNotBlank()
            ) {
                // create new donation object
                val donation = Donation(
                    id = UUID.randomUUID().toString(),
                    category = selectedCategoryTitle,
                    title = binding.addDonationTitleTI.text.toString(),
                    description = binding.addDonationDescriptionTI.text.toString(),
                    creator = binding.addDonationCreatorTI.text.toString(),
                    image = binding.addDonationImageTI.text.toString(),
                    payment = binding.addAccountInfo.text.toString()
                )

                // add donation to Firebase Firestore
                firestore.collection("donations")
                    .add(donation)
                    .addOnSuccessListener { documentReference ->
                        val donationId = documentReference.id
                        // navigate back to Categoryfragment showing appropriate content
                        navigateToCategoryFragment(selectedCategoryTitle)

                        // display success message
                        showSuccessDialog()
                    }
                    .addOnFailureListener {
                        // display error message if not all required fields are filled in
                        showErrorToast()
                    }
            } else {
                // display error message if not all required fields are filled in
                showErrorToast()
            }
        }

        // set OnClickListener do navigate back (when canceled)
        binding.cancelBTN.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    // function showing appropriate recyclerview
    private fun navigateToCategoryFragment(category: String) {
        val navController = findNavController()
        val action =
            AddDonationFragmentDirections.actionAddDonationFragmentToCategoryFragment(category)
        navController.navigate(action)
    }

    private fun showSuccessDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Gratulation")
        alertDialog.setMessage("Ihre Spendenaktion wurde erfolgreich erstellt!")
        alertDialog.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    private fun showErrorToast() {
        Toast.makeText(
            requireContext(),
            "Bitte füllen Sie alle Felder aus!",
            Toast.LENGTH_SHORT
        ).show()
    }
}