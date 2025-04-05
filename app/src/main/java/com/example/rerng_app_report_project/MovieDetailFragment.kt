package com.example.rerng_app_report_project

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {
    private var movieId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieId = arguments?.getInt("MOVIE_ID", -1) ?: -1
        Log.d("MovieDetailFragment", "Movie ID in onCreate: $movieId")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("MovieDetailFragment", "Movie ID in onViewCreated: $movieId")

        if (movieId == -1) {
            Log.e("MovieDetailFragment", "Error: Invalid Movie ID!")
        }
    }
}

