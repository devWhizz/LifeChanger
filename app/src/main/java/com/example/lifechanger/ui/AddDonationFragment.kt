package com.example.lifechanger.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
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

        // declare variable to store selected Spinner title
        var selectedCategoryTitle = ""

        // initialize Spinner
        val categorySpinner = view.findViewById<Spinner>(R.id.addDonationCategorySpinner)

        // set ClickOnListener for Spinner
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                selectedCategoryTitle = parentView?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.addBTN.setOnClickListener {

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

                        // navigate back to CategoryFragment showing appropriate content
                        navigateToCategoryFragment(selectedCategoryTitle)

                        // display success message
                        showSuccessDialog()
                    }
                    .addOnFailureListener { e ->
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