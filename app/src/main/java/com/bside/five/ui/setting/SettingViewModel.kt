package com.bside.five.ui.setting

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import com.bside.five.BuildConfig
import com.bside.five.R
import com.bside.five.base.BaseViewModel
import com.bside.five.constants.Constants
import com.bside.five.util.ActivityUtil
import com.bside.five.util.FivePreference
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

class SettingViewModel : BaseViewModel() {

    private val tag = this::class.java
    val version = ObservableField<String>()

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
            R.id.settingVersionBtn -> {
                Toast.makeText(view.context, BuildConfig.VERSION_CODE.toString(), Toast.LENGTH_LONG).show()
            }
            R.id.settingLicenseBtn -> {
                ContextCompat.startActivity(view.context, Intent(view.context, OssLicensesMenuActivity::class.java), null)
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