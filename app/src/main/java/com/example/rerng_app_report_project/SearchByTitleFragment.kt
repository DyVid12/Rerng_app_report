package com.example.rerng_app_report_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rerng_app_report_project.Data_rerngApp.State
import com.example.rerng_app_report_project.ViewModels.SearchViewModels
import com.example.rerng_app_report_project.adapter.SearchAdapter
import com.example.rerng_app_report_project.databinding.FragmentSearchByTitleBinding

class SearchByTitleFragment : Fragment() {

    private lateinit var binding: FragmentSearchByTitleBinding
    private lateinit var searchViewModel: SearchViewModels
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchByTitleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel = ViewModelProvider(this).get(SearchViewModels::class.java)

        // Set up RecyclerView
        searchAdapter = SearchAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = searchAdapter

        // Observe search results
        searchViewModel.dataState.observe(viewLifecycleOwner) { state ->
            when (state.state) {
                State.SUCCESS -> {
                    val movies = state.movies
                    searchAdapter.submitList(movies)
                }
                State.ERROR -> {
                    Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Handle search input
        binding.searchInput.addTextChangedListener { editable ->
            val query = editable.toString().trim()
            if (query.isNotEmpty()) {
                searchViewModel.loadSearchDataByTitle(query)
            } else {
                searchAdapter.submitList(emptyList()) // Clear results when empty
            }
        }
    }
}
