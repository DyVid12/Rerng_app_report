package com.example.rerng_app_report_project

// AuthenticationRes.kt
data class AuthenticationRes(
    val access: String,
    val refresh: String,
    val user: User
)


// UserData.kt
data class User(
    val id: Int,
    val username: String,
    val email: String
)

