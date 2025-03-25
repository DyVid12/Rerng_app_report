package com.example.rerng_app_report_project.global

import android.content.Context
import android.content.SharedPreferences

class AppPref private constructor(){


    fun isLoggedIn(context: Context): Boolean{
        return getPref(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setLoggedIn(context: Context ,value: Boolean){
        getPref(context).edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()
    }

    fun setUserName(context: Context, value: String) {
        getPref(context).edit().putString(KEY_USER_NAME, value).apply()
    }

    fun setPassword(context: Context, value: String) {
        getPref(context).edit().putString(KEY_PASSWORD, value).apply()
    }


    fun getUserName(context: Context): String {
        return getPref(context).getString(KEY_USER_NAME, "") ?: ""
    }

    fun getPassword(context: Context): String {
        return getPref(context).getString(KEY_PASSWORD, "") ?: ""
    }

    private fun getPref(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    companion object{
        private val PREF_NAME = "EncryptedPrefs"
        private const val KEY_IS_LOGGED_IN = "IsLoggIn"
        private const val KEY_USER_NAME = "UserName"
        private const val KEY_PASSWORD = "Password"

        private var instance: AppPref? = null

        fun get(): AppPref{
            if(instance == null){
                instance = AppPref()
            }
            return instance!!
        }
    }
}