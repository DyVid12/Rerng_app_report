package com.example.rerng_app_report_project.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rerng_app_report_project.Models_rerngApp.Movy
import com.example.rerng_app_report_project.MovieDetailActivity
import com.example.rerng_app_report_project.databinding.ViewHolderDataBinding
import com.squareup.picasso.Picasso

class RomanceAdapter : ListAdapter<Movy, RomanceAdapter.RomanceViewHolder>(RomanceDiffUtil) {

    class RomanceViewHolder(private val binding: ViewHolderDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindActionDataMovie(movy: Movy) {
            if (!movy.poster.isNullOrEmpty()) {
                val baseUrl = "http://10.0.2.2:8000/" // Replace with actual base URL
                val posterUrl = if (movy.poster.startsWith("http")) movy.poster else "$baseUrl${movy.poster}"

                Log.d("Picasso", "Loading Image URL: $posterUrl")

                Picasso.get().load(posterUrl)
                    .into(binding.image1, object : com.squareup.picasso.Callback {
                        override fun onSuccess() {
                            Log.d("Picasso", "Image loaded successfully")
                        }

                        override fun onError(e: Exception?) {
                            Log.e("Picasso Error", "Failed to load image: ${e?.message}")
                        }
                    })
                binding.image1.setOnClickListener { v ->
                    val context = v.context
                    val intent = Intent(context, MovieDetailActivity::class.java).apply {
                        putExtra("poster", posterUrl) // ✅ Use correct URL
                        putExtra("release_date", movy.release_date)
                        putExtra("title", movy.title)
                        putExtra("overview", movy.movie_detail.overview)
                        putExtra("rating", movy.rating)
                        putExtra("trailer_url", movy.movie_detail.trailer_url)
                        val ratingValue = movy.movie_detail.rating ?: movy.rating ?: 0.0 // Default 0.0 if null
                        Log.d("RomanceAdapter ", "Passing Rating: $ratingValue")
                        putExtra("rating", ratingValue.toString()) // Convert to string before passing
                    }
                    context.startActivity(intent)
                }
            } else {
                Log.e("Picasso Error", "Poster URL is empty or null!")
            }

            binding.tittlemovies.text = movy.title
            binding.relistdate.text = "Release Date: ${movy.release_date}"
            binding.movieRating.text = "⭐ ${movy.rating ?: "N/A"} /10"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RomanceViewHolder {
        val binding = ViewHolderDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RomanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RomanceViewHolder, position: Int) {
        holder.bindActionDataMovie(getItem(position))
    }
}

object RomanceDiffUtil : DiffUtil.ItemCallback<Movy>() {
    override fun areItemsTheSame(oldItem: Movy, newItem: Movy): Boolean {
        return oldItem.id == newItem.id // Compare IDs instead of full objects
    }

    override fun areContentsTheSame(oldItem: Movy, newItem: Movy): Boolean {
        return oldItem == newItem
    }
}
