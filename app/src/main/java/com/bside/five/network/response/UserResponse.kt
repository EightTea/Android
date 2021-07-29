package com.bside.five.network.response

data class UserResponse(val data: String) : BaseResponse() {
    data class Data(val access_token: String)

    // FIXME : 파싱 수정하면 val data: String -> Data 클래스로 수정
}


