package com.bside.five.network

import com.bside.five.network.response.UserResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitService {

    /**
     * 회원가입
     */
    @POST("user")
    fun requestJoin(
        @Body body: Map<String, String>
    ): Single<UserResponse>

    /**
     * 로그인
     */
    @GET("/user/{id}")
    fun requestLogin(
        @Path("id") id: Int
    ): Single<UserResponse>
}