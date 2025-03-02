package com.example.rerng_app_report_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rerng_app_report_project.Data_rerngApp.State
import com.example.rerng_app_report_project.ViewModels.MovieViewModels
import com.example.rerng_app_report_project.adapter.MovieAdapter
import com.example.rerng_app_report_project.categories_screen.ActionFragment
import com.example.rerng_app_report_project.categories_screen.AdventureFragment
import com.example.rerng_app_report_project.categories_screen.AllFragment
import com.example.rerng_app_report_project.categories_screen.AnimationFragment
import com.example.rerng_app_report_project.categories_screen.ComedyFragment
import com.example.rerng_app_report_project.categories_screen.DramaFragment
import com.example.rerng_app_report_project.categories_screen.FantasyFragment
import com.example.rerng_app_report_project.categories_screen.HorrorFragment
import com.example.rerng_app_report_project.categories_screen.RomanceFragment
import com.example.rerng_app_report_project.categories_screen.SciFiFragment
import com.example.rerng_app_report_project.categories_screen.ThrillerFragment
import com.example.rerng_app_report_project.categories_screen.WarFragment
import com.example.rerng_app_report_project.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var movieAdapter: MovieAdapter
    private val viewModel by viewModels<MovieViewModels>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter = MovieAdapter()

        binding.recyclerCategory.apply {
//            layoutManager = GridLayoutManager(context, 3)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = movieAdapter
        }

        viewModel.dataState.observe(viewLifecycleOwner) { dataState ->
            when (dataState.state) {
                State.SUCCESS -> {
                    val movies = dataState.movies
                    Log.d("HomeFragment", "✅ Movie List Received: $movies")

                    if (!movies.isNullOrEmpty()) {
                        movieAdapter.submitList(movies)
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



        viewModel.loadMoviedata()

        // ✅ Load AllFragment by default
        parentFragmentManager.commit {
            replace(R.id.fragment_container, AllFragment())
        }

        // ✅ Highlight "All" button by default
        binding.Allbtn.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_light))

        setupButton(binding.Actionbtn, ActionFragment())
        setupButton(binding.Adventurebtn, AdventureFragment())
        setupButton(binding.Comedybtn, ComedyFragment())
        setupButton(binding.Dramabtn, DramaFragment())
        setupButton(binding.Horrorbtn, HorrorFragment())
        setupButton(binding.SciFibtn, SciFiFragment())
        setupButton(binding.Fantasybtn, FantasyFragment())
        setupButton(binding.Romancebtn, RomanceFragment())
        setupButton(binding.Thrillerbtn, ThrillerFragment())
        setupButton(binding.Animationbtn, AnimationFragment())
        setupButton(binding.Warbtn, WarFragment())
        setupButton(binding.Allbtn, AllFragment())


    }

    private fun setupButton(button: Button, fragment: Fragment) {
        button.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragment_container, fragment)
                addToBackStack(null)
            }
            resetButtonColors()
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_light))
        }
    }

    private fun resetButtonColors() {
        val buttons = listOf(
            binding.Allbtn,binding.Actionbtn, binding.Adventurebtn, binding.Comedybtn,
            binding.Dramabtn, binding.Horrorbtn, binding.SciFibtn,
            binding.Fantasybtn, binding.Romancebtn, binding.Thrillerbtn,
            binding.Animationbtn, binding.Warbtn
        )
        buttons.forEach { it.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.holo_purple)) }
    }

}