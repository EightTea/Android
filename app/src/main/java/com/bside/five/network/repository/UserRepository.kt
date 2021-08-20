package com.bside.five.network.repository

import com.bside.five.network.response.BaseResponse
import com.bside.five.network.response.SelectUserResponse
import com.bside.five.network.response.UserResponse
import io.reactivex.Single
import java.util.*

class UserRepository : BaseRepository() {

    fun requestJoin(
        id: String,
        nickName: String,
        email: String,
        gender: Int,
        year: String
    ): Single<UserResponse> {
        val param: HashMap<String, String> = HashMap()
        param["id"] = id
        param["nickname"] = nickName
        param["email"] = email
        param["gender"] = gender.toString()
        param["year"] = year

        return service.requestJoin(param)
    }

    fun requestUserDelete(accessToken: String, id: String): Single<BaseResponse> {
        return service.requestUserDelete(accessToken, id)
    }

    fun requestSelectUser(id: String): Single<SelectUserResponse> {
        return service.requestSelectUser(id)
    }
}