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
    var questionNo = 1
    var content = ObservableField<String>("")
    var imgPath = ObservableField<Uri>(Uri.EMPTY)
    private val questionInfoList = ArrayList<QuestionInfo>()
    lateinit var adapter: ScreenSlidePagerAdapter
    var parentId = -1

    override fun onClickListener(view: View) {
        when (view.id) {
            R.id.newSurveyAddQuestionBtn -> {
                Toast.makeText(view.context, "newSurveyAddQuestionBtn", Toast.LENGTH_LONG).show()
                saveQuestionInfo()
                createChildId()
            }
            R.id.newSurveyFinishQuestionBtn -> {
                Toast.makeText(view.context, "newSurveyFinishQuestionBtn", Toast.LENGTH_LONG).show()

                saveQuestionInfo()

                for (item in questionInfoList) {
                    Log.d(tag, "kch item.no : ${item.no}")
                    Log.d(tag, "kch item.content : ${item.content}")
                    Log.d(tag, "kch item.ImageUri : ${item.ImageUri}")
                }

                // QR 구현 및 완료 화면 출력
            }
            R.id.questionImageContainer -> {
                val activity = view.context as AppCompatActivity

                if (checkStoragePermission(activity)) {
                    ActivityUtil.startGalleryActivity(activity)
                }
            }
            R.id.questionImgRemoveBtn -> {
                imgPath.set(Uri.EMPTY)
            }
        }
    }

    fun init(activity: AppCompatActivity) {
        adapter = ScreenSlidePagerAdapter(activity.supportFragmentManager, activity.lifecycle)
//        createParentId()
    }

    fun createParentId() {
        val getParentId = 1
        parentId = getParentId
        createChildId()
    }

    private fun createChildId() {
//        if (parentId == -1) {
//            Log.d(tag, "ParentId create fail")
//            return
//        }

        // FIXME: 원래 자식 DB 아이디를 넣어서 표기해야하나 DB는 마지막에 구현하기 때문에 질문번호로 대체함
        val getChildId = questionNo
//        val getChildId = 1
        adapter.addFragment(SurveyFragmentInfo(parentId, getChildId))

        pagePositionLive.postValue(adapter.itemCount - 1)
    }

    private fun saveQuestionInfo() {
        questionInfoList.add(QuestionInfo(questionNo, content.get() ?: "", imgPath.get()))

        content.set("")
        imgPath.set(Uri.EMPTY)
        questionNo += 1
    }

    fun deleteQuestionInfo() {
        val position: Int = questionInfoList.lastIndex
        questionInfoList.removeAt(position).let {
            content.set(it.content)
            imgPath.set(it.ImageUri)
            questionNo = it.no
        }

        deletePage(position)
    }

    private fun deletePage(position: Int) {
        val item = adapter.getItem(position + 1)
        adapter.removeFragment(item)
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
