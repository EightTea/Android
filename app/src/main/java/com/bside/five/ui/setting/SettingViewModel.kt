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
import com.bside.five.util.ActivityUtil
import com.bside.five.util.FivePreference

class SettingViewModel : BaseViewModel() {

    private val tag = this::class.java

    override fun onClickListener(view: View) {
        when (view.id) {
            R.id.settingSupportBtn -> {
                Toast.makeText(view.context, "준비중입니다.", Toast.LENGTH_LONG).show()
            }
            R.id.settingContactBtn -> {
                showContactEmail(view.context)
            }
            R.id.settingPrivacyPolicyBtn -> {
                ActivityUtil.startPrivacyPolicy(view.context)
            }
        }
    }

    private fun showContactEmail(context: Context) {
        val title = "알고싶어 - "
        val contents = "App Version : ${BuildConfig.VERSION_NAME}\nOS Version : ${Build.VERSION.RELEASE}\n\n"

        Intent(Intent.ACTION_SEND).let {
            it.putExtra(Intent.EXTRA_EMAIL, arrayOf(Constants.EMAIL_ADDRESS))
            it.putExtra(Intent.EXTRA_SUBJECT, title)
            it.putExtra(Intent.EXTRA_TEXT, contents)
            it.type = "message/rfc822"
            context.startActivity(Intent.createChooser(it, "Choose Email"))
        }
    }
}