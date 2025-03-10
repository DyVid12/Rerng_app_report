package com.example.rerng_app_report_project.Data_rerngApp

import com.example.rerng_app_report_project.AuthenticationRes
import com.example.rerng_app_report_project.Models_rerngApp.LoginModels
import com.example.rerng_app_report_project.Models_rerngApp.Movy
import com.example.rerng_app_report_project.Models_rerngApp.RegisterModels
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/Movie_data/")
    suspend fun getMovies(): APIResponse<List<Movy>>

    @GET("/Movie_data/search_movies_by_category/{category}/")  // Corrected endpoint
    suspend fun searchMovies(
        @Path("category") category: String,
        @Query("q") query: String
    ): APIResponse<List<Movy>>

    @POST("/Authorization/login")
    suspend fun signIn(@Body loginRequest: LoginModels): APIResponse<AuthenticationRes>

    @POST("/Authorization/register")
    suspend fun signUp(@Body signUpRequest: RegisterModels): APIResponse<AuthenticationRes>

}
