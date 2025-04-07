package com.example.rerng_app_report_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.rerng_app_report_project.Data_rerngApp.State
import com.example.rerng_app_report_project.ViewModels.LoginViewModels
import com.example.rerng_app_report_project.databinding.ActivityLoginBinding
import com.example.rerng_app_report_project.global.AppEncrypted
import com.example.rerng_app_report_project.global.AppPref

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModels: LoginViewModels by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        setupListeners()
        observeViewModel()
    }

    private fun setupUi() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupListeners() {
        binding.loginButton.setOnClickListener { attemptSignIn() }
        binding.signupTextView.setOnClickListener { navigateToSignUp() }
    }

    private fun observeViewModel() {
        loginViewModels.loginState.observe(this) { state ->
            when (state.state) {
                State.SUCCESS -> handleSuccess(state.movies!!)
                State.ERROR -> {
                    val errorMessage = state.errorMessage ?: "Invalid username or password"
                    Log.e("LoginActivity", "Login failed: $errorMessage")
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE  // Hide progress bar on error
                }
            }
        }
    }

    private fun attemptSignIn() {
        val username = binding.usernameEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        // Show loading indicator while processing login
        binding.progressBar.visibility = View.VISIBLE

        if (username.isEmpty() || password.isEmpty()) {
            Log.e("LoginActivity", "Empty username or password")
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.GONE  // Hide progress bar if input is invalid
        } else {
            Log.d("LoginActivity", "Attempting login for user: $username")
            loginViewModels.login(username, password)
        }
    }

    private fun navigateToSignUp() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    private fun handleSuccess(data: AuthenticationRes) {
        Log.d("LoginActivity", "Full API Response: $data")

        // Check if the access token is valid
        if (data.access.isNullOrEmpty()) {
            Log.e("LoginActivity", "Access token is null or empty")
            Toast.makeText(this, "Login successful but token is missing", Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.GONE  // Hide progress bar if token is missing
            return
        }

        // Save user credentials securely
        AppPref.get().setLoggedIn(this, true)
        AppEncrypted.get().storeToken(this, data.access) // Store access token securely

        // Save user ID and token in SharedPreferences
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Remove existing token (if any) before saving new data
        editor.remove("USER_TOKEN")
        editor.putInt("USER_ID", data.user.id ?: -1) // Save user ID
        editor.putString("USER_TOKEN", data.access)  // Save token
        editor.apply()

        // Verify token storage
        val storedToken = sharedPreferences.getString("USER_TOKEN", null)
        Log.d("LoginActivity", "Stored Token: $storedToken")

        // Save user credentials
        val username = data.user.username ?: "No Username"
        val email = data.user.email ?: "No Email"

        AppPref.get().apply {
            setUserName(this@LoginActivity, username)
            setEmail(this@LoginActivity, email)
        }

        // Show success message
        Toast.makeText(this, "Sign-In Successful", Toast.LENGTH_LONG).show()

        // Redirect to main activity
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("goToProfile", true) // Optionally pass data to go to profile
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

        // Check if user is already logged in
        if (AppPref.get().isLoggedIn(this)) {
            Log.d("LoginActivity", "User already logged in. Redirecting to main screen.")

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    // Add this function to clear the token and user session data
    // Add this method to your ProfileFragment or wherever the logout occurs
    fun logout() {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear() // Clears all saved data like token and user ID
        editor.apply()

        // Redirect to login activity or wherever necessary
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // Optional: close current activity
    }
}
