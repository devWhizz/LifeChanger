package com.example.lifechanger.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.lifechanger.SharedViewModel
import com.example.lifechanger.data.model.Donation
import com.example.lifechanger.databinding.DonationdetailItemBinding
import com.example.lifechanger.ui.WaterFragmentDirections

class WaterAdapter (

    //  create val of type List to save data objects and show them in RecyclerView
    var dataset: List<Donation>,
    val viewmodel: SharedViewModel,
) : RecyclerView.Adapter<WaterAdapter.WaterViewHolder>() {

    inner class WaterViewHolder(val binding: DonationdetailItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaterViewHolder {
        val binding =
            DonationdetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WaterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WaterViewHolder, position: Int) {

        //  create val to work with expression
        val item = dataset[position]

        //  settings to implement objects in ViewHolder
        holder.binding.titleTV.text = item.title
        holder.binding.companyTV.text = item.company

        //Use Coil to load images
        holder.binding.waterIV.load(item.image.toUri().buildUpon().scheme("https").build()) {
        }

        // navigate to detailed view of donation from water category, passing on relevant information
        holder.binding.waterCV.setOnClickListener {
            holder.binding.root.findNavController().navigate(WaterFragmentDirections.actionWaterFragmentToDonationDetailFragment(position, category = "Gewässer"))
        }
    }

    //  return size of elements in dataset (list elements)
    override fun getItemCount(): Int {
        return dataset.size
    }
}