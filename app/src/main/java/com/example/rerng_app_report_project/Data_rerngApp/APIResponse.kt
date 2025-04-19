package com.example.rerng_app_report_project.Data_rerngApp

import com.example.rerng_app_report_project.Models_rerngApp.Movie
import com.example.rerng_app_report_project.Models_rerngApp.MovieDetail
import com.example.rerng_app_report_project.Models_rerngApp.Movy
import com.example.rerng_app_report_project.Models_rerngApp.Review
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





data class ReviewsResponse(
    val reviews: List<Review>

)
