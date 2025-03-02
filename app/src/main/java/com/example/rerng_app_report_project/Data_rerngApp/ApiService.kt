package com.example.rerng_app_report_project.Data_rerngApp


import com.example.rerng_app_report_project.Models_rerngApp.Movy

import retrofit2.http.GET

interface ApiService {
    @GET("/Movie_data/")
    suspend fun getMovies(): APIResponse<List<Movy>>
}
