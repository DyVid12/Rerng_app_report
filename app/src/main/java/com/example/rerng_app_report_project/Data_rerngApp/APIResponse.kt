package com.example.rerng_app_report_project.Data_rerngApp

import Movie
import com.example.rerng_app_report_project.Models_rerngApp.Movy
import com.example.rerng_app_report_project.User

class APIResponse<T>(
    val message: String,
    val status: String,
    val movies: T?,
    val movies_by_category: Map<String, List<Movy>>? = null,
    val access: String,
    val refresh: String,
    val user: User
)

data class MoviesResponse(
    val status: String,
    val movies: List<Movie>
)

data class Movie(
    val id: Int,
    val title: String,
    val release_date: String,
    val poster: String,
    val rating: Double
)
