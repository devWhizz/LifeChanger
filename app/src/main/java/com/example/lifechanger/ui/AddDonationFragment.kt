package com.example.lifechanger.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.lifechanger.MainActivity
import com.example.lifechanger.R
import com.example.lifechanger.data.model.Donation
import com.example.lifechanger.databinding.FragmentAddDonationBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class AddDonationFragment : Fragment() {

    private lateinit var binding: FragmentAddDonationBinding
    private lateinit var imagePicker: ActivityResultLauncher<Intent>
    private var selectedImageUri: Uri? = null

    private val firestore = FirebaseFirestore.getInstance()

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

        val selectImageBTN = view.findViewById<Button>(R.id.selectImageBTN)
        val selectedImageIV = view.findViewById<ImageView>(R.id.selectedImageIV)

        // set ImageView to be initially invisible
        selectedImageIV.visibility = View.INVISIBLE

        imagePicker =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    selectedImageUri = data?.data
                    if (selectedImageUri != null) {
                        selectedImageIV.setImageURI(selectedImageUri)
                        // set ImageView to be visible
                        selectedImageIV.visibility = View.VISIBLE
                    }
                }
            }

        selectImageBTN.setOnClickListener {
            // open device gallery
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagePicker.launch(intent)
        }

        binding.addFAB.setOnClickListener {
            // check if all fields were filled in
            if (selectedCategoryTitle.isNotEmpty() &&
                binding.addDonationTitleTI.text!!.isNotBlank() &&
                binding.addDonationDescriptionTI.text!!.isNotBlank() &&
                binding.addDonationCreatorTI.text!!.isNotBlank() &&
                selectedImageUri != null &&
                binding.addAmountTI.text!!.isNotBlank()
            ) {
                // show ProgressBar to indicate upload progress
                binding.uploadProgressBar.visibility = View.VISIBLE

                // upload selected image to Firebase Storage and retrieve download URL
                val storageRef = FirebaseStorage.getInstance().reference
                val imageRef = storageRef.child("images/${UUID.randomUUID()}")
                val uploadTask = imageRef.putFile(selectedImageUri!!)

                uploadTask.addOnSuccessListener { taskSnapshot ->
                    // provide download URL of uploaded image
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        val imageString = uri.toString()

                        // create new donation object
                        val donation = Donation(
                            id = UUID.randomUUID().toString(),
                            category = selectedCategoryTitle,
                            title = binding.addDonationTitleTI.text.toString(),
                            description = binding.addDonationDescriptionTI.text.toString(),
                            creator = binding.addDonationCreatorTI.text.toString(),
                            image = imageString,
                            payment = binding.addAmountTI.text.toString()
                        )

                        // add donation to Firestore
                        firestore.collection("donations")
                            .add(donation)
                            .addOnSuccessListener { documentReference ->
                                val donationId = documentReference.id

                                // navigate back to CategoryFragment showing appropriate content
                                navigateToCategoryFragment(selectedCategoryTitle)

                                // display success message
                                showSuccessDialog()
                            }
                            .addOnFailureListener {
                                // display error message if not all required fields are filled in
                                showErrorToast()

                                // hide ProgressBar on failure
                                binding.uploadProgressBar.visibility = View.INVISIBLE

                            }
                    }
                }.addOnFailureListener {
                    // display error message if not all required fields are filled in
                    showErrorToast()
                    // hide ProgressBar on failure
                    binding.uploadProgressBar.visibility = View.INVISIBLE
                }
            } else {
                // display error message if no image was uploaded
                showErrorToast()
            }
        }

        // set OnClickListener do navigate back (when canceled)
        binding.cancelAddingFAB.setOnClickListener {
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
        alertDialog.setMessage("Deine Spendenaktion wurde erfolgreich erstellt!")
        alertDialog.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    private fun showErrorToast() {
        Toast.makeText(
            requireContext(),
            "Bitte fülle alle Felder aus!",
            Toast.LENGTH_SHORT
        ).show()
    }


    override fun onResume() {
        super.onResume()
        // hide bottom menu on AddDonationFragment
        (activity as MainActivity?)?.findViewById<View>(R.id.bottomNav)?.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // show bottom menu when AddDonationFragment is destroyed
        (activity as MainActivity?)?.findViewById<View>(R.id.bottomNav)?.visibility = View.VISIBLE
    }

}