package com.example.lifechanger.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lifechanger.MainActivity
import com.example.lifechanger.R
import com.example.lifechanger.SharedViewModel
import com.example.lifechanger.adapter.CategoryAdapter
import com.example.lifechanger.addOnItemClickListener
import com.example.lifechanger.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private val viewmodel: SharedViewModel by activityViewModels()
    private lateinit var binding: FragmentSearchBinding
    private lateinit var autoCompleteTextView: AutoCompleteTextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set toolbar title
        (activity as MainActivity).updateToolbarTitle(R.string.search)

        val categoryAdapter = CategoryAdapter(emptyList(), viewmodel)
        binding.searchRV.adapter = categoryAdapter

        // check if previous user input exists, set this input and show search results
        if (viewmodel.lastSearchQuery.isNotEmpty()) {
            binding.searchET.setText(viewmodel.lastSearchQuery)
            viewmodel.searchDonations(viewmodel.lastSearchQuery)
                .observe(viewLifecycleOwner) { searchResults ->
                    categoryAdapter.dataset = searchResults
                    categoryAdapter.notifyDataSetChanged()
                }
        }

        // initialize AutoCompleteTextView with search suggestions
        autoCompleteTextView = binding.searchET
        val searchSuggestions: Array<String> = if (viewmodel.getTargetLanguage() == "en") {
            arrayOf(
                "Climate",
                "Water",
                "Garbage",
                "Disasters",
                "Animal Welfare",
                "Species Protection",
                "Cats",
                "Dogs",
                "Horses",
                "Children",
                "Seniors",
                "Homeless",
                "Refugees",
                "Inclusion",
                "Education",
                "Infrastructure"
            )
        } else {
            arrayOf(
                "Klima",
                "Gewässer",
                "Müll",
                "Katastrophen",
                "Tierschutz",
                "Artenschutz",
                "Katzen",
                "Hunde",
                "Pferde",
                "Kinder",
                "Senioren",
                "Obdachlose",
                "Flüchtlinge",
                "Inklusion",
                "Bildung",
                "Infrastruktur"
            )
        }

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            searchSuggestions
        )
        autoCompleteTextView.setAdapter(adapter)

        // show suggestions after typing 1 character
        autoCompleteTextView.threshold = 1

        autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            val selectedSuggestion = parent.getItemAtPosition(position) as String
            autoCompleteTextView.setText(selectedSuggestion)
        }

        binding.searchET.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.isEmpty()) {
                    categoryAdapter.dataset = emptyList()
                    categoryAdapter.notifyDataSetChanged()
                    binding.noResultsTV.visibility = View.GONE
                } else {
                    // get language status
                    val targetLang = viewmodel.getTargetLanguage()

                    if (targetLang == "en") {
                        // translate user's query from English to German
                        viewmodel.translateText(query, "de")
                            .observe(viewLifecycleOwner) { translatedQuery ->
                                // search for donations with translated query
                                viewmodel.searchDonations(translatedQuery)
                                    .observe(viewLifecycleOwner) { searchResults ->
                                        if (searchResults.isEmpty()) {
                                            binding.noResultsTV.visibility = View.VISIBLE
                                        } else {
                                            binding.noResultsTV.visibility = View.GONE
                                        }
                                        categoryAdapter.dataset = searchResults
                                        categoryAdapter.notifyDataSetChanged()
                                    }
                            }
                    } else {
                        // search for donations using the user's query (German)
                        viewmodel.searchDonations(query)
                            .observe(viewLifecycleOwner) { searchResults ->
                                if (searchResults.isEmpty()) {
                                    binding.noResultsTV.visibility = View.VISIBLE
                                } else {
                                    binding.noResultsTV.visibility = View.GONE
                                }
                                categoryAdapter.dataset = searchResults
                                categoryAdapter.notifyDataSetChanged()
                            }
                    }
                }
                viewmodel.lastSearchQuery = query
            }
        })

        binding.searchRV.addOnItemClickListener {
            // get donation ID
            val donationId =
                (binding.searchRV.adapter as CategoryAdapter).getDonationIdAtPosition(it)
            val navController = findNavController()
            // navigate to fragment passing relevant information
            val action =
                SearchFragmentDirections.actionSearchFragmentToDonationDetailFragment(
                    donationId = donationId
                )
            navController.navigate(action)
        }
    }
}