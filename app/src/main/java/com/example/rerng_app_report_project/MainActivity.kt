package com.example.rerng_app_report_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.rerng_app_report_project.databinding.ActivityMainBinding
import com.example.rerng_app_report_project.global.AppPref

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val goToProfile = intent.getBooleanExtra("goToProfile", false)

        if (savedInstanceState == null) { // Prevent replacing fragment on rotation
            if (goToProfile) {
                replaceFragment(ProfileFragment())
                binding.bottomNavigation.selectedItemId = R.id.navigation_profile
            } else {
                replaceFragment(HomeFragment())
            }
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> replaceFragment(HomeFragment())
                R.id.navigation_favorite -> replaceFragment(FavoriteFragment())
                R.id.navigation_about -> replaceFragment(AboutUsFragment())
                R.id.navigation_profile -> replaceFragment(ProfileFragment())
                R.id.navigation_search -> replaceFragment(SearchFragment())
            }
            true
        }
    }



    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment) // Ensure `container` is your FrameLayout in activity_main.xml
            .commit()
    }

    private fun logout() {
        // Clear login status and stored user credentials
        AppPref.get().apply {
            setLoggedIn(this@MainActivity, false)
            setUserName(this@MainActivity, "")
            setPassword(this@MainActivity, "")
        }

        // Display logout success message
        Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show()

        // Navigate the user back to the LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

}
