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

            Picasso.get().load(posterUrl).into(binding.image1)
            binding.tittlemovies.text = movie.title
            binding.relistdate.text = movie.release_date
            binding.movieRating.text = "⭐ ${movie.rating} /10"

            // Use movie_detail for the overview and trailer_url
            binding.root.setOnClickListener { v ->
                val context = v.context
                val intent = Intent(context, MovieDetailActivity::class.java).apply {
                    putExtra("MOVIE_ID", movie.id)
                    putExtra("title", movie.title)
                    putExtra("release_date", movie.release_date)
                    putExtra("overview", movie.overview ?: "No overview available")
                    putExtra("rating", movie.rating.toString())
                    putExtra("poster", posterUrl)
                    putExtra("trailer_url", movie.trailer_url ?: "")
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
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}
