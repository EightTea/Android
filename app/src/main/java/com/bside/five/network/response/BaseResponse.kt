package com.bside.five.network.response

import android.text.TextUtils

open class BaseResponse {
    var code: String? = null // 결과 코드.
    var msg: String? = null // 메시지.

    fun isSuccess(): Boolean {
        return !TextUtils.isEmpty(code) && "200".equals(code, ignoreCase = true)
    }
}