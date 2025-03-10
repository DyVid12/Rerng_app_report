package com.example.rerng_app_report_project


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.rerng_app_report_project.LoginActivity
import com.example.rerng_app_report_project.databinding.FragmentProfileBinding
import com.example.rerng_app_report_project.global.AppEncrypted
import com.example.rerng_app_report_project.global.AppPref

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    private val activityLoginResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            // The user logged in successfully, so update the UI.
            Log.d("SettingFragment", "Login successful, updating UI.")
            setUpUi() // Refresh UI after login
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUi()
        setUpListener()
    }

    private fun setUpUi(){
        if(AppPref.get().isLoggedIn(requireContext())){
            showAccount()
        }else{
            showLogInButton()
        }
    }

    private fun setUpListener(){
        binding.btnSignIn.setOnClickListener { onLogInButtonClick() }

        // Handle the logout button click
       // binding.btnLogOut.setOnClickListener { onLogOutButtonClick() }
    }

    private fun onLogInButtonClick(){
        val intent = Intent(requireContext(), LoginActivity::class.java)
        activityLoginResult.launch(intent)
    }

    private fun onLogOutButtonClick() {
        // Clear user data from AppPref
        AppPref.get().setLoggedIn(requireContext(), false)
        AppPref.get().setUserName(requireContext(), "")
        AppPref.get().setPassword(requireContext(), "")

        Toast.makeText(context, "LogOut Successful", Toast.LENGTH_LONG).show()

        // Clear access token from AppEncryptedPref
        AppEncrypted.get().removeToken(requireContext())

        // Update the UI
        showLogInButton()
    }

    override fun onResume() {
        super.onResume()
        // Check login state and update UI
        if (AppPref.get().isLoggedIn(requireContext())) {
            showAccount()
        } else {
            showLogInButton()
        }
    }

    private fun showAccount() {
        binding.lytAccount.isVisible = true
        binding.lytlogin.isVisible = false

    }

    private fun showLogInButton(){
        binding.lytAccount.isVisible = false
        binding.lytlogin.isVisible = true
    }
}