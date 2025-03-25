package com.example.rerng_app_report_project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.rerng_app_report_project.Data_rerngApp.State
import com.example.rerng_app_report_project.ViewModels.LoginViewModels
import com.example.rerng_app_report_project.databinding.ActivityLoginBinding
import com.example.rerng_app_report_project.global.AppEncrypted
import com.example.rerng_app_report_project.global.AppPref

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModels by viewModels()

    // Register the result launcher for login activity
    private val activityLoginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                finish()  // Close LoginActivity if user logged in
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        loadBackgroundImage() // Load the background image here
        setupListeners()
        observeViewModel()
    }

    private fun setupUi() {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun loadBackgroundImage() {
        Glide.with(this)
            .load(R.drawable.hero_image) // Your background image resource
            .override(1080, 1920) // Resize to prevent memory overload
            .into(binding.backgroundImage) // Your ImageView
    }

    private fun setupListeners() {
        binding.loginButton.setOnClickListener {
            attemptLogin()
        }
    }

    private fun observeViewModel() {
        loginViewModel.loginState.observe(this) { state ->
            binding.progressBar.visibility = View.GONE
            when (state.state) {
                State.SUCCESS -> {
                    handleSuccess(state.movies!!)  // Ensure movies field is properly handled
                }
                State.ERROR -> {
                    showAlert("Sign In", state.errorMessage ?: "An unknown error occurred.")
                }
            }
        }
    }

    private fun attemptLogin() {
        val username = binding.usernameEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Invalid Input", "Please enter both username and password!")
        } else {
            binding.progressBar.visibility = View.VISIBLE
            loginViewModel.login(username, password)
        }
    }

    private fun handleSuccess(authRes: AuthenticationRes) {
        AppPref.get().setLoggedIn(this, true)
        val token = authRes.access ?: ""
        if (token.isNotEmpty()) {
            AppEncrypted.get().storeToken(this, token)
        }

        val username = binding.usernameEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        AppPref.get().apply {
            setUserName(this@LoginActivity, username)
            setPassword(this@LoginActivity, password)
        }

        Toast.makeText(this, "Sign In Successful", Toast.LENGTH_LONG).show()

        // Set result to notify ProfileFragment
        setResult(Activity.RESULT_OK)
        finish() // Close LoginActivity
    }


    override fun onResume() {
        super.onResume()
        if (AppPref.get().isLoggedIn(this)) {
            setResult(Activity.RESULT_OK) // Notify ProfileFragment about login success
            finish() // Close LoginActivity
        }
    }

    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
