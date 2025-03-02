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

class AdventureModels : ViewModel() {
    private val _dataState = MutableLiveData<APIState<List<Movy>>>()
    val dataState: LiveData<APIState<List<Movy>>> get() = _dataState

    fun loadAdventureData() {
        viewModelScope.launch {
            try {
                val response = ApiManager.getMovies()
                Log.d("API Response", "✅ Raw Response: $response")

                if (response.status == "success") {
                    val filterData = response.movies?.filter { movie ->
                        movie.genres.any { genre -> genre.equals("Adventure", ignoreCase = true) }
                    }
                    _dataState.postValue(APIState(State.SUCCESS, filterData))
                } else {
                    _dataState.postValue(APIState(State.ERROR))
                }
            } catch (e: Exception) {
                _dataState.postValue(APIState(State.ERROR))
            }
        }
    }
}

