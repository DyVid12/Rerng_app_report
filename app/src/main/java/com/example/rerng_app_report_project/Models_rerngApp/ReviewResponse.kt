package com.example.rerng_app_report_project.Models_rerngApp

data class ReviewRequest(
    val user: Int,        // The ID of the user who is submitting the review
    val rating: Float,    // The rating provided by the user
    val comment: String   // The text of the review
)

data class ReviewResponse(
    val id: Int,          // The ID of the review
    val movie: Int,       // The movie ID associated with this review
    val user: Int,        // The ID of the user who submitted the review
    val rating: Int,      // The rating given by the user
    val comment: String,  // The comment written by the user
    val created_at: String // The timestamp when the review was created
)

