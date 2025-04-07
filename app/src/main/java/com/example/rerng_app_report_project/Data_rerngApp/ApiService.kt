package com.example.rerng_app_report_project.Data_rerngApp

import Movie
import android.telecom.Call
import com.example.rerng_app_report_project.AuthenticationRes
import com.example.rerng_app_report_project.Models_rerngApp.GenericResponse
import com.example.rerng_app_report_project.Models_rerngApp.LoginModels
import com.example.rerng_app_report_project.Models_rerngApp.Movy
import com.example.rerng_app_report_project.Models_rerngApp.RegisterModels
import com.example.rerng_app_report_project.Models_rerngApp.Review
import com.example.rerng_app_report_project.Models_rerngApp.ReviewRequest
import com.example.rerng_app_report_project.Models_rerngApp.ReviewResponse
import com.example.rerng_app_report_project.Models_rerngApp.WatchlistResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
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
    suspend fun getWatchlist(
        @Header("Authorization") token: String
    ): Response<WatchlistResponse>




    @POST("movies/{movieId}/review/")  // change "reviews" to "review"
    suspend fun submitReview(
        @Path("movieId") movieId: Int,
        @Body reviewRequest: ReviewRequest,
        @Header("Authorization") token: String
    ): Response<ReviewResponse>

    @GET("movie/{movieId}/reviews/")
    suspend fun getMovieReviews(
        @Path("movieId") movieId: Int,
        @Header("Authorization") token: String
    ): Response<List<Review>>




    @POST("movies/{id}/watchlist/")
    suspend fun addToWatchlist(
        @Path("id") movieId: Int,
        @Header("Authorization") token: String
    ): Response<GenericResponse>





















}
