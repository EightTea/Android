package com.bside.five.network.response

data class UserResponse(val data: Data) : BaseResponse() {
    data class Data(val access_token: String)
}


