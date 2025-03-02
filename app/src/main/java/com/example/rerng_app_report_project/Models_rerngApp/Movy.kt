package com.example.rerng_app_report_project.Models_rerngApp

data class Movy(
    val genres: List<String>,
    val id: Int,
    val movie_detail: MovieDetail,
    val poster: String,
    val rating: Double?,
    val release_date: String?,
    val title: String?
)

