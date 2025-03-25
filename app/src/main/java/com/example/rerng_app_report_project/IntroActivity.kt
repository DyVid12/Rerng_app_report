package com.example.rerng_app_report_project

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rerng_app_report_project.databinding.IntroActivityBinding

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: IntroActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = IntroActivityBinding.inflate(layoutInflater) // ViewBinding initialization
        setContentView(binding.root)

        binding.btnGetStarted.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Close IntroActivity
        }
    }
}
