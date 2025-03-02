package com.example.rerng_app_report_project.Models_rerngApp

data class MovieDetail(
    val overview: String,
    val poster: String,
    val release_date: String,
    val title: String,
    val trailer_url: String,
    val rating: Double? = 0.0  // Keep this nullable if the API sometimes omits it
)
