package com.bside.five.network.repository

import com.bside.five.network.response.MySurveyListResponse
import com.bside.five.network.response.SurveyResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.*

class SurveyRepository : BaseRepository() {

    fun createSurvey(
        accessToken: String,
        title: String,
        content: String,
        questionContentList: ArrayList<MultipartBody.Part>,
        questionFileList: ArrayList<MultipartBody.Part>
    ): Single<SurveyResponse> {
        val param: HashMap<String, RequestBody> = HashMap()
        param["title"] = toRequestBody(title)
        param["content"] = toRequestBody(content)

        return service.createSurvey(accessToken, param, questionContentList, questionFileList)
    }

    fun requestSurveyList(accessToken: String): Single<MySurveyListResponse> {
        return service.requestSurveyList(accessToken)
    }
}