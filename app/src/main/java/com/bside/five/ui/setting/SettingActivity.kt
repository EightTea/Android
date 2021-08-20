package com.bside.five.ui.setting

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.bside.five.R
import com.bside.five.base.BaseActivity
import com.bside.five.custom.dialog.MoreBottomSheetDialog
import com.bside.five.databinding.ActivitySettingBinding

class SettingActivity : BaseActivity<ActivitySettingBinding, SettingViewModel>() {

    private val tag = this::class.java

    override val layoutResourceId: Int
        get() = R.layout.activity_setting
    override val viewModelClass: Class<SettingViewModel>
        get() = SettingViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()
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
}