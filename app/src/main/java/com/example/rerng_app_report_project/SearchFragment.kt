package com.example.rerng_app_report_project

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rerng_app_report_project.Data_rerngApp.State
import com.example.rerng_app_report_project.ViewModels.SearchViewModels
import com.example.rerng_app_report_project.adapter.SearchAdapter
import com.example.rerng_app_report_project.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: SearchAdapter
    private val viewModel by viewModels<SearchViewModels>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchAdapter = SearchAdapter()

        binding.recyclerSearch.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = searchAdapter
        }

        setUpUi()
        setUpListeners()
    }

    private fun setUpUi() {
        // Observe search results
        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            when (state.state) {
                State.SUCCESS -> {
                    state.movies?.let { data ->
                        if (data.isNotEmpty()) {
                            binding.textViewNoResults.visibility = View.GONE
                            searchAdapter.submitList(data) // Update RecyclerView
                        } else {
                            binding.textViewNoResults.visibility = View.VISIBLE
                            searchAdapter.submitList(emptyList()) // Show no results message
                        }
                    }
                }
                State.ERROR -> {
                    Log.e("SearchFragment", "Error fetching data")
                }
            }
        }
    }

    private fun setUpListeners() {
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                performSearch()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


    }


    private fun performSearch() {
        val query = binding.editTextSearch.text.toString().trim()

        if (query.isNotEmpty()) {
            Log.d("SearchFragment", "Searching: $query")

            // Remove previous observers to prevent duplication
            viewModel.dataState.removeObservers(viewLifecycleOwner)

            // First, search by category
            viewModel.loadSearchData(query)
            viewModel.dataState.observe(viewLifecycleOwner) { state ->
                when (state.state) {
                    State.SUCCESS -> {
                        if (!state.movies.isNullOrEmpty()) {
                            Log.d("SearchFragment", "Category search successful, displaying results.")
                            searchAdapter.submitList(state.movies) // Show category results
                        } else {
                            Log.d("SearchFragment", "No results in category, searching by title...")

                            // Remove observer before starting title search
                            viewModel.dataState.removeObservers(viewLifecycleOwner)

                            // Now, search by title
                            viewModel.loadSearchDataByTitle(query)
                            viewModel.dataState.observe(viewLifecycleOwner) { titleState ->
                                when (titleState.state) {
                                    State.SUCCESS -> {
                                        Log.d("SearchFragment", "Title search successful, displaying results.")
                                        searchAdapter.submitList(titleState.movies) // Show title-based results
                                    }
                                    State.ERROR -> {
                                        Log.e("SearchFragment", "Error searching by title: ${titleState.errorMessage}")
                                        searchAdapter.submitList(emptyList()) // No results found in both searches
                                    }
                                }
                            }
                        }
                    }
                    State.ERROR -> {
                        Log.e("SearchFragment", "Error searching category: ${state.errorMessage}")

                        // Remove observer before starting title search
                        viewModel.dataState.removeObservers(viewLifecycleOwner)

                        // If category search fails, try searching by title immediately
                        viewModel.loadSearchDataByTitle(query)
                        viewModel.dataState.observe(viewLifecycleOwner) { titleState ->
                            when (titleState.state) {
                                State.SUCCESS -> {
                                    Log.d("SearchFragment", "Title search successful, displaying results.")
                                    searchAdapter.submitList(titleState.movies) // Show title-based results
                                }
                                State.ERROR -> {
                                    Log.e("SearchFragment", "Error searching by title: ${titleState.errorMessage}")
                                    searchAdapter.submitList(emptyList()) // No results found
                                }
                            }
                        }
                    }
                }
            }
        } else {
            searchAdapter.submitList(emptyList()) // Clear results if query is empty
        }
    }





}
