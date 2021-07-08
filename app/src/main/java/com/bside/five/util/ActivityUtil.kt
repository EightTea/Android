package com.bside.five.util

import android.app.Activity
import android.content.Intent
import com.bside.five.constants.Constants
import com.bside.five.ui.gallery.GalleryActivity

object ActivityUtil {

    fun startGalleryActivity(activity: Activity) {
        Intent(activity, GalleryActivity::class.java).run {
            activity.startActivityForResult(this, Constants.REQUEST_CODE_GALLERY)
//            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }
}