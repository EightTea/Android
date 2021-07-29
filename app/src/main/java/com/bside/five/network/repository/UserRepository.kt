package com.bside.five.network.repository

import com.bside.five.network.response.UserResponse
import io.reactivex.Single
import java.util.*

class UserRepository : BaseRepository() {

    fun requestJoin(
        id: Int,
        nickName: String,
        storeName: String,
        email: String,
        gender: Int,
        year: Int
    ): Single<UserResponse> {
        val param: HashMap<String, String> = HashMap()
        param["id"] = id.toString()
        param["nickname"] = nickName
        param["store_name"] = storeName
        param["email"] = email
        param["gender"] = gender.toString()
        param["year"] = year.toString()

        return service.requestJoin(param)
    }

    fun requestLogin(id: Int): Single<UserResponse> {
        return service.requestLogin(id)
    }
}