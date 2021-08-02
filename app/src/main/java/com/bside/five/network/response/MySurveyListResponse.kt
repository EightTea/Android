package com.bside.five.network.response

import java.sql.Timestamp

data class MySurveyListResponse(val data: Data) : BaseResponse() {
    data class Data(val survey_list: ArrayList<MySurveyInfo>)
    data class MySurveyInfo(
        val survey_id: Int,
        val title: String,
        val status: String,
        val start_date: Timestamp,
        val end_date: Timestamp,
        val answer_cnt: Int
    )
}