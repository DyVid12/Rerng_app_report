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
import com.example.rerng_app_report_project.ViewModels.WarModels
import com.example.rerng_app_report_project.adapter.WarAdapter
import com.example.rerng_app_report_project.databinding.FragmentWarBinding

class WarFragment : Fragment() {
    private lateinit var binding: FragmentWarBinding
    private lateinit var warAdapter: WarAdapter
    private val viewModel by viewModels<WarModels>()  // Fix ViewModel reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =FragmentWarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        warAdapter = WarAdapter()

        binding.warrecycleAll.apply{  // Ensure this ID exists in XML
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            layoutManager = GridLayoutManager(context, 2)
            adapter = warAdapter
        }

        viewModel.dataState.observe(viewLifecycleOwner) { dataState ->
            when (dataState.state) {
                State.SUCCESS -> {
                    val movies = dataState.movies
                    Log.d("WarFragment", "✅ Movie List Received: $movies")

                    if (!movies.isNullOrEmpty()) {
                        warAdapter.submitList(movies)
                    } else {
                        Log.e("WarFragment", "❌ API returned empty list!")
                        Toast.makeText(context, "No movies available", Toast.LENGTH_LONG).show()
                    }
                }
                State.ERROR -> {
                    Log.e("WarFragment", "❌ API Error occurred.")
                    Toast.makeText(context, "Error loading data. Please try again", Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.loadWarData()  // Ensure this method exists in AdventureModels
    }
}
