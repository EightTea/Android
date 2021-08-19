package com.bside.five.network

import com.bside.five.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val BASE_APP_URL = "http://ec2-3-34-137-134.ap-northeast-2.compute.amazonaws.com:8080/api/"
    const val USER_SURVEY_URL = "http://ec2-3-34-137-134.ap-northeast-2.compute.amazonaws.com:8080/survey/"
    const val BASE_URL = "http://ec2-3-34-137-134.ap-northeast-2.compute.amazonaws.com:8080/"
    private const val TIMEOUT_CONNECT = 15
    private const val TIMEOUT_READ = 15
    private const val TIMEOUT_WRITE = 20

    private var retrofit: Retrofit? = null
    private var okHttpClient: OkHttpClient? = null
    private var interceptor: HttpLoggingInterceptor? = null

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return interceptor ?: HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }

            interceptor = this
        }
    }

    private fun getOkHttpClient(): OkHttpClient {
        return okHttpClient ?: OkHttpClient.Builder()
            .addInterceptor(getLoggingInterceptor())
            .connectTimeout(TIMEOUT_CONNECT.toLong(), TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_READ.toLong(), TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_WRITE.toLong(), TimeUnit.SECONDS)
            .build()
            .apply {
                okHttpClient = this
            }
    }

    fun getClient(): Retrofit {
        return retrofit ?: Retrofit.Builder()
            .baseUrl(BASE_APP_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient())
            .build()
            .apply {
                retrofit = this
            }
    }
}