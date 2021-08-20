package com.bside.five.util

import com.bside.five.base.FiveApp

object FivePreference {
    private const val PREF_USER_ID = "user_id"
    private const val PREF_TOKEN = "access_token"

    /**
     * 유저 아이디
     */
    fun setUserId(id: String) {
        FiveApp.preferences.edit().putString(PREF_USER_ID, id).apply()
    }

    fun getUserId(): String {
        return FiveApp.preferences.getString(PREF_USER_ID, "") ?: ""
    }

    /**
     * 유저 토큰
     */
    fun setAccessToken(token: String) {
        FiveApp.preferences.edit().putString(PREF_TOKEN, token).apply()
    }

    fun getAccessToken(): String {
        return FiveApp.preferences.getString(PREF_TOKEN, "") ?: ""
    }
}