package com.bside.five.ui.survey

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.bside.five.R
import com.bside.five.adapter.ScreenSlidePagerAdapter
import com.bside.five.base.BaseViewModel
import com.bside.five.model.SurveyFragmentInfo

class NewSurveyViewModel : BaseViewModel() {

    private val tag = NewSurveyViewModel::class.java.simpleName
    var pagePositionLive: MutableLiveData<Int> = MutableLiveData()
    var content = ObservableField<String>("")
    var imgPath = ""
    lateinit var adapter: ScreenSlidePagerAdapter
    var parentId = -1

    override fun onClickListener(view: View) {
        when (view.id) {
            R.id.newSurveyAddQuestionBtn -> {
                Toast.makeText(view.context, "newSurveyAddQuestionBtn", Toast.LENGTH_LONG).show()
                createChildId()
            }
            R.id.newSurveyFinishQuestionBtn -> {
                Toast.makeText(view.context, "newSurveyFinishQuestionBtn", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun init(activity: AppCompatActivity) {
        adapter = ScreenSlidePagerAdapter(activity.supportFragmentManager, activity.lifecycle)
//        createParentId()
    }

    private fun createParentId() {
        val getParentId = 1
        parentId = getParentId
        createChildId()
    }

    private fun createChildId() {
//        if (parentId == -1) {
//            Log.d(tag, "ParentId create fail")
//            return
//        }

        val getChildId = 1
        adapter.addFragment(SurveyFragmentInfo(parentId, getChildId))

        pagePositionLive.postValue(adapter.itemCount - 1)
    }
}