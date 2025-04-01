package com.example.rerng_app_report_project

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.rerng_app_report_project.databinding.FragmentProfileBinding
import com.example.rerng_app_report_project.global.AppPref

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    private val activityLoginResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                Log.d("ProfileFragment", "Login successful, updating UI.")
                setUpUi() // Refresh UI after login
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUi()

        binding.btnSignIn.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            activityLoginResult.launch(intent) // Open LoginActivity
        }

        binding.logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun setUpUi() {
        val isLoggedIn = AppPref.get().isLoggedIn(requireContext())
        Log.d("ProfileFragment", "isLoggedIn: $isLoggedIn")

        if (isLoggedIn) {
            showAccount()
        } else {
            showLogInButton()
        }
    }

    private fun showAccount() {
        Log.d("ProfileFragment", "Displaying Account Layout")
        binding.lytAccount.isVisible = true
        binding.lytlogin.isVisible = false

        val username = AppPref.get().getUserName(requireContext()) ?: "Unknown User"
        val email = AppPref.get().getEmail(requireContext()) ?: "No Email"

        binding.usernameTextView.text = "Username: $username"
        binding.emailTextView.text = "Email: $email"
    }

    private fun showLogInButton() {
        Log.d("ProfileFragment", "Displaying Login Button Layout")
        binding.lytAccount.isVisible = false
        binding.lytlogin.isVisible = true
    }

    private fun logout() {
        Log.d("ProfileFragment", "User logged out")
        AppPref.get().setLoggedIn(requireContext(), false)
        AppPref.get().setUserName(requireContext(), "")
        AppPref.get().setEmail(requireContext(), "")

        setUpUi() // Update UI after logout
    }

    override fun onResume() {
        super.onResume()

        val username = AppPref.get().getUserName(requireContext()) // Use binding
        val email = AppPref.get().getEmail(requireContext()) // Use binding
        val gender = AppPref.get().getGender(requireContext()) // Use binding

        // Update the TextViews with the data from AppPref
        binding.usernameTextView.text = " $username"
        binding.emailTextView.text = " $email"
        binding.genderTextView.text = " $gender"
    }





}

