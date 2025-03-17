package com.example.rerng_app_report_project.Data_rerngApp

import com.example.rerng_app_report_project.AuthenticationRes
import com.example.rerng_app_report_project.Models_rerngApp.LoginModels
import com.example.rerng_app_report_project.Models_rerngApp.Movy
import com.example.rerng_app_report_project.Models_rerngApp.RegisterModels
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/Movie_data/")
    suspend fun getMovies(): APIResponse<List<Movy>>

    @GET("/Movie_data/search_movies_by_category/{category}/")
    suspend fun searchMovies(
        @Path("category") category: String  // Category directly in the URL
    ): APIResponse<List<Movy>>

    @GET("/Movie_data/search_movies_by_title/")
    suspend fun searchMoviesByTitle(
        @Query("q") title: String  // Title query parameter
    ): APIResponse<List<Movy>>

    @POST("/Authorization/login")
    suspend fun signIn(@Body loginRequest: LoginModels): Response<APIResponse<AuthenticationRes>>

    @POST("/Authorization/register")
    suspend fun signUp(@Body signUpRequest: RegisterModels): APIResponse<AuthenticationRes>

}
