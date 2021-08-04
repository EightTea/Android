package com.bside.five.network.response

data class QrUrlResponse(val data: Data) : BaseResponse(){
    data class Data(val qrcode_url: String)
}