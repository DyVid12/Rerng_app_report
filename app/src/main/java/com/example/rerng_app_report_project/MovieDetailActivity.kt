package com.example.rerng_app_report_project

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class MovieDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_movie_detail)

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

        Log.d("MovieDetailActivity", "Overview: $overview")


        // Get data from Intent
        val movieTitle = intent.getStringExtra("title")
        val movieReleaseDate = intent.getStringExtra("release_date")
        val movieOverview = intent.getStringExtra("overview")
        val movieRating = intent.getStringExtra("rating")
        val trailerUrl = intent.getStringExtra("trailer_url")
        val posterUrl = intent.getStringExtra("poster")

        Log.d("MovieDetailActivity", "Received Rating: $movieRating")
        Log.d("MovieDetailActivity", "Received Poster URL: $posterUrl")

        // Set data to views
        title.text = movieTitle
        releaseDate.text = "Release Date: $movieReleaseDate"
        overview.text = movieOverview

        // Fix rating display
        rating.text = if (!movieRating.isNullOrEmpty()) "⭐ $movieRating /10" else "Rating: N/A"

        // Load poster
        if (!posterUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(posterUrl)
                .placeholder(R.drawable.begman) // Make sure this exists in res/drawable
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
            onBackPressed() // This will take the user back to the previous screen (Movie List)
        }

        // Handle "Add Review" button click
        btnAddReview.setOnClickListener {
            // Logic to open a review dialog or activity to add reviews
            Log.d("MovieDetailActivity", "Add Review clicked")
            // You can start a new activity or show a dialog to add the review.
        }

        // Handle "Add to Watchlist" button click
        btnAddToWatchlist.setOnClickListener {
            // Logic to add the movie to a watchlist
            Log.d("MovieDetailActivity", "Add to Watchlist clicked")
            // You can save this movie to a local database or shared preferences.
        }
    }
}
