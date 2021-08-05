package com.bside.five.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.bside.five.constants.Constants
import com.bside.five.ui.answer.AnswerActivity
import com.bside.five.ui.gallery.GalleryActivity
import com.bside.five.ui.main.MainActivity
import com.bside.five.ui.my.MySurveyActivity
import com.bside.five.ui.preview.PreviewActivity
import com.bside.five.ui.result.QrCodeActivity
import com.bside.five.ui.sample.SampleActivity
import com.bside.five.ui.setting.SettingActivity
import com.bside.five.ui.survey.NewSurveyActivity

object ActivityUtil {

    fun startMainActivity(activity: Activity) {
        Intent(activity, MainActivity::class.java).run {
            activity.startActivity(this)
            activity.overridePendingTransition(0, 0)
        }
    }

    fun startNewSurveyActivity(activity: Activity) {
        Intent(activity, NewSurveyActivity::class.java).run {
            activity.startActivity(this)
        }
    }

    fun startGalleryActivity(activity: Activity, fragment: Fragment? = null) {
        Intent(activity, GalleryActivity::class.java).run {
            fragment?.let {
                it.startActivityForResult(this, Constants.REQUEST_CODE_GALLERY)
            } ?: activity.startActivityForResult(this, Constants.REQUEST_CODE_GALLERY)
//            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    fun startPreviewActivity(activity: Activity, no: Int, contents: String, imageUri: Uri) {
        Intent(activity, PreviewActivity::class.java).run {
            putExtra(Constants.EXTRA_NO, no)
            putExtra(Constants.EXTRA_CONTENTS, contents)
            putExtra(Constants.EXTRA_IMAGE, imageUri)
            activity.startActivity(this)
        }
    }

    fun startQrCodeActivity(activity: Activity, surveyId: String, isCreateQr: Boolean = false) {
        Intent(activity, QrCodeActivity::class.java).run {
            putExtra(Constants.EXTRA_SURVEY_ID, surveyId)
            putExtra(Constants.EXTRA_IS_CREATE_QR, isCreateQr)
            activity.startActivity(this)
        }
    }

    fun startSampleActivity(activity: Activity, title: String? = null, contents: String? = null) {
        Intent(activity, SampleActivity::class.java).run {
            putExtra(Constants.EXTRA_TITLE, title)
            putExtra(Constants.EXTRA_CONTENTS, contents)
            activity.startActivity(this)
        }
    }

    fun startSettingActivity(activity: Activity) {
        Intent(activity, SettingActivity::class.java).run {
            activity.startActivity(this)
        }
    }

    fun startMySurveyActivity(activity: Activity) {
        Intent(activity, MySurveyActivity::class.java).run {
            activity.startActivity(this)
        }
    }

    fun startAnswerActivity(activity: Activity, surveyId: String, title: String, count: Int) {
        Intent(activity, AnswerActivity::class.java).run {
            putExtra(Constants.EXTRA_SURVEY_ID, surveyId)
            putExtra(Constants.EXTRA_TITLE, title)
            putExtra(Constants.EXTRA_ANSWER_COUNT, count)
            activity.startActivity(this)
        }
    }
}