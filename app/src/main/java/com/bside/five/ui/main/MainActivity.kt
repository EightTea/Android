package com.bside.five.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.bside.five.R
import com.bside.five.base.BaseActivity
import com.bside.five.databinding.ActivityMainBinding
import com.bside.five.ui.scanner.QrScannerActivity
import com.bside.five.util.ActivityUtil
import com.bside.five.util.FivePreference
import com.google.zxing.integration.android.IntentIntegrator

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    private val tag = this::class.java.simpleName

    override val layoutResourceId: Int
        get() = R.layout.activity_main
    override val viewModelClass: Class<MainViewModel>
        get() = MainViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()
        showSnackBar(R.string.login_complete_msg)

        Log.d(tag, "kch AccessToken : ${FivePreference.getAccessToken(this)}")
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                ActivityUtil.startSettingActivity(this)
                overridePendingTransition(0, 0)
                return true
            }
            R.id.action_scanner -> {
                showQrScanner()
                return true
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        result?.contents?.let {
            Toast.makeText(this, "Scanned: ${result.contents}", Toast.LENGTH_LONG).show()
        } ?: super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initToolbar() {
        setSupportActionBar(binding.mainToolbar as Toolbar)
        supportActionBar?.run {
            setDisplayShowCustomEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
            setHomeAsUpIndicator(R.drawable.ic_nav_menu)
        }
    }

    private fun showQrScanner() {
        val intentIntegrator = IntentIntegrator(this)
        intentIntegrator.setBeepEnabled(false)
        intentIntegrator.captureActivity = QrScannerActivity::class.java
        intentIntegrator.initiateScan()
    }
}