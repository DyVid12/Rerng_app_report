package com.example.rerng_app_report_project

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class MovieDetailFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val txtBackToList = view.findViewById<TextView>(R.id.txtBackToList)
        txtBackToList?.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment()) // Replace with the home fragment
                .commit()
        }
    }
}

