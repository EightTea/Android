package com.bside.five.custom.dialog

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bside.five.R
import com.bside.five.databinding.DialogBottomSheetMoreBinding
import com.bside.five.util.ActivityUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kakao.sdk.user.UserApiClient


class MoreBottomSheetDialog(context: Context) : BottomSheetDialog(context, R.style.RadiusBottomSheetDialogTheme) {

    private var binding: DialogBottomSheetMoreBinding =
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_bottom_sheet_more, null, false)

    init {
        setContentView(binding.root)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
    }

    override fun show() {
        binding.moreBottomSheetLogoutBtn.setOnClickListener {
            logoutKakao()
        }

        binding.moreBottomSheetDeleteAccountBtn.setOnClickListener {
            val dialog = DeleteAccountDialog(context)
            dialog.show()
        }

        super.show()
    }

    /**
     * 로그아웃
     */
    private fun logoutKakao() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e("TAG", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            } else {
                Log.i("TAG", "로그아웃 성공. SDK에서 토큰 삭제됨")
                dismiss()
                ActivityUtil.startLoginActivity(context, context.getString(R.string.logout_msg))
            }
        }
    }
}