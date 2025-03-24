package com.example.rerng_app_report_project.Data_rerngApp

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
// Define a data class for the API response






