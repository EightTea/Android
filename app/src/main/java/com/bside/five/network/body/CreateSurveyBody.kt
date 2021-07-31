package com.bside.five.network.body

import okhttp3.MultipartBody

data class CreateSurveyBody(var title: String, var content: String, var question: ArrayList<QuestionInfo>) {
    data class QuestionInfo(var content: String, var file: MultipartBody.Part? = null)
}