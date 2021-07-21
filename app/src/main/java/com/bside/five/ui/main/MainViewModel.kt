package com.bside.five.ui.main

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableArrayList
import com.bside.five.R
import com.bside.five.base.BaseViewModel
import com.bside.five.model.Survey
import com.bside.five.ui.survey.NewSurveyActivity

class MainViewModel : BaseViewModel() {

    private val TAG = this::class.java.simpleName

    override fun onClickListener(view: View) {

        when (view.id) {
            R.id.mainAddSurveyBtn -> {
                val activity = (view.context as AppCompatActivity)

                val intent = Intent(activity, NewSurveyActivity::class.java)
                activity.startActivity(intent)
            }
            R.id.mainMySurveyBtn -> {
                Log.d(TAG, "onClickListener - mainMySurveyBtn")
            }
        }
    }
}