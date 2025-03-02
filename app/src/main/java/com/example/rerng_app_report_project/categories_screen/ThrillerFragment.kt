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
import com.example.rerng_app_report_project.ViewModels.ThrillerModels
import com.example.rerng_app_report_project.adapter.ThrillerAdapter
import com.example.rerng_app_report_project.databinding.FragmentThrillerBinding

class ThrillerFragment : Fragment() {
    private lateinit var binding: FragmentThrillerBinding
    private lateinit var thrillerAdapter: ThrillerAdapter
    private val viewModel by viewModels<ThrillerModels>()  // Fix ViewModel reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThrillerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        thrillerAdapter = ThrillerAdapter()

        binding.thrillerrecycleAll.apply{  // Ensure this ID exists in XML
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            layoutManager = GridLayoutManager(context, 2)
            adapter = thrillerAdapter
        }

        viewModel.dataState.observe(viewLifecycleOwner) { dataState ->
            when (dataState.state) {
                State.SUCCESS -> {
                    val movies = dataState.movies
                    Log.d("ThrillerFragment", "✅ Movie List Received: $movies")

                    if (!movies.isNullOrEmpty()) {
                        thrillerAdapter.submitList(movies)
                    } else {
                        Log.e("ThrillerFragment", "❌ API returned empty list!")
                        Toast.makeText(context, "No movies available", Toast.LENGTH_LONG).show()
                    }
                }
                State.ERROR -> {
                    Log.e("ThrillerFragment", "❌ API Error occurred.")
                    Toast.makeText(context, "Error loading data. Please try again", Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.loadThrillerData()  // Ensure this method exists in AdventureModels
    }
}
