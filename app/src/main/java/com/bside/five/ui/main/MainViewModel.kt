package com.bside.five.ui.main

import android.util.Log
import android.view.View
import com.bside.five.R
import com.bside.five.base.BaseViewModel

class MainViewModel : BaseViewModel() {

    private val TAG = this::class.java.simpleName

    override fun onClickListener(view: View) {

        when (view.id) {
            R.id.qrBtn -> {
                Log.d(TAG, "onClickListener - qrBtn")
            }
            R.id.newSurveyBtn -> {
                Log.d(TAG, "onClickListener - newSurveyBtn")
            }
            R.id.SurveyResultBtn -> {
                Log.d(TAG, "onClickListener - SurveyResultBtn")
            }
        }
    }
}