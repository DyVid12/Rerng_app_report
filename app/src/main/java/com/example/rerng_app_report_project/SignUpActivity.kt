package com.example.rerng_app_report_project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.rerng_app_report_project.Models_rerngApp.RegisterModels
import com.example.rerng_app_report_project.databinding.ActivitySignupBinding
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btsignup.setOnClickListener {
            attemptSignup()
        }

        // Redirect to LoginActivity if user has an account
        binding.loginPrompt.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun attemptSignup() {
        val username = binding.usernameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        // Get the selected gender from the Spinner
        val gender = binding.genderSpinner.selectedItem.toString().trim()

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || gender.isEmpty()) {
            showAlert("Invalid Input", "Please fill in all fields!")
        } else {
            binding.progressBar.visibility = View.VISIBLE

            // Create the signUpRequest object
            val signUpRequest = RegisterModels(username, email, gender, password)

            lifecycleScope.launch {
                try {
                    val response = RetrofitInstance.apiService.signUp(signUpRequest)

                    if (response.isSuccessful) {
                        val authenticationRes = response.body() // Get the response body
                        if (authenticationRes != null) {
                            // Handle the response
                            Toast.makeText(this@SignUpActivity, "Account Created Successfully", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            showAlert("Error", "Empty response received.")
                        }
                    } else {
                        showAlert("Sign Up Failed", "Something went wrong.")
                    }
                } catch (e: Exception) {
                    showAlert("Error", "An error occurred while signing up.")
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
