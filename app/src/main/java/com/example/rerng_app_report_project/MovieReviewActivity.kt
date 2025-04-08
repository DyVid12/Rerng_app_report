package com.example.rerng_app_report_project

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.rerng_app_report_project.Models_rerngApp.ReviewRequest
import com.example.rerng_app_report_project.global.AppEncrypted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieReviewActivity : AppCompatActivity() {

    private lateinit var ratingBar: RatingBar
    private lateinit var etReview: EditText
    private lateinit var btnSubmit: Button
    private lateinit var tvBack: TextView
    private var movieId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_review)

        // Initialize views
        ratingBar = findViewById(R.id.ratingBar)
        etReview = findViewById(R.id.etReview)
        btnSubmit = findViewById(R.id.btnSubmit)
        tvBack = findViewById(R.id.tvBack)

        // Get movieId from intent
        movieId = intent.getIntExtra("MOVIE_ID", -1)

        // Log the received movieId for debugging
        Log.d("MovieReviewActivity", "Received movieId: $movieId")

        if (movieId == -1) {
            Toast.makeText(this, "Error: Invalid Movie ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Handle submit button click
        btnSubmit.setOnClickListener {
            submitReview()
        }

        // Handle back button click
        tvBack.setOnClickListener {
            finish()
        }
    }

    private fun submitReview() {
        val rating = ratingBar.rating
        val reviewText = etReview.text.toString().trim()

        if (reviewText.isEmpty() || rating == 0f) {
            Toast.makeText(this, "Please provide a rating and review", Toast.LENGTH_SHORT).show()
            return
        }

        // ✅ Retrieve and format the token
        val rawToken = AppEncrypted.get().getToken(this)
        val token = "Bearer $rawToken"

        // ✅ Check if token is null or empty
        if (rawToken.isNullOrEmpty()) {
            Toast.makeText(this, "Please log in first", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = 12  // Replace with actual logic if needed
        val reviewRequest = ReviewRequest(user = userId, rating = rating, comment = reviewText)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.submitReview(movieId, reviewRequest, token)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Review added successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                        Toast.makeText(applicationContext, "Failed: $errorMessage", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}
