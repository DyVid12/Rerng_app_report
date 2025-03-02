//package com.example.rerng_app_report_project
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.navigation.fragment.navArgs
//import com.bumptech.glide.Glide
//import com.example.rerng_app_report_project.databinding.FragmentMovieDetailBinding
//
//class MovieDetailFragment : Fragment() {
//    private lateinit var binding: FragmentMovieDetailBinding
//    private val args: MovieDetailFragmentArgs by navArgs()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val movie = args.movie
//
//        // Load movie data
//        binding.movieTitle.text = movie.title
//        binding.movieReleaseDate.text = "Release Date: ${movie.release_date}"
//        binding.movieRating.text = "⭐ ${movie.rating}/10"
//        binding.movieDescription.text = movie.overview
//
//        Glide.with(this).load(movie.imageUrl).into(binding.moviePoster)
//
//        // Handle Watch Trailer button
//        binding.watchTrailerButton.setOnClickListener {
//            if (!movie.trailerUrl.isNullOrEmpty()) {
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(movie.trailerUrl))
//                startActivity(intent)
//            } else {
//                Toast.makeText(requireContext(), "Trailer not available", Toast.LENGTH_SHORT).show()
//            }
//        }
////
//        // Handle Add to Watchlist button
//        binding.addToWatchlistButton.setOnClickListener {
//            Toast.makeText(requireContext(), "Added to Watchlist!", Toast.LENGTH_SHORT).show()
//        }
//
//        // Show Reviews (if available)
//        if (movie.reviews.isNotEmpty()) {
//            binding.reviewsContainer.visibility = View.VISIBLE
//            val review = movie.reviews.first()
//            binding.reviewUser.text = "User: ${review.user}"
//            binding.reviewRating.text = "⭐ ${review.rating}/10"
//            binding.reviewComment.text = review.comment
//        }
//    }
//}
