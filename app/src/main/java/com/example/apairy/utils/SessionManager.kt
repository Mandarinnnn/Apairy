package com.example.apairy.utils

import android.content.Context
import android.content.SharedPreferences
import kotlin.math.E

class SessionManager(val context: Context) {

    private val sharedPreferences: SharedPreferences = context
        .getSharedPreferences("SessionPreferences", Context.MODE_PRIVATE)


    companion object {
        private const val TOKEN_KEY = "TOKEN_KEY"
        private const val EMAIL = "EMAIL"
    }

    fun saveToken(token: String){
        val editor = sharedPreferences.edit()
        editor.putString(TOKEN_KEY, token)
        editor.apply()
    }

    fun saveEmail(email: String){
        val editor = sharedPreferences.edit()
        editor.putString(EMAIL, email)
        editor.apply()
    }


    fun getToken(): String?{
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    fun getEmail(): String?{
        return sharedPreferences.getString(EMAIL, null)
    }

    fun logout(){
        val editor = sharedPreferences.edit()
        editor.remove(TOKEN_KEY)
        editor.remove(EMAIL)
        editor.apply()
    }

}