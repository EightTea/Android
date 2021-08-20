package com.bside.five.ui.login

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.bside.five.R
import com.bside.five.base.BaseApplicationViewModel
import com.bside.five.custom.dialog.PolicyDialog
import com.bside.five.custom.listener.OnConfirmListener
import com.bside.five.network.repository.UserRepository
import com.bside.five.util.FivePreference
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.Gender
import com.kakao.sdk.user.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginViewModel(application: Application) : BaseApplicationViewModel(application) {

    private val tag = this::class.java.simpleName
    private val context = getApplication<Application>().applicationContext
    var activityLive: MutableLiveData<String> = MutableLiveData()
    var dialogLive: MutableLiveData<User> = MutableLiveData()

    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(tag, "로그인 실패", error)
        } else if (token != null) {
            Log.i(tag, "로그인 성공 ${token.accessToken}")
            requestKakaoUserInfo()
        }
    }

    override fun onClickListener(view: View) {
        if (view.id == R.id.kakaoLoginBtn) {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    /**
     * 사용자 정보 요청 (기본)
     */
    private fun requestKakaoUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(tag, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.i(
                    tag, "사용자 정보 요청 성공" +
                            "\n회원번호: ${user.id}" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                            "\n이메일: ${user.kakaoAccount?.email}" +
                            "\n성별: ${user.kakaoAccount?.gender}" +
                            "\n생일: ${user.kakaoAccount?.birthyear}"
                )

                requestSelectUser(user)
            }
        }
    }

    /**
     * Kakao Login Access check
     */
    fun checkKakaoLoginAccess() {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { accessToken, error ->
                if (error != null) {
                    Log.e(tag, "사용자 정보 요청 실패", error)
                } else if (accessToken != null) {
                    requestKakaoUserInfo()
                }
            }
        }
    }

    /**
     * 회원가입/로그인
     */
    fun requestJoin(id: String, nickName: String, email: String, gender: Int, year: String) {
        disposables.add(
            UserRepository().requestJoin(id, nickName, email, gender, year)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.isSuccess()) {
                        FivePreference.setAccessToken("Bearer ${response.data.access_token}")
                        FivePreference.setUserId(id)

                        activityLive.postValue("")
                    } else {
                        Toast.makeText(context, response.msg, Toast.LENGTH_LONG).show()
                    }
                }, { t: Throwable? ->
                    t?.printStackTrace()
                })
        )
    }

    private fun requestSelectUser(user: User) {
        disposables.add(
            UserRepository().requestSelectUser(user.id.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.isSuccess()) {
                        if (response.data.isUser) {
                            requestJoin(
                                user.id.toString(),
                                user.kakaoAccount?.profile?.nickname ?: "",
                                user.kakaoAccount?.email ?: "",
                                if (user.kakaoAccount?.gender == Gender.MALE) 1 else 0,
                                user.kakaoAccount?.birthyear ?: ""
                            )
                        } else {
                            dialogLive.postValue(user)
                        }
                    } else {
                        Toast.makeText(context, response.msg, Toast.LENGTH_LONG).show()
                    }
                }, { t: Throwable? ->
                    t?.printStackTrace()
                })
        )
    }
}