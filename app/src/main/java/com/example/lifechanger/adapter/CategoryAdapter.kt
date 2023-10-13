package com.example.lifechanger.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.lifechanger.R
import com.example.lifechanger.SharedViewModel
import com.example.lifechanger.data.model.Donation
import com.example.lifechanger.databinding.DonationdetailItemBinding

class CategoryAdapter(

    var dataset: List<Donation>,
    val viewmodel: SharedViewModel,
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val binding: DonationdetailItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            DonationdetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        // create val to work with expression
        val item = dataset[position]

        // settings to implement objects in ViewHolder
        holder.binding.detailTitleTV.text = item.title
        holder.binding.detailCompanyTV.text = item.creator

        // use Coil to load images
        holder.binding.detailImageIV.load(item.image.toUri().buildUpon().scheme("https").build())

        // set like status
        if (viewmodel.isLiked(item.id)) {
            // item is liked
            holder.binding.likeBTN.setImageResource(R.drawable.favorite_icon)
        } else {
            // item isn't liked
            holder.binding.likeBTN.setImageResource(R.drawable.favorite_blank_icon)
        }

        // set clicklistener on like button
        holder.binding.likeBTN.setOnClickListener {
            // change status
            item.isLiked = !item.isLiked

            // update symbol based on new status
            if (item.isLiked) {
                holder.binding.likeBTN.setImageResource(R.drawable.favorite_icon)
                viewmodel.addToLikedDonations(item)
            } else {
                holder.binding.likeBTN.setImageResource(R.drawable.favorite_blank_icon)
                viewmodel.removeFromLikedDonations(item)
            }
        }
    }

    // update data in adapter for liked items
    fun updateData(newData: List<Donation>) {
        dataset = newData
        notifyDataSetChanged()
    }

    // get ID of donation based on its position in recyclerview
    fun getDonationIdAtPosition(position: Int): String {
        if (position in dataset.indices) {
            return dataset[position].id
        }
        return ""
    }

    //  return size of elements in dataset (list elements)
    override fun getItemCount(): Int {
        return dataset.size
    }
}