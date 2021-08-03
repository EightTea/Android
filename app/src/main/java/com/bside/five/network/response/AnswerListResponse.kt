package com.bside.five.network.response

import java.sql.Timestamp

data class AnswerListResponse(val data: Data) : BaseResponse() {
    data class Data(val answer: ArrayList<Answer>, val is_more: Boolean)
    data class Answer(
        val answer_id: String,
        val answer_no: String,
        val comment: String,
        val date: Timestamp
    )
}