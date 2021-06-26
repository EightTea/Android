package com.bside.five.ui.main

import android.util.Log
import android.view.View
import androidx.databinding.ObservableArrayList
import com.bside.five.R
import com.bside.five.base.BaseViewModel
import com.bside.five.model.Survey

class MainViewModel : BaseViewModel() {

    private val TAG = this::class.java.simpleName

    val items = ObservableArrayList<Survey>()

    override fun onClickListener(view: View) {

        when (view.id) {
            R.id.mainNoticeBtn -> {
                Log.d(TAG, "onClickListener - mainNoticeBtn")
            }
            R.id.mainAddSurveyBtn -> {
                Log.d(TAG, "onClickListener - mainAddSurveyBtn")
            }

        }
    }

    fun requestMainApi() {
        val list = ArrayList<Survey>()
        list.add(Survey("우리 가게 방문 이유가 궁금합니다.", 10, "b"))
        list.add(Survey("신제품이 출시 되었습니다. 해당 제품에 관해 의견 부탁드려요.", 3, "i"))
        list.add(Survey("우리 카페에 바뀌었으면 하는 부분을 자유롭게 알려주세요.", 28, "i"))
        list.add(Survey("선호하는 제품을 알려주세요.", 100, "e"))

        items.addAll(list)
    }
}