package com.example.lifechanger.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.lifechanger.R
import com.example.lifechanger.data.model.Category
import com.example.lifechanger.databinding.CategoryListItemBinding
import com.example.lifechanger.ui.HomeFragmentDirections

class CategoryAdapter(
    private val dataset: List<Category>
) : RecyclerView.Adapter<CategoryAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: CategoryListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            CategoryListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        //  create val to work with expression
        val item = dataset[position]


        // Set text and image for category
        holder.binding.categoryTitleTV.setText(item.title)
        holder.binding.categoryTitleIV.setImageResource(item.image)

        // Add OnClickListener to CardView
        holder.binding.categoryCV.setOnClickListener {
            val action = when (item.title) {
                R.string.categoryTitleWater -> HomeFragmentDirections.actionHomeFragmentToWaterFragment()
                else -> null
            }

            action?.let {
                holder.binding.root.findNavController().navigate(it)
            }
        }
    }

    //  return size of elements in dataset (list elements)
    override fun getItemCount(): Int {
        return dataset.size
    }
}