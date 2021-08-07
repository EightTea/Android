package com.bside.five.base

import androidx.multidex.MultiDexApplication
import com.kakao.sdk.common.KakaoSdk

class FiveApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        // Kakao SDK 초기화
        KakaoSdk.init(this, "1b4b19261159f94bca9e1a52db883da5")
    }
}