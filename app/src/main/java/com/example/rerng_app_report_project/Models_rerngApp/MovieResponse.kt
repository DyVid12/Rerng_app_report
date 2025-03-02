package com.example.rerng_app_report_project.Models_rerngApp

import Movie
import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val message: String,
    val status: String,
    val movies: List<Movie> // ✅ Matches API response
)
