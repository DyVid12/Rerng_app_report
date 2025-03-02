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
import com.example.rerng_app_report_project.ViewModels.SciFiModels
import com.example.rerng_app_report_project.adapter.SciFiAdapter
import com.example.rerng_app_report_project.databinding.FragmentSciFiBinding

class SciFiFragment : Fragment() {
    private lateinit var binding: FragmentSciFiBinding
    private lateinit var sciFiAdapter: SciFiAdapter
    private val viewModel by viewModels<SciFiModels>()  // Fix ViewModel reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSciFiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sciFiAdapter = SciFiAdapter()

        binding.scifirecycleAll.apply{  // Ensure this ID exists in XML
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            layoutManager = GridLayoutManager(context, 2)
            adapter = sciFiAdapter
        }

        viewModel.dataState.observe(viewLifecycleOwner) { dataState ->
            when (dataState.state) {
                State.SUCCESS -> {
                    val movies = dataState.movies
                    Log.d("AdventureFragment", "✅ Movie List Received: $movies")

                    if (!movies.isNullOrEmpty()) {
                        sciFiAdapter.submitList(movies)
                    } else {
                        Log.e("sciFiFragment", "❌ API returned empty list!")
                        Toast.makeText(context, "No movies available", Toast.LENGTH_LONG).show()
                    }
                }
                State.ERROR -> {
                    Log.e("sciFiFragment", "❌ API Error occurred.")
                    Toast.makeText(context, "Error loading data. Please try again", Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.loadSciFiData()  // Ensure this method exists in AdventureModels
    }
}
