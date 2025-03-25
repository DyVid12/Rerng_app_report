package com.example.rerng_app_report_project.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rerng_app_report_project.Data_rerngApp.APIState
import com.example.rerng_app_report_project.Data_rerngApp.State
import com.example.rerng_app_report_project.Models_rerngApp.ApiManager
import com.example.rerng_app_report_project.Models_rerngApp.Movy
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SearchViewModels : ViewModel() {
    private val _dataState = MutableLiveData<APIState<List<Movy>>>()
    val dataState: LiveData<APIState<List<Movy>>> get() = _dataState

    fun loadSearchData(query: String) {
        _dataState.postValue(APIState(State.ERROR))  // Show loading before making the request
        viewModelScope.launch {
            try {
                val apiService = ApiManager.getDataService()
                val categoryToSend = query

                Log.d("API_DEBUG", "Searching for Category: $categoryToSend")

                val response = apiService.searchMovies(categoryToSend)

                // Log the actual API response
                Log.d("API_DEBUG", "Full API Response: ${response.movies_by_category?.toString() ?: response.movies?.toString() ?: "Empty"}")

                val allMovies = response.movies_by_category?.flatMap { (category, movies) ->
                    Log.d("API_DEBUG", "Category: $category | Movies Count: ${movies.size}")
                    movies.forEach { movie ->
                        Log.d("API_DEBUG", "Movie: ${movie.title} | Overview: ${movie.overview ?: "NULL"}")
                    }
                    movies
                } ?: response.movies ?: emptyList()

                if (allMovies.isEmpty()) {
                    Log.e("API_ERROR", "No movies found in the response")
                    _dataState.postValue(APIState(State.ERROR, null, "No movies found"))
                } else {
                    _dataState.postValue(APIState(State.SUCCESS, allMovies))
                }
            } catch (e: HttpException) {
                Log.e("API_ERROR", "HTTP ${e.code()} - ${e.message()}")
                _dataState.postValue(APIState(State.ERROR, null, "HTTP Error: ${e.message()}"))
            } catch (e: Exception) {
                Log.e("SearchViewModels", "Error loading search data", e)
                _dataState.postValue(APIState(State.ERROR, null, "Network Error: ${e.message}"))
            }
        }
    }

    fun loadSearchDataByTitle(title: String) {
        _dataState.postValue(APIState(State.ERROR))  // Set error before starting the call
        viewModelScope.launch {
            try {
                val apiService = ApiManager.getDataService()
                val response = apiService.searchMoviesByTitle(title)  // Use 'title' parameter

                val movies = response.movies ?: emptyList()

                if (movies.isEmpty()) {
                    _dataState.postValue(APIState(State.ERROR, errorMessage = "No movies found"))
                } else {
                    _dataState.postValue(APIState(State.SUCCESS, movies = movies))
                }
            } catch (e: HttpException) {
                _dataState.postValue(APIState(State.ERROR, errorMessage = "HTTP Error: ${e.message()}"))
            } catch (e: Exception) {
                _dataState.postValue(APIState(State.ERROR, errorMessage = "Network Error: ${e.message}"))
            }
        }
    }



}
