package com.example.rerng_app_report_project

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.rerng_app_report_project.global.AppEncrypted
import kotlinx.coroutines.launch

class MovieDetailActivity : AppCompatActivity() {
    private var movieId: Int = -1
    private lateinit var reviewContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_movie_detail)

        movieId = intent.getIntExtra("MOVIE_ID", -1)
        reviewContainer = findViewById(R.id.reviewContainer)

        val token = AppEncrypted.get().getToken(this)
        if (!token.isNullOrEmpty() && movieId != -1) {
            getMovieReviews(movieId, token)
        } else {
            Log.e("MovieDetailActivity", "Token or Movie ID is missing.")
        }


        // Log the received movieId for debugging
        Log.d("MovieDetailActivity", "Received movieId: $movieId")

        // Find views
        val poster = findViewById<ImageView>(R.id.imgMoviePoster)
        val title = findViewById<TextView>(R.id.txtMovieTitle)
        val releaseDate = findViewById<TextView>(R.id.txtReleaseDate)
        val rating = findViewById<TextView>(R.id.txtRating)
        val overview = findViewById<TextView>(R.id.txtOverview)
        val btnTrailer = findViewById<Button>(R.id.btnWatchTrailer)
        val btnAddReview = findViewById<Button>(R.id.btnAddReview)
        val btnAddToWatchlist = findViewById<Button>(R.id.btnAddToWatchlist)
        val txtBackToList = findViewById<TextView>(R.id.txtBackToList)

        // Get data from Intent
        val movieTitle = intent.getStringExtra("title")
        val movieReleaseDate = intent.getStringExtra("release_date")
        val movieOverview = intent.getStringExtra("overview")
        val movieRating = intent.getStringExtra("rating")
        val trailerUrl = intent.getStringExtra("trailer_url")
        val posterUrl = intent.getStringExtra("poster")
        val movieId = intent.getIntExtra("MOVIE_ID", -1)  // Get movie ID

        // Log received values for debugging
        Log.d("MovieDetailActivity", "Received movie ID: $movieId")
        Log.d("MovieDetailActivity", "Received Rating: $movieRating")
        Log.d("MovieDetailActivity", "Received Poster URL: $posterUrl")
        Log.d("MovieDetailActivity", "Received movie ID: $movieId")

        // Set data to views
        title.text = movieTitle
        releaseDate.text = "Release Date: $movieReleaseDate"
        overview.text = movieOverview
        rating.text = if (!movieRating.isNullOrEmpty()) "⭐ $movieRating /10" else "Rating: N/A"

        // Load poster
        if (!posterUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(posterUrl)
                .placeholder(R.drawable.begman) // Default image
                .into(poster)
        } else {
            Log.e("MovieDetailActivity", "Poster URL is null or empty!")
            poster.setImageResource(R.drawable.begman) // Default image
        }


        // Handle trailer button click
        btnTrailer.setOnClickListener {
            if (!trailerUrl.isNullOrEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl))
                startActivity(intent)
            }
        }


        // Handle "Back to Movie List" click
        txtBackToList.setOnClickListener {
            onBackPressed() // Go back to the previous screen
        }

        // Handle "Add Review" button click
        btnAddReview.setOnClickListener {
            if (movieId != -1) {
                val intent = Intent(this, AddReviewActivity::class.java)
                intent.putExtra("MOVIE_ID", movieId) // Pass the correct movieId here
                startActivity(intent)
            } else {
                Log.e("MovieDetailActivity", "Invalid Movie ID!")
                Toast.makeText(this, "Error: Invalid Movie ID!", Toast.LENGTH_SHORT).show()
            }
        }


        btnAddToWatchlist.setOnClickListener {
            Log.d("MovieDetailActivity", "Movie ID before add to watchlist: $movieId")

            // Check if movieId is valid
            if (movieId == -1) {
                Log.e("MovieDetailActivity", "Invalid Movie ID! Cannot add to watchlist.")
                Toast.makeText(this, "Invalid Movie ID!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if the user is logged in and the token is available
            val token = AppEncrypted.get().getToken(this)  // Get token securely
            if (token.isNullOrEmpty()) {
                Log.e("MovieDetailActivity", "User is not logged in. Token is null or empty.")
                Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))  // Navigate to login screen
                return@setOnClickListener
            }

            // Proceed with adding the movie to the watchlist
            Log.d("MovieDetailActivity", "Calling addToWatchlist with movieId: $movieId and token: $token")
            addToWatchlist(movieId, token)  // Proceed with API call
        }

    }


    private fun addToWatchlist(movieId: Int, token: String) {
        val apiService = ApiClient.apiService

        lifecycleScope.launch {
            Log.d("Watchlist", "Sending request for Movie ID: $movieId with Token: Bearer $token")

            try {
                val response = apiService.addToWatchlist(
                    movieId,
                    "Bearer $token"
                )

                Log.d("Watchlist", "Response Code: ${response.code()}")
                val responseBody = response.body()?.toString() ?: "No body content"
                Log.d("Watchlist", "Response Body: $responseBody")

                if (response.isSuccessful) {
                    Toast.makeText(this@MovieDetailActivity, "Added to Watchlist! ❤️", Toast.LENGTH_SHORT).show()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("Watchlist", "Failed! Error: $errorMessage")

                    // Check for authentication error and show login message
                    if (errorMessage.contains("Authentication credentials were not provided", true)) {
                        Toast.makeText(this@MovieDetailActivity, "Please log in first", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@MovieDetailActivity, LoginActivity::class.java))  // Navigate to login
                    } else {
                        Toast.makeText(this@MovieDetailActivity, "Failed to add to Watchlist: $errorMessage", Toast.LENGTH_LONG).show()
                    }
                }

            } catch (e: Exception) {
                Log.e("Watchlist", "API Call Error: ${e.message}", e)
                Toast.makeText(this@MovieDetailActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getMovieReviews(movieId: Int, token: String) {
        val apiService = ApiClient.apiService

        lifecycleScope.launch {
            try {
                Log.d("MovieDetailActivity", "Calling getMovieReviews with movieId: $movieId and token: $token")

                val response = apiService.getMovieReviews(movieId, "Bearer $token")

                Log.d("MovieDetailActivity", "Response Code: ${response.code()}")
                Log.d("MovieDetailActivity", "Response Body: ${response.body()}")
                Log.d("MovieDetailActivity", "Response Message: ${response.message()}")

                if (response.code() == 401) {
                    Toast.makeText(this@MovieDetailActivity, "Authentication failed. Please log in again.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@MovieDetailActivity, LoginActivity::class.java))
                    return@launch
                }

                if (response.isSuccessful && response.body() != null) {
                    val reviews = response.body()!!

                    // If there are reviews, remove the placeholder and add actual reviews
                    if (reviews.isNotEmpty()) {
                        findViewById<TextView>(R.id.txtReview).visibility = View.GONE // Hide the "No reviews yet" placeholder

                        // Add reviews dynamically to the layout
                        reviews.forEach { review ->
                            val reviewLayout = LinearLayout(this@MovieDetailActivity)
                            reviewLayout.orientation = LinearLayout.HORIZONTAL
                            reviewLayout.setPadding(16, 10, 16, 16)

                            val userText = TextView(this@MovieDetailActivity)
                            userText.text = "By: ${review.user_name}"
                            userText.setTextColor(Color.WHITE)

                            val ratingText = TextView(this@MovieDetailActivity)
                            ratingText.text = "Rating: ${review.rating}"
                            ratingText.setTextColor(Color.YELLOW)

                            val commentText = TextView(this@MovieDetailActivity)
                            commentText.text = "comment: ${review.comment}"
                            commentText.setTextColor(Color.LTGRAY)

                            // Add TextViews to the review layout
                            reviewLayout.addView(userText)
                            reviewLayout.addView(ratingText)
                            reviewLayout.addView(commentText)

                            // Add the review layout to the container
                            reviewContainer.addView(reviewLayout)
                        }
                    } else {
                        // If no reviews, show a placeholder message
                        findViewById<TextView>(R.id.txtReview).visibility = View.VISIBLE
                    }

                } else {
                    Toast.makeText(this@MovieDetailActivity, "Failed to load reviews", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("MovieDetailActivity", "Error fetching reviews: ${e.message}", e)
                Toast.makeText(this@MovieDetailActivity, "Error fetching reviews: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }





}