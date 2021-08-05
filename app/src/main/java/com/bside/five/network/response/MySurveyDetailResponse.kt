package com.bside.five.network.response

data class MySurveyDetailResponse(val data: Data) : BaseResponse() {
    data class Data(val survey_id: String, val qrcode_url: String, val question: ArrayList<Question>)
    data class Question(
        val question_id: String,
        val no: String,
        val title: String,
        val content: String,
        val image: String
    )
}