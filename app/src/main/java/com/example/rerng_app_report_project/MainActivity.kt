package com.example.rerng_app_report_project

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.rerng_app_report_project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val txtBackToList = findViewById<TextView>(R.id.txtBackToList)
        txtBackToList?.setOnClickListener {
            replaceFragment(HomeFragment())
        }

        replaceFragment(HomeFragment())

        // Set up bottom navigation
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

}
