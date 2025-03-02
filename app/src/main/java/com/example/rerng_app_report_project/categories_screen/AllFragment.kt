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
import com.example.rerng_app_report_project.ViewModels.AllMovieViewModel
import com.example.rerng_app_report_project.adapter.AllMovieAdapter
import com.example.rerng_app_report_project.databinding.FragmentAllBinding

class AllFragment : Fragment() {
    private lateinit var binding: FragmentAllBinding
    private lateinit var allMovieAdapter: AllMovieAdapter
    private val viewModel by viewModels<AllMovieViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allMovieAdapter = AllMovieAdapter()

        binding.recycleAll.apply {
          layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            layoutManager = GridLayoutManager(context, 2)
            adapter = allMovieAdapter
        }


        viewModel.dataState.observe(viewLifecycleOwner) { dataState ->
            when (dataState.state) {
                State.SUCCESS -> {
                    val movies = dataState.movies
                    Log.d("HomeFragment", "✅ Movie List Received: $movies")

                    if (!movies.isNullOrEmpty()) {
                        allMovieAdapter.submitList(movies)
                    } else {
                        Log.e("HomeFragment", "❌ API returned empty list!")
                        Toast.makeText(context, "No movies available", Toast.LENGTH_LONG).show()
                    }
                }
                State.ERROR -> {
                    Log.e("HomeFragment", "❌ API Error occurred.")
                    Toast.makeText(context, "Error loading data. Please try again", Toast.LENGTH_LONG).show()
                }
            }
        }



        viewModel.loadAllMoviedata()
    }
}