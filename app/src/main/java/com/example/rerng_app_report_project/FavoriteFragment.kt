package com.example.rerng_app_report_project

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rerng_app_report_project.adapter.FavoriteMoviesAdapter
import com.example.rerng_app_report_project.Models_rerngApp.Movie
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private lateinit var favoriteMoviesRecyclerView: RecyclerView
    private lateinit var favoriteMoviesAdapter: FavoriteMoviesAdapter
    private var favoriteMovies: List<Movie> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        favoriteMoviesRecyclerView = view.findViewById(R.id.favoriteMoviesRecyclerView)
        favoriteMoviesAdapter = FavoriteMoviesAdapter(favoriteMovies)
        favoriteMoviesRecyclerView.layoutManager = LinearLayoutManager(context)
        favoriteMoviesRecyclerView.adapter = favoriteMoviesAdapter

        checkUserTokenAndFetchMovies()

        return view
    }

    private fun checkUserTokenAndFetchMovies() {
        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("user_token", null)

        // Debug: Check if the token is being retrieved
        Log.d("FavoriteFragment", "Retrieved token: $token")

        if (token.isNullOrEmpty()) {
            Log.e("FavoriteFragment", "User is not logged in (Token is null or empty)")
            Toast.makeText(context, "User is not logged in", Toast.LENGTH_SHORT).show()
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

                val response = apiService.getWatchlist("Bearer $token") // Send token in header

                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    Log.d("FavoriteFragment", "API Response: ${response.body()}")

                    if (apiResponse != null && apiResponse.movies != null) {
                        val favoriteMoviesList = apiResponse.movies // This is a List<Movie>
                        Log.d("FavoriteFragment", "Movies count: ${favoriteMoviesList.size}")
                        favoriteMoviesAdapter.updateData(favoriteMoviesList)
                    } else {
                        Log.e("FavoriteFragment", "No movies found in response")
                        Toast.makeText(context, "No favorite movies found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("FavoriteFragment", "Failed to load favorite movies: ${response.errorBody()}")
                    Toast.makeText(context, "Failed to load favorite movies", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("FavoriteFragment", "Error fetching favorite movies: ${e.message}", e)
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
