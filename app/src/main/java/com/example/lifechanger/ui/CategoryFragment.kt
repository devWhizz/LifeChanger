package com.example.lifechanger.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.lifechanger.MainActivity
import com.example.lifechanger.SharedViewModel
import com.example.lifechanger.adapter.CategoryAdapter
import com.example.lifechanger.addOnItemClickListener
import com.example.lifechanger.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment() {

    // assign binding object to layout
    private lateinit var binding: FragmentCategoryBinding

    // assign viewmodel to SharedViewModel
    private val viewmodel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get selected category parameter
        val selectedCategory = arguments?.getString("selectedCategory")

        // get toolbar title
        (activity as MainActivity).updateToolbarTitleDetail(selectedCategory.toString())

        // use selected category parameter to display correct category in recyclerview
        val categoryAdapter = CategoryAdapter(emptyList(), viewmodel)
        binding.categoryRV.adapter = categoryAdapter

        // observe livedata
        viewmodel.donations.observe(viewLifecycleOwner) { donations ->
            val categoryDonation = donations.filter { it.category == selectedCategory }
            categoryAdapter.dataset = categoryDonation
            categoryAdapter.notifyDataSetChanged()
        }

        binding.categoryRV.addOnItemClickListener {
            // get donation ID
            val donationId =
                (binding.categoryRV.adapter as CategoryAdapter).getDonationIdAtPosition(it)
            val navController = findNavController()
            // navigate to DonationDetailFragment passing relevant information
            val action = CategoryFragmentDirections.actionCategoryFragmentToDonationDetailFragment(
                donationId = donationId
            )
            navController.navigate(action)
        }

        binding.addFAB.setOnClickListener() {
            val navController = findNavController()
            navController.navigate(CategoryFragmentDirections.actionCategoryFragmentToAddDonationFragment())
        }
    }
}