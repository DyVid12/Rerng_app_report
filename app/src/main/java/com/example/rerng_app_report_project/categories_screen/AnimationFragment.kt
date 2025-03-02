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
import com.example.rerng_app_report_project.ViewModels.AnimationModels
import com.example.rerng_app_report_project.adapter.AnimationAdapter
import com.example.rerng_app_report_project.databinding.FragmentAnimationBinding

class AnimationFragment : Fragment() {
    private lateinit var binding: FragmentAnimationBinding
    private lateinit var animationAdapter: AnimationAdapter
    private val viewModel by viewModels<AnimationModels>()  // Fix ViewModel reference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnimationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animationAdapter = AnimationAdapter()

        binding.animationrecycleAll.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            layoutManager = GridLayoutManager(context, 2)
            adapter = animationAdapter
        }

        viewModel.dataState.observe(viewLifecycleOwner) { dataState ->
            when (dataState.state) {
                State.SUCCESS -> {
                    val movies = dataState.movies
                    Log.d("AnimationFragment", "✅ Movie List Received: $movies")

                    if (!movies.isNullOrEmpty()) {
                        animationAdapter.submitList(movies)
                    } else {
                        Log.e("AnimationFragment", "❌ API returned empty list!")
                        Toast.makeText(context, "No movies available", Toast.LENGTH_LONG).show()
                    }
                }
                State.ERROR -> {
                    Log.e("AnimationFragment", "❌ API Error occurred.")
                    Toast.makeText(context, "Error loading data. Please try again", Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.loadAnimationData()  // Fix method name
    }
}
