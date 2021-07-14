package com.bside.five.ui.scanner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bside.five.R
import com.bside.five.databinding.ActivityQrScannerBinding
import com.journeyapps.barcodescanner.CaptureManager

class QrScannerActivity : AppCompatActivity() {

    private val binding: ActivityQrScannerBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_qr_scanner) as ActivityQrScannerBinding
    }

    private lateinit var manager: CaptureManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        manager = CaptureManager(this, binding.scannerBarcodeView)
        manager.initializeFromIntent(intent, savedInstanceState)
        manager.decode()

        binding.scannerBarcodeView.viewFinder.setLaserVisibility(false)
        binding.scannerBarcodeView.viewFinder.clearAnimation()
    }

    override fun onResume() {
        super.onResume()
        manager.onResume()
    }

    override fun onPause() {
        super.onPause()
        manager.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        manager.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        manager.onSaveInstanceState(outState)
    }
}