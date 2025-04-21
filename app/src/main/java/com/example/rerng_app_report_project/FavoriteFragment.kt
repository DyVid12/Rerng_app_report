package com.example.rerng_app_report_project

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rerng_app_report_project.adapter.FavoriteMoviesAdapter
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private lateinit var favoriteMoviesRecyclerView: RecyclerView
    private lateinit var favoriteMoviesAdapter: FavoriteMoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        favoriteMoviesRecyclerView = view.findViewById(R.id.favoriteMoviesRecyclerView)
        favoriteMoviesRecyclerView.layoutManager = GridLayoutManager(context, 2)
        favoriteMoviesAdapter = FavoriteMoviesAdapter()
        favoriteMoviesRecyclerView.adapter = favoriteMoviesAdapter

        checkUserTokenAndFetchMovies()

        return view
    }

    private fun checkUserTokenAndFetchMovies() {
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("USER_TOKEN", null)

        Log.d("FavoriteFragment", "Retrieved token: $token")

        if (token.isNullOrEmpty()) {
            Log.e("FavoriteFragment", "User is not logged in")
            Toast.makeText(context, "User is not logged in", Toast.LENGTH_SHORT).show()
            favoriteMoviesAdapter.submitList(emptyList())
        } else {
            getFavoriteMovies(token)
        }
    }

    private fun getFavoriteMovies(token: String) {
        val apiService = ApiClient.apiService

        lifecycleScope.launch {
            try {
                val response = apiService.getWatchlist("Bearer $token")
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val favoriteMoviesList = apiResponse?.movies ?: emptyList()
                    favoriteMoviesAdapter.submitList(favoriteMoviesList)
                } else {
                    Toast.makeText(context, "Please login first !!", Toast.LENGTH_SHORT).show()
                    favoriteMoviesAdapter.submitList(emptyList())
                }
            } catch (e: Exception) {
                Log.e("FavoriteFragment", "Error fetching favorite movies", e)
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                favoriteMoviesAdapter.submitList(emptyList())
            }
        }
    }

}
