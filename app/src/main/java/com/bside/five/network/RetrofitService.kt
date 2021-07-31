package com.bside.five.network

import com.bside.five.network.response.BaseResponse
import com.bside.five.network.response.SurveyResponse
import com.bside.five.network.response.UserResponse
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
    @DELETE("/user/{id}")
    fun requestUserDelete(
        @Path("id") id: String
    ): Single<BaseResponse>

    /**
     * 설문 조사 생성
     */
    @Multipart
    @POST("/survey")
    fun createSurvey(
        @Header("Authorization") accessToken: String,
        @PartMap params: HashMap<String, RequestBody>,
        @Part questionContentList: ArrayList<MultipartBody.Part>,
        @Part questionFileList: ArrayList<MultipartBody.Part>
    ): Single<SurveyResponse>
}