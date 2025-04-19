package com.example.rerng_app_report_project.Models_rerngApp


data class WatchlistResponse(
    val movies: List<Movie>
)


data class Movie(
    val id: Int,
    val title: String,
    val release_date: String,
    val overview: String?,
    val rating: Double,
    val poster: String,
    val trailer_url: String?,
    val movie_detail: MovieDetail1
)


data class MovieDetail1(
    val title: String,
    val release_date: String,
    val rating: Double?,
    val overview: String?,
    val poster: String,
    val trailer_url: String?
)