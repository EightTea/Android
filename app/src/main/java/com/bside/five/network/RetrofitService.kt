package com.bside.five.network

import com.bside.five.network.response.*
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface RetrofitService {

    /**
     * 회원가입
     */
    @Headers("Content-Type: application/json")
    @POST("user")
    fun requestJoin(
        @Body body: Map<String, String>
    ): Single<UserResponse>

    /**
     * 회원 탈퇴
     */
    @Headers("Content-Type: application/json")
    @DELETE("user/{id}")
    fun requestUserDelete(
        @Header("Authorization") accessToken: String,
        @Path("id") id: String
    ): Single<BaseResponse>

    @Headers("Content-Type: application/json")
    @GET("user/{id}")
    fun requestSelectUser(
        @Path("id") id: String
    ): Single<SelectUserResponse>


    /**
     * 설문 조사 생성
     */
    @Multipart
    @POST("survey")
    fun createSurvey(
        @Header("Authorization") accessToken: String,
        @PartMap params: HashMap<String, RequestBody>,
        @Part questionContentList: ArrayList<MultipartBody.Part>,
        @Part questionFileList: ArrayList<MultipartBody.Part>
    ): Single<SurveyResponse>

    /**
     * 내 설문 조사 리스트
     */
    @Headers("Content-Type: application/json")
    @GET("survey")
    fun requestSurveyList(
        @Header("Authorization") accessToken: String
    ): Single<MySurveyListResponse>

    /**
     * 설문 조사 질문 리스트
     */
    @Headers("Content-Type: application/json")
    @GET("survey/{survey_id}")
    fun requestSurveyDetail(
        @Header("Authorization") accessToken: String,
        @Path("survey_id") surveyId: String
    ): Single<MySurveyDetailResponse>

    /**
     * 설문 조사 질문별 답변 리스트
     */
    @Headers("Content-Type: application/json")
    @GET("survey/{survey_id}/{question_id}")
    fun requestSurveyAnswer(
        @Header("Authorization") accessToken: String,
        @Path("survey_id") surveyId: String,
        @Path("question_id") questionId: String
    ): Single<AnswerListResponse>

    /**
     * 내 설문 조사 상태 변경 요청(ex. 설문 종료)
     */
    @Headers("Content-Type: application/json")
    @PUT("survey/{survey_id}")
    fun requestSurveyStateChange(
        @Header("Authorization") accessToken: String,
        @Path("survey_id") surveyId: String,
        @Body body: Map<String, String>
    ): Single<BaseResponse>
}