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

        // use selected category parameter to display correct category in RecyclerView
        val categoryAdapter = CategoryAdapter(emptyList(), viewmodel)
        binding.categoryRV.adapter = categoryAdapter

        // observe livedata
        viewmodel.donations.observe(viewLifecycleOwner) { donations ->
            val categoryDonation = donations.filter { it.category == selectedCategory }
            categoryAdapter.dataset = categoryDonation
            categoryAdapter.notifyDataSetChanged()
        }

        binding.categoryRV.addOnItemClickListener { position ->
            val selectedCategory = arguments?.getString("selectedCategory")
            val navController = findNavController()
            val action = CategoryFragmentDirections.actionCategoryFragmentToDonationDetailFragment(
                donationIndex = position,
                category = selectedCategory ?: ""
            )
            navController.navigate(action)
        }

        binding.addBTN.setOnClickListener() {
            val navController = findNavController()
            navController.navigate(CategoryFragmentDirections.actionCategoryFragmentToAddDonationFragment())
        }
    }
}

// function adding a click listener to a RecyclerView item
fun RecyclerView.addOnItemClickListener(onItemClickListener: (Int) -> Unit) {
    this.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
            view.setOnClickListener {
                val holder = getChildViewHolder(view)
                onItemClickListener(holder.adapterPosition)
            }
        }

        override fun onChildViewDetachedFromWindow(view: View) {
            view.setOnClickListener(null)
        }
    })
}