package com.bside.five.ui.survey

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.bside.five.R
import com.bside.five.adapter.ScreenSlidePagerAdapter
import com.bside.five.base.BaseViewModel
import com.bside.five.model.QuestionInfo
import com.bside.five.model.SurveyFragmentInfo
import com.bside.five.util.ActivityUtil

class NewSurveyViewModel : BaseViewModel() {

    private val tag = NewSurveyViewModel::class.java.simpleName
    var pagePositionLive: MutableLiveData<Int> = MutableLiveData()
    var contentSizeLive: MutableLiveData<Int> = MutableLiveData()
    var clearImageLive: MutableLiveData<Int> = MutableLiveData()
    var questionNo = 1
    var content = ""
    var imgPath: Uri = Uri.EMPTY
    val questionInfoList = ArrayList<QuestionInfo>()
    lateinit var adapter: ScreenSlidePagerAdapter

    override fun onClickListener(view: View) {
        when (view.id) {
            R.id.newSurveyAddQuestionBtn -> {
                updateQuestionInfo()
                createPage()
            }
            R.id.newSurveyFinishQuestionBtn -> {
                Toast.makeText(view.context, "newSurveyFinishQuestionBtn", Toast.LENGTH_LONG).show()

                updateQuestionInfo()

                for (item in questionInfoList) {
                    Log.d(tag, "kch item.no : ${item.no}")
                    Log.d(tag, "kch item.content : ${item.content}")
                    Log.d(tag, "kch item.ImageUri : ${item.imageUri}")
                }

                // QR 구현 및 완료 화면 출력
            }
            R.id.questionImageContainer -> {
                val activity = view.context as AppCompatActivity
                val fragment = activity.supportFragmentManager.fragments.last()

                if (checkStoragePermission(activity)) {
                    ActivityUtil.startGalleryActivity(activity, fragment)
                }
            }
            R.id.questionImgRemoveBtn -> {
                imgPath = Uri.EMPTY
                clearImageLive.postValue(questionNo - 1)
            }
        }
    }

    fun init(activity: AppCompatActivity) {
        adapter = ScreenSlidePagerAdapter(activity.supportFragmentManager, activity.lifecycle)
    }

    fun createPage() {
        addQuestionInfo()

        adapter.addFragment(SurveyFragmentInfo(1, questionNo))
        pagePositionLive.postValue(adapter.itemCount - 1)
        questionNo += 1
    }

    fun deletePage() {
        val position: Int = questionInfoList.lastIndex

        deleteQuestionInfo(position)

        val item = adapter.getItem(position)
        adapter.removeFragment(item)
        pagePositionLive.postValue(adapter.itemCount - 1)
        questionNo -= 1
    }

    private fun addQuestionInfo() {
        questionInfoList.add(QuestionInfo(questionNo, "", Uri.EMPTY))
        imgPath = Uri.EMPTY
    }

    private fun updateQuestionInfo() {
        questionInfoList.last().let {
            it.content = content
            it.imageUri = imgPath
        }
    }

    private fun deleteQuestionInfo(position: Int) {
        questionInfoList[position - 1].let {
            content = it.content
            imgPath = it.imageUri
        }

        questionInfoList.removeAt(position)
    }

    private fun checkStoragePermission(activity: AppCompatActivity): Boolean {
        val permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
            return false
        }

        return true
    }
}
