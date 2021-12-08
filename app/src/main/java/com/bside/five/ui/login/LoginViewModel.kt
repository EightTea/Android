package com.bside.five.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bside.five.base.BaseViewModel
import com.bside.five.network.repository.UserRepository
import com.bside.five.util.FivePreference
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.Gender
import com.kakao.sdk.user.model.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginViewModel : BaseViewModel() {

    private val tag = this::class.java.simpleName
    private val _activityLive: MutableLiveData<Unit> = MutableLiveData()
    val activityLive: LiveData<Unit> get() = _activityLive
    private val _policyDialogLive: MutableLiveData<User> = MutableLiveData()
    val policyDialogLive: LiveData<User> get() = _policyDialogLive
    private val _toastLive: MutableLiveData<String> = MutableLiveData()
    val toastLive: LiveData<String> get() = _toastLive

    /**
     * 사용자 정보 요청 (기본)
     */
    fun requestKakaoUserInfo() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(tag, "사용자 정보 요청 실패", error)
            } else if (user != null) {
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

                        _activityLive.postValue(Unit)
                    } else {
                        _toastLive.value = response.msg
                    }
                }, { t: Throwable? ->
                    t?.printStackTrace()
                })
        )
    }

    /**
     * 기존 유져 여부 판단
     */
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
                            _policyDialogLive.postValue(user)
                        }
                    } else {
                        _toastLive.value = response.msg
                    }
                }, { t: Throwable? ->
                    t?.printStackTrace()
                })
        )
    }
}