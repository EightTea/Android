package com.bside.five.custom.dialog

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bside.five.R
import com.bside.five.databinding.DialogDeleteAccoutBinding
import com.bside.five.network.repository.UserRepository
import com.bside.five.util.ActivityUtil
import com.bside.five.util.FivePreference
import com.kakao.sdk.user.UserApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DeleteAccountDialog(context: Context) : Dialog(context, R.style.DefaultDialog) {

    private val tag = this::class.java.simpleName
    private var binding: DialogDeleteAccoutBinding =
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_delete_accout, null, false)

    private val disposable = CompositeDisposable()

    init {
        setContentView(binding.root)
    }

    override fun show() {
        binding.deleteAccountOkBtn.setOnClickListener {
            requestUserDelete(it)
        }

        binding.deleteAccountCancelBtn.setOnClickListener {
            dismiss()
        }

        super.show()
    }

    override fun dismiss() {
        disposable.dispose()
        super.dismiss()
    }

    /**
     * Kakao 연결 끊키
     */
    private fun unlinkKakaoUser() {
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e(tag, "연결 끊기 실패", error)
            } else {
                Log.i(tag, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
                Log.i(tag, "kch context: $context")

                ActivityUtil.startLoginActivity(
                    context,
                    context.getString(R.string.user_delete_msg)
                )
            }
        }
    }

    private fun requestUserDelete(view: View) {
        disposable.add(
            UserRepository().requestUserDelete(
                FivePreference.getAccessToken(view.context),
                FivePreference.getUserId(view.context)
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    Toast.makeText(view.context, response.msg, Toast.LENGTH_LONG).show()
                    unlinkKakaoUser()
                    dismiss()
                }, { t: Throwable? ->
                    t?.printStackTrace()
                    Toast.makeText(view.context, "다시 시도해주세요.", Toast.LENGTH_LONG).show()
                    dismiss()
                })
        )
    }
}