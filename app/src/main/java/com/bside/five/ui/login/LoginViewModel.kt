package com.bside.five.ui.login

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bside.five.R
import com.bside.five.base.BaseViewModel
import com.bside.five.base.FiveApp
import com.bside.five.network.repository.UserRepository
import com.bside.five.util.ActivityUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

class LoginViewModel : BaseViewModel() {

    private val tag = this::class.java.simpleName

    private var id: Int = 12345

    override fun onClickListener(view: View) {
        if (view.id == R.id.kakaoLoginBtn) {
//            requestJoin()
            requestLogin(view)
        }
    }

    /**
     * 1. (12345, "kch", "kch store", "kch@gmail.com", 1, 1991)
     * 2. (11111, "sub", "chain store", "kch2@gmail.com", 0, 1992)
     */
    private fun requestJoin() {
        disposables.add(
            UserRepository().requestJoin(11111, "sub", "chain store", "kch2@gmail.com", 0, 1992)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.isSuccess()) {
                        // FIXME : MainActiviry Start 및 토큰 저장
                    } else {
                        Toast.makeText(
                            FiveApp().applicationContext,
                            "error : ${response.msg} code : ${response.code}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }, { t: Throwable? ->
                    t?.printStackTrace()
                })
        )
    }

    private fun requestLogin(view: View) {
        disposables.add(
            UserRepository().requestLogin(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.isSuccess()) {
//                        FiveApp().preference.setAccessToken(response.data.access_token)

                        val activity = view.context as AppCompatActivity
                        ActivityUtil.startMainActivity(view.context as AppCompatActivity)
                        activity.finish()
                    }

                    Toast.makeText(view.context, response.msg, Toast.LENGTH_LONG).show()
                }, { t: Throwable? -> t?.printStackTrace() })
        )
    }
}