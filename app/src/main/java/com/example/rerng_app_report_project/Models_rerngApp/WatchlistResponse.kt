package com.example.rerng_app_report_project.Models_rerngApp

data class WatchlistResponse(
    val status: String,
    val movies: List<Movie> // Ensure this is a List<Movie> type
)

data class Movie(
    val id: Int,
    val title: String,
    val release_date: String,
    val overview: String,
    val rating: Double,
    val poster: String,
    val trailer_url: String
)
