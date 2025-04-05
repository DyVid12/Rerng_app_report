package com.example.rerng_app_report_project.global

import android.content.Context
import android.content.SharedPreferences

class AppPref private constructor() {

    fun isLoggedIn(context: Context): Boolean {
        return getPref(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setLoggedIn(context: Context, value: Boolean) {
        getPref(context).edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()
    }
    fun setUserId(context: Context, value: Int) {
        getPref(context).edit().putInt(KEY_USER_ID, value).apply()
    }

    fun getUserId(context: Context): Int {
        return getPref(context).getInt(KEY_USER_ID, -1) // Return -1 if not set
    }

    fun setUserName(context: Context, value: String) {
        getPref(context).edit().putString(KEY_USER_NAME, value).apply()
    }

    fun setPassword(context: Context, value: String) {
        getPref(context).edit().putString(KEY_PASSWORD, value).apply()
    }

    fun setEmail(context: Context, value: String) {
        getPref(context).edit().putString(KEY_EMAIL, value).apply()
    }

    fun setGender(context: Context, value: String?) {
        getPref(context).edit().putString(KEY_GENDER, value ?: "Not Available").apply()
    }


    fun getUserName(context: Context): String {
        return getPref(context).getString(KEY_USER_NAME, "") ?: ""
    }

    fun getEmail(context: Context): String {
        return getPref(context).getString(KEY_EMAIL, "") ?: ""
    }

    fun getGender(context: Context): String {
        return getPref(context).getString(KEY_GENDER, "") ?: ""
    }

    private fun getPref(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }


    companion object {
        private val PREF_NAME = "EncryptedPrefs"
        private const val KEY_IS_LOGGED_IN = "IsLoggIn"
        private const val KEY_USER_NAME = "UserName"
        private const val KEY_USER_ID = "UserId"
        private const val KEY_PASSWORD = "Password"
        private const val KEY_EMAIL = "Email"
        private const val KEY_GENDER = "Gender"

        private var instance: AppPref? = null

        fun get(): AppPref {
            if (instance == null) {
                instance = AppPref()
            }
            return instance!!
        }
    }
}
