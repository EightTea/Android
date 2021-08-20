package com.bside.five.network.response

import com.google.gson.annotations.SerializedName

data class SelectUserResponse(val data: Data) : BaseResponse() {
    data class Data(@SerializedName("user") val isUser: Boolean)

}