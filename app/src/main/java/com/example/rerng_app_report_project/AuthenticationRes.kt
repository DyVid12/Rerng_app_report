package com.example.rerng_app_report_project


// AuthenticationRes.kt
data class AuthenticationRes(
    val token: String,
    val user: User
)



// UserData.kt
data class User(
    val id: Int,
    val gender: String,
    val username: String,
    val email: String
)

