package com.example.rerng_app_report_project.Data_rerngApp

data class LoginResponse(
    val token: String?,  // Nullable token, received upon successful login
    val userId: Int?     // Nullable user ID associated with the user
)
