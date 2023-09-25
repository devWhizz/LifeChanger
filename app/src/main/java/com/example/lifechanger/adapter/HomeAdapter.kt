package com.example.lifechanger.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.lifechanger.R
import com.example.lifechanger.data.model.Category
import com.example.lifechanger.databinding.CategoryListItemBinding
import com.example.lifechanger.ui.HomeFragmentDirections

class HomeAdapter(
    private val dataset: List<Category>
) : RecyclerView.Adapter<HomeAdapter.ItemViewHolder>() {

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

        // set text and image for category
        holder.binding.categoryTitleTV.setText(item.title)
        holder.binding.categoryTitleIV.setImageResource(item.image)

        // add OnClickListener to CardView
        holder.binding.categoryCV.setOnClickListener {
            val action = when (item.title) {
                R.string.categoryTitleClimate -> HomeFragmentDirections.actionHomeFragmentToCategoryFragment(
                    "Klima"
                )

                R.string.categoryTitleWater -> HomeFragmentDirections.actionHomeFragmentToCategoryFragment(
                    "Gewässer"
                )

                R.string.categoryTitleGarbage -> HomeFragmentDirections.actionHomeFragmentToCategoryFragment(
                    "Müll"
                )

                R.string.categoryTitleDisaster -> HomeFragmentDirections.actionHomeFragmentToCategoryFragment(
                    "Katastrophen"
                )

                R.string.categoryTitleAnimalRights -> HomeFragmentDirections.actionHomeFragmentToCategoryFragment(
                    "Tierschutz"
                )

                R.string.categoryTitleSpeciesProtection -> HomeFragmentDirections.actionHomeFragmentToCategoryFragment(
                    "Artenschutz"
                )

                R.string.categoryTitleCats -> HomeFragmentDirections.actionHomeFragmentToCategoryFragment(
                    "Katzen"
                )

                R.string.categoryTitleDogs -> HomeFragmentDirections.actionHomeFragmentToCategoryFragment(
                    "Hunde"
                )

                R.string.categoryTitleHorses -> HomeFragmentDirections.actionHomeFragmentToCategoryFragment(
                    "Pferde"
                )

                R.string.categoryTitleKids -> HomeFragmentDirections.actionHomeFragmentToCategoryFragment(
                    "Kinder"
                )

                R.string.categoryTitleSeniors -> HomeFragmentDirections.actionHomeFragmentToCategoryFragment(
                    "Senioren"
                )

                R.string.categoryTitleHomeless -> HomeFragmentDirections.actionHomeFragmentToCategoryFragment(
                    "Obdachlose"
                )

                R.string.categoryTitleRefugees -> HomeFragmentDirections.actionHomeFragmentToCategoryFragment(
                    "Flüchtlinge"
                )

                R.string.categoryTitleInclusion -> HomeFragmentDirections.actionHomeFragmentToCategoryFragment(
                    "Inklusion"
                )

                R.string.categoryTitleEducation -> HomeFragmentDirections.actionHomeFragmentToCategoryFragment(
                    "Bildung"
                )

                R.string.categoryTitleInfrastructure -> HomeFragmentDirections.actionHomeFragmentToCategoryFragment(
                    "Infrastuktur"
                )

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