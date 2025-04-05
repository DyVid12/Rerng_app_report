package com.example.rerng_app_report_project

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_movie_detail)

        movieId = intent.getIntExtra("MOVIE_ID", -1)

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
            val token = AppEncrypted.get().getToken(this) // Fetch the token from secure storage
            if (token.isNullOrEmpty()) {
                Log.e("MovieDetailActivity", "User is not logged in. Token is null or empty.")
                Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show()

                // Optionally, navigate to the login activity here
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                return@setOnClickListener
            }

            // Proceed with adding the movie to the watchlist
            Log.d("MovieDetailActivity", "Calling addToWatchlist with movieId: $movieId and token: $token")
            addToWatchlist(movieId, token)
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

                // Log status code and body
                Log.d("Watchlist", "Response Code: ${response.code()}")
                val responseBody = response.body()?.toString() ?: "No body content"
                Log.d("Watchlist", "Response Body: $responseBody")

                if (response.isSuccessful) {
                    Log.d("Watchlist", "Success: $responseBody")
                    Toast.makeText(this@MovieDetailActivity, "Added to Watchlist! ❤️", Toast.LENGTH_SHORT).show()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("Watchlist", "Failed! Code: ${response.code()}, Error: $errorMessage")
                    Toast.makeText(this@MovieDetailActivity, "Failed to add to Watchlist: $errorMessage", Toast.LENGTH_LONG).show()
                }

            } catch (e: Exception) {
                Log.e("Watchlist", "API Call Error: ${e.message}", e)
                Toast.makeText(this@MovieDetailActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}