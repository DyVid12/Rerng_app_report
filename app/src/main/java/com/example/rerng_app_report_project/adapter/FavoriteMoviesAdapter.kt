package com.example.rerng_app_report_project.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rerng_app_report_project.Models_rerngApp.Movie
import com.example.rerng_app_report_project.R

class FavoriteMoviesAdapter(private var movies: List<Movie>) : RecyclerView.Adapter<FavoriteMoviesAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_data, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = movies.size

    fun updateData(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged() // Notify adapter to refresh the list
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val movieTitle: TextView = itemView.findViewById(R.id.movieTitle)
        private val moviePoster: ImageView = itemView.findViewById(R.id.moviePoster)

        fun bind(movie: Movie) {
            movieTitle.text = movie.title
            Glide.with(itemView.context)
                .load("http://10.0.2.2:8000/${movie.poster}") // Replace with your actual backend URL
                .into(moviePoster)
        }
    }
}
