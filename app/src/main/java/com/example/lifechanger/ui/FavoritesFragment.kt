package com.example.lifechanger.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.lifechanger.MainActivity
import com.example.lifechanger.R
import com.example.lifechanger.SharedViewModel
import com.example.lifechanger.adapter.CategoryAdapter
import com.example.lifechanger.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding

    // assign viewmodel to SharedViewModel
    private val viewmodel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set toolbar title
        (activity as MainActivity).updateToolbarTitle(R.string.favorites)

        val categoryAdapter = CategoryAdapter(emptyList(), viewmodel)
        binding.favoritesRV.adapter = categoryAdapter

        // get IDs of liked donations from SharedPreferences
        val likedDonationIds = viewmodel.getLikedDonationIds()

        // filter donations based on liked IDs and update RecyclerView (FavoritesFragment)
        val likedDonations = viewmodel.getDonationsByIds(likedDonationIds)
        categoryAdapter.updateData(likedDonations)

        // observe livedata of liked donations updates
        viewmodel.likedDonationsUpdated.observe(viewLifecycleOwner) {
            // Get the updated liked donations and update the RecyclerView
            val likedDonationIds = viewmodel.getLikedDonationIds()
            val likedDonations = viewmodel.getDonationsByIds(likedDonationIds)
            categoryAdapter.updateData(likedDonations)
        }

        // TODO implement ClickListener for RV items
//        binding.favoritesRV.addOnItemClickListener {}

    }
}