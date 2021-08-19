package com.bside.five.network.response

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class AnswerListResponse(val data: Data) : BaseResponse() {
    data class Data(val answer: ArrayList<Answer>, val is_more: Boolean)
    data class Answer(
        @SerializedName("id") val answer_id: String,
        val surveyId: String,
        val questionId: String,
        val comment: String,
        val date: String,
        var isMore: Boolean = false,
        var page: Int = 0
    )
}