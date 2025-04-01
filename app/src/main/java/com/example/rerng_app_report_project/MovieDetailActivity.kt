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
import com.bumptech.glide.Glide

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
                Log.d("MovieDetailActivity", "Passing movie ID: $movieId")  // Log for debugging
                val intent = Intent(this, AddReviewActivity::class.java)
                intent.putExtra("MOVIE_ID", movieId)  // Pass movie ID to the review activity
                startActivity(intent)
            } else {
                Log.e("MovieDetailActivity", "Error: Invalid Movie ID passed!")
                Toast.makeText(this, "Error: Invalid Movie ID", Toast.LENGTH_SHORT).show()
            }
        }





        // Handle "Add to Watchlist" button click
        btnAddToWatchlist.setOnClickListener {
            Log.d("MovieDetailActivity", "Add to Watchlist clicked")
            // You can save this movie to a local database or shared preferences.
        }
    }
}
