package com.example.rerng_app_report_project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
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

        setupGenderSpinner()
        setupListeners()
    }

    private fun setupGenderSpinner() {
        val genders = listOf("Male", "Female", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, genders)
        binding.genderSpinner.setAdapter(adapter)

        // Ensure dropdown is clickable
        binding.genderSpinner.setOnClickListener {
            binding.genderSpinner.showDropDown()
        }
    }


    private fun setupListeners() {
        binding.btsignup.setOnClickListener {
            attemptSignup()
        }

        binding.loginPrompt.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun attemptSignup() {
        val username = binding.usernameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        // Retrieve selected gender
        val selectedGender = binding.genderSpinner.text.toString().trim()
        val gender = when (selectedGender) {
            "Male" -> "M"
            "Female" -> "F"
            "Other" -> "O"
            else -> ""
        }

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || gender.isEmpty()) {
            showAlert("Invalid Input", "Please fill in all fields!")
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        val signUpRequest = RegisterModels(username, email, gender, password)

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.apiService.signUp(signUpRequest)
                if (response.isSuccessful) {
                    Toast.makeText(this@SignUpActivity, "Account Created Successfully", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                    finish()
                } else {
                    showAlert("Sign Up Failed", "Something went wrong. Please try again.")
                }
            } catch (e: Exception) {
                showAlert("Error", "An error occurred while signing up.")
            } finally {
                binding.progressBar.visibility = View.GONE
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
