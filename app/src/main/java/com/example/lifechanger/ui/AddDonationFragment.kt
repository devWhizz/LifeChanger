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

        // set toolbar title
        (activity as MainActivity).updateToolbarTitle(R.string.addDonation)

        // Deklariere eine Variable, um den ausgewählten RadioButton-Titel zu speichern
        var selectedCategoryTitle = ""

        // set ClickOnListener for RadioGroup
        binding.addDonationCategoryRB.setOnCheckedChangeListener { radioGroup, checkedId ->
            // Check which option is selected
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
            // check if option is selected
            if (selectedCategoryTitle.isNotEmpty()) {
                val id = viewmodel.getTableCount().toLong() + 1
                val category = selectedCategoryTitle
                val title = binding.addDonationTitleTI.text.toString()
                val description = binding.addDonationDescriptionTI.text.toString()
                val creator = binding.addDonationCreatorTI.text.toString()
                val image = binding.addDonationImageTI.text.toString()
                val payment = binding.addAccountInfo.text.toString()

                // insert data into database
                viewmodel.insertToDb(
                    Donation(
                        id,
                        category,
                        title,
                        description,
                        creator,
                        image,
                        payment
                    )
                )

                // navigate to category fragment when donation was added
                navigateToCategoryFragment(category)

                // create and setup dialog
                val alertDialog = AlertDialog.Builder(requireContext())
                alertDialog.setTitle("Gratulation")
                alertDialog.setMessage("Ihre Spendenaktion wurde erfolgreich erstellt!")
                alertDialog.setPositiveButton("OK") { dialog, which ->
                    dialog.dismiss()
                }
                alertDialog.show()

            } else {
                // show Toast if no category was selected
                Toast.makeText(
                    requireContext(),
                    "Bitte wähle eine Kategorie aus",
                    Toast.LENGTH_SHORT
                ).show()
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
}