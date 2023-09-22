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
import com.example.lifechanger.adapter.WaterAdapter
import com.example.lifechanger.databinding.FragmentWaterBinding

class WaterFragment : Fragment() {

    // assign binding object to layout
    private lateinit var binding: FragmentWaterBinding

    // assign viewmodel to SharedViewModel
    private val viewmodel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWaterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set toolbar title
        (activity as MainActivity).updateToolbarTitle(R.string.categoryTitleWater)

        // assign category
        val category = "Gewässer"

        // assign adapter to layout
        val waterdapter = WaterAdapter(emptyList(), viewmodel)
        binding.waterRV.adapter = waterdapter

        // observe livedata to update changes in recyclerview
        viewmodel.donation.observe(viewLifecycleOwner) { donations ->
            // filter donations by category
            val waterDonation = donations.filter { it.category == category }
            waterdapter.dataset = waterDonation
            waterdapter.notifyDataSetChanged()
        }

        // setup of FAB to navigate to AddDonationFragment
        binding.addBTN.setOnClickListener() {
            val navController = findNavController()
            navController.navigate(WaterFragmentDirections.actionWaterFragmentToAddDonationFragment())
        }
    }
}
