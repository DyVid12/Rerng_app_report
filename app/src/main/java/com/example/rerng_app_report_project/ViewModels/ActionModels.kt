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

class ActionModels : ViewModel() {  // Ensure ViewModel name is correct

    private val _dataState = MutableLiveData<APIState<List<Movy>>>()
    val dataState: LiveData<APIState<List<Movy>>> get() = _dataState

    fun loadActionData() {  // Fixed method name
        viewModelScope.launch {
            try {
                val response = ApiManager.getMovies()
                Log.d("API Response", "✅ Raw Response: $response")

                if (response.status == "success") {
                    Log.d("API Response", "✅ Movies received: ${response.movies}")

                    val filterData = response.movies?.filter { movie ->
                        movie.genres.any { genre -> genre.equals("Action", ignoreCase = true) }
                    }
                    _dataState.postValue(APIState(State.SUCCESS, filterData))
                } else {
                    Log.e("API Response", "❌ API returned empty or null data! Message: ${response.message}")
                    _dataState.postValue(APIState(State.ERROR))
                }
            } catch (e: Exception) {
                Log.e("API Response", "❌ API Call Failed: ${e.message}", e)
                _dataState.postValue(APIState(State.ERROR))
            }
        }
    }
}
