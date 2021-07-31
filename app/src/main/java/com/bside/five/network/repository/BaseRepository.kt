package com.bside.five.network.repository

import com.bside.five.network.ApiClient
import com.bside.five.network.RetrofitService
import okhttp3.MediaType
import okhttp3.RequestBody

open class BaseRepository {
    val service: RetrofitService = ApiClient.getClient().create(RetrofitService::class.java)

    fun toRequestBody(content: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), content)
    }
}