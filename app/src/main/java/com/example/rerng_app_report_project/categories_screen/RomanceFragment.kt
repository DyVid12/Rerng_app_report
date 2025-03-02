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
import com.example.rerng_app_report_project.ViewModels.RomanceModels
import com.example.rerng_app_report_project.adapter.RomanceAdapter
import com.example.rerng_app_report_project.databinding.FragmentRomanceBinding

class RomanceFragment : Fragment() {
    private lateinit var binding: FragmentRomanceBinding
    private lateinit var romanceAdapter: RomanceAdapter
    private val viewModel by viewModels<RomanceModels>()  // Fix ViewModel reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRomanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        romanceAdapter= RomanceAdapter()

        binding.romancerecycleAll.apply{  // Ensure this ID exists in XML
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            layoutManager = GridLayoutManager(context, 2)
            adapter = romanceAdapter
        }

        viewModel.dataState.observe(viewLifecycleOwner) { dataState ->
            when (dataState.state) {
                State.SUCCESS -> {
                    val movies = dataState.movies
                    Log.d("RomanceFragment", "✅ Movie List Received: $movies")

                    if (!movies.isNullOrEmpty()) {
                        romanceAdapter.submitList(movies)
                    } else {
                        Log.e("RomanceFragment", "❌ API returned empty list!")
                        Toast.makeText(context, "No movies available", Toast.LENGTH_LONG).show()
                    }
                }
                State.ERROR -> {
                    Log.e("RomanceFragment", "❌ API Error occurred.")
                    Toast.makeText(context, "Error loading data. Please try again", Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.loadRomanaceData() // Ensure this method exists in AdventureModels
    }
}
