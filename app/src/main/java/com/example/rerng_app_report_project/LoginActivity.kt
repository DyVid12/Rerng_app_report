package com.example.rerng_app_report_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
                    Log.d("Sign In", errorMessage)
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun attemptSignIn() {
        val username = binding.usernameEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Log.d("Invalid Input", "Please enter both username and password!")
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
        } else {
            loginViewModels.login(username, password)
        }
    }

    private fun navigateToSignUp() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    private fun handleSuccess(data: AuthenticationRes) {
        Log.d("LoginActivity", "Received Access Token: ${data.access}")

        // Check if the access token is null or empty
        if (data.access.isNullOrEmpty()) {
            Log.e("LoginActivity", "Access token is null or empty")
            Toast.makeText(this, "Login successful but token is missing", Toast.LENGTH_SHORT).show()
            return  // Early exit to avoid calling storeToken with a null token
        }

        // Proceed to store the access token if it's valid
        AppPref.get().setLoggedIn(this, true)
        AppEncrypted.get().storeToken(this, data.access)

        // Save user credentials to preferences
        val username = binding.usernameEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        // Handle possible null values for email and gender
        val email = data.user.email ?: "No Email"  // Default value if email is null
        val gender = data.user.gender ?: "Not Available"  // Default value if gender is null

        // Save username, password, email, and gender in AppPref
        AppPref.get().apply {
            setUserName(this@LoginActivity, username)
            setPassword(this@LoginActivity, password)
            setEmail(this@LoginActivity, email)
            setGender(this@LoginActivity, gender)  // Pass a non-null value for gender
        }

        Toast.makeText(this, "Sign-In Successful", Toast.LENGTH_LONG).show()

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("goToProfile", true)
        }
        startActivity(intent)
    }






    override fun onResume() {
        super.onResume()

        // Check if the user is already logged in
        if (AppPref.get().isLoggedIn(this)) {
            Log.d("LoginActivity", "User already logged in. Redirecting to main screen.")

            // Navigate to MainActivity or the screen containing ProfileFragment
            val intent = Intent(this, MainActivity::class.java) // Change MainActivity to your actual main screen
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

}