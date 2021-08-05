package com.bside.five.network.repository

import com.bside.five.network.response.*
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

    fun requestSurveyDetail(accessToken: String, surveyId: String): Single<MySurveyDetailResponse> {
        return service.requestSurveyDetail(accessToken, surveyId)
    }

    fun requestSurveyAnswer(accessToken: String, surveyId: String, answer: String): Single<AnswerListResponse> {
        return service.requestSurveyAnswer(accessToken, surveyId, answer)
    }

    fun requestSurveyStateChange(accessToken: String, surveyId: String, status: String): Single<BaseResponse> {
        val param: HashMap<String, String> = HashMap()
        param["status"] = status

        return service.requestSurveyStateChange(accessToken, surveyId, param)
    }
}