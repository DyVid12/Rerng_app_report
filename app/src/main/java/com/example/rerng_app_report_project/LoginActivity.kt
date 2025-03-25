package com.example.rerng_app_report_project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.rerng_app_report_project.Models_rerngApp.LoginModels
import com.example.rerng_app_report_project.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.loginButton.setOnClickListener {
            attemptLogin()
        }

        // Add the OnClickListener for "Sign Up" text
        binding.signupTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }


    private fun attemptLogin() {
        val username = binding.usernameEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Invalid Input", "Please fill in all fields!")
        } else {
            binding.progressBar.visibility = View.VISIBLE

            // Call the signIn API
            val loginRequest = LoginModels(username, password)
            lifecycleScope.launch {
                try {
                    val response = RetrofitInstance.apiService.signIn(loginRequest)
                    if (response.isSuccessful) {
                        val authRes = response.body() // This is AuthenticationRes

                        // Check if the response body contains user data
                        if (authRes != null && authRes.user != null) {
                            // Show a toast and navigate to the ProfileActivity with user details
                            Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_LONG).show()

                            val user = authRes.user
                            val intent = Intent(this@LoginActivity, ProfileActivity::class.java).apply {
                                // Pass user data to ProfileActivity
                                putExtra("username", user.username)
                                putExtra("email", user.email)
                                putExtra("gender", user.gender)
                            }
                            startActivity(intent)
                            finish() // Optional: Finish LoginActivity to prevent going back
                        } else {
                            showAlert("Error", "Failed to fetch user data.")
                        }
                    } else {
                        showAlert("Login Failed", "Invalid credentials.")
                    }
                } catch (e: Exception) {
                    showAlert("Error", "An error occurred while logging in.")
                } finally {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun showAlert(title: String, message: String) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
