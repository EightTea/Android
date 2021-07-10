package com.bside.five.util

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.bside.five.constants.Constants
import com.bside.five.ui.gallery.GalleryActivity

object ActivityUtil {

    fun startGalleryActivity(activity: Activity, fragment: Fragment? = null) {
        Intent(activity, GalleryActivity::class.java).run {
            fragment?.let {
                it.startActivityForResult(this, Constants.REQUEST_CODE_GALLERY)
            } ?: activity.startActivityForResult(this, Constants.REQUEST_CODE_GALLERY)
//            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }
}