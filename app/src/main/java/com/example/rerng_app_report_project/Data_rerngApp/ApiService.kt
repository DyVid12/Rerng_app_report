package com.example.rerng_app_report_project.Data_rerngApp

import Movie
import com.example.rerng_app_report_project.AuthenticationRes
import com.example.rerng_app_report_project.Models_rerngApp.LoginModels
import com.example.rerng_app_report_project.Models_rerngApp.Movy
import com.example.rerng_app_report_project.Models_rerngApp.RegisterModels
import com.example.rerng_app_report_project.Models_rerngApp.ReviewRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
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
    suspend fun signIn(@Body loginModels: LoginModels): Response<AuthenticationRes>

    @POST("/Authorization/register")
    suspend fun signUp(@Body signUpRequest: RegisterModels): Response<AuthenticationRes>




    @GET("watchlist/")
    suspend fun getWatchlist(@Header("Authorization") token: String): Response<APIResponse<List<Movie>>>





    @POST("movies/{movieId}/reviews")  // Replace with the correct endpoint path
        suspend fun addReview(
            @Path("movieId") movieId: Int,  // Movie ID passed dynamically
            @Body reviewRequest: ReviewRequest  // Review details passed in the body
        ): Response<Void>










}
