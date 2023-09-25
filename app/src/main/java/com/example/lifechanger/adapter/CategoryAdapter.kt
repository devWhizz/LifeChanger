package com.example.lifechanger.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.lifechanger.SharedViewModel
import com.example.lifechanger.data.model.Donation
import com.example.lifechanger.databinding.DonationdetailItemBinding

class CategoryAdapter(

    //  create val of type List to save data objects and show them in RecyclerView
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

        //  settings to implement objects in ViewHolder
        holder.binding.detailTitleTV.text = item.title
        holder.binding.detailCompanyTV.text = item.company

        //use Coil to load images
        holder.binding.detailImageIV.load(item.image.toUri().buildUpon().scheme("https").build()) {
        }
    }

    //  return size of elements in dataset (list elements)
    override fun getItemCount(): Int {
        return dataset.size
    }
}