package com.bside.five.ui.setting

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.Toast
import com.bside.five.BuildConfig
import com.bside.five.R
import com.bside.five.base.BaseViewModel
import com.bside.five.constants.Constants

class SettingViewModel : BaseViewModel() {

    private val tag = this::class.java

    override fun onClickListener(view: View) {
        when (view.id) {
            R.id.settingSupportBtn -> {
                Toast.makeText(view.context, "settingSupportBtn", Toast.LENGTH_LONG).show()
            }
            R.id.settingContactBtn -> {
                showContactEmail(view.context)
            }
            R.id.settingPrivacyPolicyBtn -> {
                Toast.makeText(view.context, "settingPrivacyPolicyBtn", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showContactEmail(context: Context) {
        val nickName = "admin"
        val title = "(귀쫑끗) "
        val content = "Nickname : $nickName \n" +
                "App Version : ${BuildConfig.VERSION_NAME}\n" +
                "OS Version : ${Build.VERSION.RELEASE}\n\n"

        Intent(Intent.ACTION_SEND).let {
            it.putExtra(Intent.EXTRA_EMAIL, Constants.EMAIL_ADDRESS)
            it.putExtra(Intent.EXTRA_SUBJECT, title)
            it.putExtra(Intent.EXTRA_TEXT, content)
            it.type = "message/rfc822"
            context.startActivity(Intent.createChooser(it, "Choose Email"))
        }
    }
}