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
import com.example.lifechanger.adapter.HomeAdapter
import com.example.lifechanger.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private val viewmodel: SharedViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set toolbar title
        (activity as MainActivity).updateToolbarTitle(R.string.home)

        // observe livedata to update changes in recyclerview
        viewmodel.environmentCategory.observe(viewLifecycleOwner) { environmentCategory ->
            binding.environmentRV.adapter = HomeAdapter(environmentCategory)
        }

        // observe livedata to update changes in recyclerview
        viewmodel.animalCategory.observe(viewLifecycleOwner) { animalCategory ->
            binding.animalRV.adapter = HomeAdapter(animalCategory)
        }
        // observe livedata to update changes in recyclerview
        viewmodel.peopleCategory.observe(viewLifecycleOwner) { peopleCategory ->
            binding.peopleRV.adapter = HomeAdapter(peopleCategory)
        }
        //  observe livedata to update changes in recyclerview
        viewmodel.developmentAidCategory.observe(viewLifecycleOwner) { developmentAidCategory ->
            binding.developmentAidRV.adapter = HomeAdapter(developmentAidCategory)
        }

        // setup of FAB to navigate to AddDonationFragment
        binding.addDonationFAB.setOnClickListener() {
            val navController = findNavController()
            navController.navigate(HomeFragmentDirections.actionHomeFragmentToAddDonationFragment())
        }
    }
}