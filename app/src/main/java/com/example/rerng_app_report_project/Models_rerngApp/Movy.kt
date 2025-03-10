package com.example.rerng_app_report_project.Models_rerngApp

data class Movy(
    val id: Int,
    val title: String,
    val release_date: String,
    val poster: String,
    val rating: Float,
    val genres: List<String>,
    val movie_detail: MovieDetail
)
