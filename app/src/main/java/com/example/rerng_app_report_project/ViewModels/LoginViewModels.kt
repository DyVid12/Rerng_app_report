package com.example.rerng_app_report_project.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rerng_app_report_project.AuthenticationRes
import com.example.rerng_app_report_project.Data_rerngApp.APIResponse
import com.example.rerng_app_report_project.Data_rerngApp.APIState
import com.example.rerng_app_report_project.Data_rerngApp.State
import com.example.rerng_app_report_project.Models_rerngApp.ApiManager
import com.example.rerng_app_report_project.Models_rerngApp.LoginModels
import com.example.rerng_app_report_project.User
import kotlinx.coroutines.launch
import retrofit2.Response

// LoginViewModels.kt
class LoginViewModels : ViewModel() {

    private val _loginState = MutableLiveData<APIState<AuthenticationRes>>()
    val loginState: LiveData<APIState<AuthenticationRes>> get() = _loginState

    private val loginService = ApiManager.getDataService()

    fun login(username: String, password: String) {
        Log.d("LoginViewModel", "Login function called with username: $username and password: $password")

        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Calling API...")
                val response: Response<AuthenticationRes> = loginService.signIn(LoginModels(username, password))

                Log.d("LoginViewModel", "API response received: ${response.code()} - ${response.message()}")

                if (response.isSuccessful && response.body() != null) {
                    val authRes = response.body()!!
                    Log.d("LoginViewModel", "Login Success: $authRes")

                    _loginState.postValue(APIState(State.SUCCESS, authRes))
                } else {
                    Log.e("LoginViewModel", "Login Failed: ${response.code()} - ${response.errorBody()?.string()}")
                    _loginState.postValue(APIState(State.ERROR, null, "Login failed: ${response.message()}"))
                }
            } catch (ex: Exception) {
                Log.e("LoginViewModel", "Exception during API call", ex)
                _loginState.postValue(APIState(State.ERROR, null, ex.localizedMessage ?: "An unknown error occurred"))
            }
        }
    }


}


