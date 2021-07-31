package com.bside.five.network.response

data class SurveyResponse(val data: Data) : BaseResponse() {
    data class Data(val survey_id: String, val qrcode_url: String)
}