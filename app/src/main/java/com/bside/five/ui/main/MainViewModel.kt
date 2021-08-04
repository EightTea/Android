package com.bside.five.ui.main

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bside.five.R
import com.bside.five.base.BaseViewModel
import com.bside.five.ui.survey.NewSurveyActivity
import com.bside.five.util.ActivityUtil

class MainViewModel : BaseViewModel() {

    private val TAG = this::class.java.simpleName

    override fun onClickListener(view: View) {

        when (view.id) {
            R.id.mainAddSurveyBtn -> {
                ActivityUtil.startNewSurveyActivity(view.context as AppCompatActivity)
            }
            R.id.mainMySurveyBtn -> {
                ActivityUtil.startMySurveyActivity(view.context as AppCompatActivity)
            }
        }
    }
}