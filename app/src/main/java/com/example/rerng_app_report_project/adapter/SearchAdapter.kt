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
import com.example.rerng_app_report_project.databinding.ViewHolderSearchDataBinding
import com.squareup.picasso.Picasso

class SearchAdapter : ListAdapter<Movy, SearchAdapter.SearchViewHolder>(SearchDiffUtil) {

    class SearchViewHolder(private val binding: ViewHolderSearchDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindSearchDataMovie(movy: Movy) {
            val baseUrl = "http://10.0.2.2:8000/"
            val posterUrl = if (movy.poster.startsWith("http")) movy.poster else "$baseUrl${movy.poster}"

            // Log movie data
            Log.d("SearchAdapter", "Binding movie: ${movy.title}")
            Log.d("SearchAdapter", "Poster URL: $posterUrl")

            Picasso.get().load(posterUrl).into(binding.poster)

            // Ensure overview is not null
            val overview = movy.movie_detail?.overview?.takeIf { it.isNotEmpty() }
                ?: movy.overview?.takeIf { it.isNotEmpty() }
                ?: "No overview available"

            val ratingValue = movy.movie_detail?.rating ?: movy.rating ?: 0.0

            // Log overview to debug
            Log.d("SearchAdapter", "Movie: ${movy.title} | Overview: $overview")

            // Setting up onClick listener for the poster
            binding.poster.setOnClickListener { v ->
                val context = v.context
                val intent = Intent(context, MovieDetailActivity::class.java).apply {
                    putExtra("poster", posterUrl)
                    putExtra("release_date", movy.release_date)
                    putExtra("title", movy.title)
                    putExtra("overview", overview)  // ✅ Ensure correct overview is passed
                    putExtra("trailer_url", movy.movie_detail?.trailer_url ?: "")
                    putExtra("rating", ratingValue.toString())
                    putExtra("movie_id", movy.id)
                }
                context.startActivity(intent)
            }

            binding.tittleposter.text = movy.title
            binding.relistdateposter.text = "Release Date: ${movy.release_date}"
            binding.movieratingposter.text = "⭐ ${ratingValue} /10"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ViewHolderSearchDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bindSearchDataMovie(movie)
    }
}

object SearchDiffUtil : DiffUtil.ItemCallback<Movy>() {
    override fun areItemsTheSame(oldItem: Movy, newItem: Movy): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Movy, newItem: Movy): Boolean = oldItem == newItem
}
