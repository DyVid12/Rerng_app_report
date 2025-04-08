package com.example.rerng_app_report_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.rerng_app_report_project.Models_rerngApp.ReviewRequest
import kotlinx.coroutines.launch

class AddReviewActivity : AppCompatActivity() {

    private var movieId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)

        // Retrieve the movie ID
        movieId = intent.getIntExtra("MOVIE_ID", -1)
        Log.d("AddReviewActivity", "Received Movie ID: $movieId")

        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val etReview = findViewById<EditText>(R.id.etReview)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val rating = ratingBar.rating.toInt()  // Convert to integer
            val reviewText = etReview.text.toString().trim()

            if (movieId == -1) {
                Toast.makeText(this, "Invalid Movie ID!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (reviewText.isEmpty()) {
                Toast.makeText(this, "Please enter your review.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            submitReview(movieId, rating, reviewText)
        }
    }

    private fun submitReview(movieId: Int, rating: Int, review: String) {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("USER_TOKEN", null)

        // Check if token is null or empty (user is logged out)
        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java)) // Navigate to login screen
            return
        }

        val userId = sharedPreferences.getInt("USER_ID", -1)
        if (userId == -1) {
            Toast.makeText(this, "Invalid user data. Please log in again.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java)) // Navigate to login screen
            return
        }

        if (rating == 0) {
            Toast.makeText(this, "Please provide a rating.", Toast.LENGTH_SHORT).show()
            return
        }

        val reviewRequest = ReviewRequest(user = userId, rating = rating.toFloat(), comment = review)

        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.submitReview(movieId, reviewRequest, "Bearer $token")

                if (response.isSuccessful) {
                    Toast.makeText(this@AddReviewActivity, "Review submitted!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val errorMessage = response.errorBody()?.string()
                    Log.e("AddReviewActivity", "Error: $errorMessage")
                    Toast.makeText(this@AddReviewActivity, "Failed: $errorMessage", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.e("AddReviewActivity", "API Call Error: ${e.message}", e)
                Toast.makeText(this@AddReviewActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }



}
