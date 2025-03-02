package com.example.rerng_app_report_project.Models_rerngApp

import com.example.rerng_app_report_project.Data_rerngApp.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiManager {
    private var dataService: ApiService? = null

    fun getDataService(): ApiService {
        if (dataService == null) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY // Corrected this line
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")  // ✅ Correct for Emulator
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            dataService = retrofit.create(ApiService::class.java)
        }
        return dataService!!
    }

    // ✅ Add this function to fetch movies
    suspend fun getMovies() = getDataService().getMovies()
}
