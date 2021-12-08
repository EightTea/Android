package com.bside.five.ui.setting

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.bside.five.BuildConfig
import com.bside.five.R
import com.bside.five.base.BaseActivity
import com.bside.five.constants.Constants
import com.bside.five.custom.dialog.MoreBottomSheetDialog
import com.bside.five.databinding.ActivitySettingBinding
import com.bside.five.util.ActivityUtil
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

class SettingActivity : BaseActivity<ActivitySettingBinding, SettingViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.activity_setting
    override val viewModelClass: Class<SettingViewModel>
        get() = SettingViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()
        listener()
        viewModel.setVersion(getString(R.string.setting_version, BuildConfig.VERSION_NAME))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                overridePendingTransition(0, 0)
                return true
            }
            R.id.action_more -> {
                val dialog = MoreBottomSheetDialog(this)
                dialog.show()
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.setting, menu)
        return true
    }

    private fun initToolbar() {
        setSupportActionBar(binding.settingToolbar as Toolbar)
        supportActionBar?.run {
            setDisplayShowCustomEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun listener() {
        binding.settingSupportBtn.setOnClickListener {
            Toast.makeText(this, "준비중입니다.", Toast.LENGTH_LONG).show()
        }
        binding.settingContactBtn.setOnClickListener {
            showContactEmail()
        }
        binding.settingPrivacyPolicyBtn.setOnClickListener {
            ActivityUtil.startPrivacyPolicy(this)
        }
        binding.settingVersionBtn.setOnClickListener {
            Toast.makeText(this, BuildConfig.VERSION_CODE.toString(), Toast.LENGTH_LONG).show()
        }
        binding.settingLicenseBtn.setOnClickListener {
            ContextCompat.startActivity(this, Intent(this, OssLicensesMenuActivity::class.java), null)
        }
    }

    private fun showContactEmail() {
        val title = "알고싶어 - "
        val contents = "App Version : ${BuildConfig.VERSION_NAME}\nOS Version : ${Build.VERSION.RELEASE}\n\n"

        Intent(Intent.ACTION_SEND).let {
            it.putExtra(Intent.EXTRA_EMAIL, arrayOf(Constants.EMAIL_ADDRESS))
            it.putExtra(Intent.EXTRA_SUBJECT, title)
            it.putExtra(Intent.EXTRA_TEXT, contents)
            it.type = "message/rfc822"
            startActivity(Intent.createChooser(it, "Choose Email"))
        }
    }
}