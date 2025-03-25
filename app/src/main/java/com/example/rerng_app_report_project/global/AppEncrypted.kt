package com.example.rerng_app_report_project.global

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class AppEncrypted private constructor(){

    fun storeToken(context: Context, accessToken: String){
        getPref(context).edit().putString(KEY_TOKEN, accessToken).apply()

    }

    fun getToken(context: Context): String?{
        return getPref(context).getString(KEY_TOKEN, null)
    }

    fun removeToken(context: Context) {
        val sharedPreferences = getPref(context)
        if (sharedPreferences.contains(KEY_TOKEN)) {
            sharedPreferences.edit().remove(KEY_TOKEN).apply()
            Log.d("AppEncrypted", "Access token removed successfully.")
        } else {
            Log.d("AppEncrypted", "No access token found to remove.")
        }
    }

    private fun getPref(context: Context): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            PREF_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

    }

    companion object {
        private val PREF_NAME = "EncryptedPrefsEnc"
        private const val KEY_TOKEN = "accessToken"
        private var instance: AppEncrypted? = null

        fun get(): AppEncrypted {
            if(instance == null) {
                instance = AppEncrypted()
            }

            return instance!!
        }

    }

}