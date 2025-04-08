package com.example.rerng_app_report_project.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rerng_app_report_project.Models_rerngApp.Movie
import com.example.rerng_app_report_project.MovieDetailActivity
import com.example.rerng_app_report_project.databinding.ViewHolderDataBinding
import com.squareup.picasso.Picasso

class FavoriteMoviesAdapter :
    ListAdapter<Movie, FavoriteMoviesAdapter.MovieViewHolder>(FavoriteMoviesDiffUtil) {

    class MovieViewHolder(private val binding: ViewHolderDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindMovie(movie: Movie) {
            val baseUrl = "http://10.0.2.2:8000/"
            val posterUrl = if (movie.poster.startsWith("http")) movie.poster else "$baseUrl${movie.poster}"

            // Log movie data for debugging
            Log.d("FavoriteMoviesAdapter", "Binding movie: ${movie.title}")
            Log.d("FavoriteMoviesAdapter", "Poster URL: $posterUrl")

            // Load poster using Picasso
            Picasso.get().load(posterUrl).into(binding.image1)

            // Ensure that overview is not null or empty
            val overview = movie.overview?.takeIf { it.isNotEmpty() } ?: "No overview available"

            val ratingValue = movie.rating ?: 0.0

            // Set the data in the respective views
            binding.tittlemovies.text = movie.title
            binding.relistdate.text = movie.release_date ?: "Unknown"
            binding.movieRating.text = "⭐ ${ratingValue} /10"

            // Set up click listener to navigate to MovieDetailActivity
            // Inside bindMovie method of your Adapter
            binding.root.setOnClickListener { v ->
                val context = v.context
                val intent = Intent(context, MovieDetailActivity::class.java).apply {
                    putExtra("MOVIE_ID", movie.id)
                    putExtra("title", movie.title)
                    putExtra("release_date", movie.release_date)
                    putExtra("overview", movie.overview ?: "No overview available") // Accessing overview
                    putExtra("rating", movie.rating.toString())
                    putExtra("poster", movie.poster)
                    putExtra("trailer_url", movie.trailer_url ?: "") // Accessing trailer_url
                }
                context.startActivity(intent)
            }





        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ViewHolderDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bindMovie(movie)
    }
}

object FavoriteMoviesDiffUtil : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id // Compare IDs instead of full objects
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}
