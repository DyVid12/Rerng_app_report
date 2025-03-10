package com.example.rerng_app_report_project.Data_rerngApp

import com.example.rerng_app_report_project.Models_rerngApp.Movy

class APIResponse<T>(
    val message: String,
    val status: String,
    val movies: T?,
    val movies_by_category: Map<String, List<Movy>>? = null,




    )




