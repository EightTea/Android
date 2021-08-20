package com.bside.five.base

import android.content.SharedPreferences
import androidx.multidex.MultiDexApplication
import androidx.preference.PreferenceManager
import com.kakao.sdk.common.KakaoSdk

class FiveApp : MultiDexApplication() {

    companion object {
        lateinit var preferences: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()

        preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        // Kakao SDK 초기화
        KakaoSdk.init(this, "1b4b19261159f94bca9e1a52db883da5")
    }
}