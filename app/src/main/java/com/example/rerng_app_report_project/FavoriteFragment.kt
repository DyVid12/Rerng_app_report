package com.example.rerng_app_report_project

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rerng_app_report_project.adapter.FavoriteMoviesAdapter
import com.example.rerng_app_report_project.Models_rerngApp.Movie
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private lateinit var favoriteMoviesRecyclerView: RecyclerView
    private lateinit var favoriteMoviesAdapter: FavoriteMoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        // Initialize RecyclerView
        favoriteMoviesRecyclerView = view.findViewById(R.id.favoriteMoviesRecyclerView)

        // Use GridLayoutManager with 2 columns (for 2 items per row vertically)
        val layoutManager = GridLayoutManager(context, 2) // 2 columns, vertically oriented
        favoriteMoviesRecyclerView.layoutManager = layoutManager

        // Initialize the adapter with an empty list for now
        favoriteMoviesAdapter = FavoriteMoviesAdapter()
        favoriteMoviesRecyclerView.adapter = favoriteMoviesAdapter

        // Check if the user is logged in and fetch favorite movies
        checkUserTokenAndFetchMovies()

        return view
    }

    private fun checkUserTokenAndFetchMovies() {
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("USER_TOKEN", null)

        Log.d("FavoriteFragment", "Retrieved token: $token") // Log the token for debugging

        if (token.isNullOrEmpty()) {
            Log.e("FavoriteFragment", "User is not logged in (Token is null or empty)")
            Toast.makeText(context, "User is not logged in", Toast.LENGTH_SHORT).show()
            favoriteMoviesAdapter.submitList(emptyList()) // Clear the list if the user is not logged in
            // Optionally, redirect the user to the login screen here
        } else {
            Log.d("FavoriteFragment", "Token exists. Fetching movies...")
            getFavoriteMovies(token)
        }
    }

    private fun getFavoriteMovies(token: String) {
        val apiService = ApiClient.apiService

        lifecycleScope.launch {
            try {
                Log.d("FavoriteFragment", "Using Token: $token")

                // Fetch favorite movies from the API
                val response = apiService.getWatchlist("Bearer $token")

                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Log.d("FavoriteFragment", "API Response: ${response.body()}")

                    if (apiResponse != null && apiResponse.movies != null) {
                        val favoriteMoviesList = apiResponse.movies // List<Movie> from API response
                        Log.d("FavoriteFragment", "Movies count: ${favoriteMoviesList.size}")

                        // Submit the list to the adapter for rendering in the RecyclerView
                        favoriteMoviesAdapter.submitList(favoriteMoviesList)
                    } else {
                        Log.e("FavoriteFragment", "No movies found in response")
                        Toast.makeText(context, "No favorite movies found", Toast.LENGTH_SHORT).show()
                        favoriteMoviesAdapter.submitList(emptyList()) // Clear the list if no movies found
                    }
                } else {
                    Log.e("FavoriteFragment", "Failed to load favorite movies: ${response.errorBody()}")
                    Toast.makeText(context, "Failed to load favorite movies", Toast.LENGTH_SHORT).show()
                    favoriteMoviesAdapter.submitList(emptyList()) // Clear the list on error
                }
            } catch (e: Exception) {
                Log.e("FavoriteFragment", "Error fetching favorite movies: ${e.message}", e)
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                favoriteMoviesAdapter.submitList(emptyList()) // Clear the list on error
            }
        }
    }
}



