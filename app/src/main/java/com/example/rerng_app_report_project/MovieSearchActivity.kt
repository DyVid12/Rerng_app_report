//package com.example.rerng_app_report_project
//
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.util.Log
//import android.widget.EditText
//import android.widget.Button
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.rerng_app_report_project.Models_rerngApp.Movy
//import com.example.rerng_app_report_project.Models_rerngApp.MovieDetail
//import com.example.rerng_app_report_project.adapter.MovieAdapter
//
//class MovieSearchActivity : AppCompatActivity() {
//
//    private lateinit var searchEditText: EditText
//    private lateinit var searchButton: Button
//    private lateinit var movieRecyclerView: RecyclerView
//    private lateinit var movieAdapter: MovieAdapter
//    private var movieList: MutableList<Movy> = mutableListOf()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_home)
//
//        // Initialize Views
//        searchEditText = findViewById(R.id.searchEditText)
//        searchButton = findViewById(R.id.searchButton)
//        movieRecyclerView = findViewById(R.id.recyclerSearchResults)
//
//        // Setup RecyclerView
//        movieRecyclerView.layoutManager = LinearLayoutManager(this)
//        movieAdapter = MovieAdapter()
//        movieRecyclerView.adapter = movieAdapter
//
//        // Load Movies (Ensure movies are available before filtering)
//        loadMovies()
//
//        // Handle Search Button Click
//        searchButton.setOnClickListener { filterMovies() }
//
//        // Listen for text input in search field
//        searchEditText.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                filterMovies()
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        })
//    }
//
//    private fun loadMovies() {
//        movieList = mutableListOf(
//            Movy(1, "VENOM", "2018-10-03", "https://imageurl.com/venom.jpg", 7.5f, listOf("Action"),
//                MovieDetail("VENOM", "2018-10-03", "A symbiotic relationship...", "https://imageurl.com/venom.jpg", "https://www.youtube.com/watch?v=u9Mv98Gr5pY")),
//            Movy(2, "Titanic", "1997-12-19", "https://imageurl.com/titanic.jpg", 8.2f, listOf("Romance"),
//                MovieDetail("Titanic", "1997-12-19", "A tragic love story...", "https://imageurl.com/titanic.jpg", "https://www.youtube.com/watch?v=kVrqfYjkTdQ")),
//            Movy(3, "Avatar", "2009-12-18", "https://imageurl.com/avatar.jpg", 8.0f, listOf("Sci-Fi"),
//                MovieDetail("Avatar", "2009-12-18", "An epic journey...", "https://imageurl.com/avatar.jpg", "https://www.youtube.com/watch?v=5PSNL1qE6VY"))
//        )
//
//        Log.d("LoadMovies", "Movies loaded: ${movieList.size}")
//
//        movieAdapter.submitList(movieList.toList()) // Ensure a new list is passed
//    }
//
//    private fun filterMovies() {
//        val query = searchEditText.text.toString().trim().lowercase()
//
//        if (query.isEmpty()) {
//            movieAdapter.submitList(movieList.toList()) // Show all movies if search is empty
//            return
//        }
//
//        val filteredMovies = movieList.filter { it.title.lowercase().contains(query) }.toMutableList()
//
//        Log.d("FilterMovies", "Query: $query, Found: ${filteredMovies.size}")
//
//        movieAdapter.submitList(filteredMovies.toList()) // Ensure a new list is passed
//    }
//}
