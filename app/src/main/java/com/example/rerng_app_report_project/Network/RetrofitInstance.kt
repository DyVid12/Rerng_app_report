package com.example.rerng_app_report_project

import com.example.rerng_app_report_project.Data_rerngApp.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // Replace with your actual base URL
    private const val BASE_URL = "http://10.0.2.2:8000/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
