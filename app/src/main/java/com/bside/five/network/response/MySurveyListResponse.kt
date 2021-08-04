package com.bside.five.network.response

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class MySurveyListResponse(val data: Data) : BaseResponse() {
    data class Data(val survey_list: ArrayList<MySurveyInfo>)
    data class MySurveyInfo(
        val survey_id: String,
        val title: String,
        val status: String,
        @SerializedName("start_data") val start_date: String?,
        @SerializedName("end_data") val end_date: String?,
        val answer_cnt: Int
    )
}