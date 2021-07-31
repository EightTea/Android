package com.bside.five.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object FivePreference {
    private const val PREF_USER_ID = "user_id"
    private const val PREF_TOKEN = "access_token"

//    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//
//
//
    /**
     * 유저 아이디
     */
    fun setUserId(context: Context, id: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREF_USER_ID, id).apply()
    }

    fun getUserId(context: Context): String {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_USER_ID, "") ?: ""
    }

    /**
     * 유저 토큰
     */
    fun setAccessToken(context: Context, token: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREF_TOKEN, token).apply()
    }

    fun getAccessToken(context: Context): String {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_TOKEN, "") ?: ""
    }
}