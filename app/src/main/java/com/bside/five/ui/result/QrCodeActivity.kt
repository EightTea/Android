package com.bside.five.ui.result

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.Observer
import com.bside.five.R
import com.bside.five.base.BaseActivity
import com.bside.five.constants.Constants
import com.bside.five.databinding.ActivityQrCodeBinding
import com.bside.five.extension.showKeyboard
import com.bside.five.network.ApiClient
import com.bside.five.util.CommonUtil
import com.bside.five.util.GlideUtil
import com.bside.five.util.ImageUtil
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder


class QrCodeActivity : BaseActivity<ActivityQrCodeBinding, QrCodeViewModel>() {

    companion object {
        const val QR_CODE_WIDTH = 176
        const val QR_CODE_HEIGHT = 176
    }

    private var uri: Uri = Uri.EMPTY
    private var qrUrl: String = ""

    override val layoutResourceId: Int
        get() = R.layout.activity_qr_code
    override val viewModelClass: Class<QrCodeViewModel>
        get() = QrCodeViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()
        initIntent()
        subscribe()

        binding.qrCodeTitle.showKeyboard()
//        binding.qrCodeTitle.requestFocus()

        viewModel.qrDrawable.set(createQrCode(qrUrl).toDrawable(resources))
    }

    override fun onResume() {
        super.onResume()
        ImageUtil.deleteImage(this, uri)
        uri = Uri.EMPTY
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_share -> {
                if (CommonUtil.checkStoragePermission(this)) {
                    val fileName = System.currentTimeMillis().toString() + "_${ImageUtil.TEMP_FILE_NAME}"
                    saveQrCodeImage(fileName).let {
                        uri = it
                        CommonUtil.shareGoogleLink(this, it, null)
                    }
                }
                return true
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.qr_code, menu)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == ImageUtil.DELETE_PERMISSION_REQUEST) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ImageUtil.deleteImage(this, ImageUtil.TEMP_FILE_NAME)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding.qrCodeToolbar as Toolbar)
        supportActionBar?.run {
            setDisplayShowCustomEnabled(true)
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun initIntent() {
        intent.let {
            it.getStringExtra(Constants.EXTRA_SURVEY_ID)?.let { surveyId ->
                qrUrl = ApiClient.USER_SURVEY_URL + surveyId + "/view"
            }

            it.getBooleanExtra(Constants.EXTRA_IS_CREATE_QR, false).let { isCreate ->
                if (isCreate) {
                    showSnackBar(R.string.survey_complete_msg)
                }
            }
        }
    }

    private fun createQrCode(url: String): Bitmap {
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.encodeBitmap(url, BarcodeFormat.QR_CODE, QR_CODE_WIDTH, QR_CODE_HEIGHT)
    }

    private fun saveQrCodeImage(fileName: String): Uri {

        val bitmap = getBitmapFromView(
            binding.qrCodeContainer,
            binding.qrCodeContainer.width,
            binding.qrCodeContainer.height
        )

        return ImageUtil.insertImage(this, bitmap, fileName, "image/*")
    }

    private fun getBitmapFromView(view: View, width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = view.background

        if (bgDrawable != null) bgDrawable.draw(canvas) else canvas.drawColor(Color.WHITE)
        view.draw(canvas)

        return bitmap
    }

    private fun subscribe() {
        viewModel.imageSaveLive.observe(this, Observer<String?> { fileName ->
            fileName?.let {
                saveQrCodeImage(it)
                viewModel.isDownloadQr.set(false)
                GlideUtil.loadImage(binding.qrCodeSaveIcon, R.drawable.ic_download_done)
            }
        })
    }
}