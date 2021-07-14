package com.bside.five.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.bside.five.constants.Constants
import com.bside.five.ui.gallery.GalleryActivity
import com.bside.five.ui.preview.PreviewActivity
import com.bside.five.ui.result.QrCodeActivity

object ActivityUtil {

    fun startGalleryActivity(activity: Activity, fragment: Fragment? = null) {
        Intent(activity, GalleryActivity::class.java).run {
            fragment?.let {
                it.startActivityForResult(this, Constants.REQUEST_CODE_GALLERY)
            } ?: activity.startActivityForResult(this, Constants.REQUEST_CODE_GALLERY)
//            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    fun startPreviewActivity(activity: Activity, no: Int, content: String, imageUri: Uri) {
        Intent(activity, PreviewActivity::class.java).run {
            putExtra(Constants.EXTRA_NO, no)
            putExtra(Constants.EXTRA_CONTENT, content)
            putExtra(Constants.EXTRA_IMAGE, imageUri)
            activity.startActivity(this)
        }
    }

    fun startQrCodeActivity(activity: Activity, surveyUrl: String) {
        Intent(activity, QrCodeActivity::class.java).run {
            putExtra(Constants.EXTRA_URL, surveyUrl)
            activity.startActivity(this)
        }
    }
}