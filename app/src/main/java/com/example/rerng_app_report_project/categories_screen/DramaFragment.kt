package com.example.rerng_app_report_project.categories_screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rerng_app_report_project.Data_rerngApp.State
import com.example.rerng_app_report_project.ViewModels.DramaModels
import com.example.rerng_app_report_project.adapter.DramaAdapter
import com.example.rerng_app_report_project.databinding.FragmentDramaBinding

class DramaFragment : Fragment() {
    private lateinit var binding: FragmentDramaBinding
    private lateinit var dramaAdapter: DramaAdapter
    private val viewModel by viewModels<DramaModels>()  // Fix ViewModel reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDramaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dramaAdapter = DramaAdapter()

        binding.dramarecycleAll.apply {  // Ensure this ID exists in XML
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            layoutManager = GridLayoutManager(context, 2)
            adapter =   dramaAdapter
        }

        viewModel.dataState.observe(viewLifecycleOwner) { dataState ->
            when (dataState.state) {
                State.SUCCESS -> {
                    val movies = dataState.movies
                    Log.d("DramaFragment", "✅ Movie List Received: $movies")

                    if (!movies.isNullOrEmpty()) {
                        dramaAdapter.submitList(movies)
                    } else {
                        Log.e("DramaFragment", "❌ API returned empty list!")
                        Toast.makeText(context, "No movies available", Toast.LENGTH_LONG).show()
                    }
                }
                State.ERROR -> {
                    Log.e("DramaFragment", "❌ API Error occurred.")
                    Toast.makeText(context, "Error loading data. Please try again", Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.loadDramaData()  // Ensure this method exists in AdventureModels
    }
}
