package com.bside.five.ui.login

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bside.five.R
import com.bside.five.base.BaseActivity
import com.bside.five.constants.Constants
import com.bside.five.custom.dialog.PolicyDialog
import com.bside.five.custom.listener.OnConfirmListener
import com.bside.five.databinding.ActivityLoginBinding
import com.bside.five.util.ActivityUtil
import com.google.android.material.snackbar.Snackbar
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthError
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.Gender
import com.kakao.sdk.user.model.User

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    private val tag = this::class.java.simpleName
    private val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(tag, "로그인 실패", error)

            when ((error as? AuthError)?.statusCode) {
                302 -> {
                    // NotSupportError
                    loginKakao(false)
                }
            }
        } else if (token != null) {
            Log.i(tag, "로그인 성공")
            viewModel.requestKakaoUserInfo()
        }
    }

    override val layoutResourceId: Int
        get() = R.layout.activity_login
    override val viewModelClass: Class<LoginViewModel>
        get() = LoginViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.getStringExtra(Constants.EXTRA_SHOW_SNACK_BAR)?.let { msg ->
            Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
        }

        subscribe()

        binding.kakaoLoginBtn.setOnClickListener {
            loginKakao(UserApiClient.instance.isKakaoTalkLoginAvailable(this))
        }

        viewModel.checkKakaoLoginAccess()
    }

    private fun loginKakao(isKakaoApp: Boolean) {
        if (isKakaoApp) {
            UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    private fun subscribe() {
        viewModel.activityLive.observe(this, Observer<Unit?> {
            ActivityUtil.startMainActivity(this@LoginActivity)
            finish()
        })

        viewModel.policyDialogLive.observe(this, Observer<User?> { user ->
            val dialog = PolicyDialog(this@LoginActivity, object : OnConfirmListener {
                override fun onConfirm() {
                    user?.let {
                        viewModel.requestJoin(
                            it.id.toString(),
                            it.kakaoAccount?.profile?.nickname ?: "",
                            it.kakaoAccount?.email ?: "",
                            if (it.kakaoAccount?.gender == Gender.MALE) 1 else 0,
                            it.kakaoAccount?.birthyear ?: ""
                        )
                    }
                }
            })
            dialog.show()
        })

        viewModel.toastLive.observe(this,
            Observer<String?> { t -> Toast.makeText(this@LoginActivity, t, Toast.LENGTH_LONG).show() })
    }
}