package com.bside.five.ui.survey

import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.bside.five.R
import com.bside.five.adapter.ScreenSlidePagerAdapter
import com.bside.five.base.BaseViewModel
import com.bside.five.model.QuestionInfo
import com.bside.five.model.SurveyFragmentInfo
import com.bside.five.util.ActivityUtil
import com.bside.five.util.CommonUtil

class NewSurveyViewModel : BaseViewModel() {

    companion object {
        const val QUESTION_SIZE_MAX = 10
    }

    private val tag = NewSurveyViewModel::class.java.simpleName
    var pagePositionLive: MutableLiveData<Int> = MutableLiveData()
    var contentsSizeLive: MutableLiveData<Int> = MutableLiveData()
    var clearImageLive: MutableLiveData<Int> = MutableLiveData()
    var isSurvey = ObservableField<Boolean>(true)
    var isEnableStartSurvey = ObservableField<Boolean>(false)
    var surveyTitle = ""
    var surveyContents = ""
    var questionNo = 1
    var contents = ""
    var imgPath: Uri = Uri.EMPTY
    val questionInfoList = ArrayList<QuestionInfo>()
    lateinit var adapter: ScreenSlidePagerAdapter

    override fun onClickListener(view: View) {
        when (view.id) {
            R.id.newSurveyStartBtn -> {
                createPage()
                isSurvey.set(false)
            }
            R.id.newSurveyAddQuestionBtn -> {
                if (questionInfoList.size < QUESTION_SIZE_MAX) {
                    updateQuestionInfo()
                    createPage()
                }
            }
            R.id.newSurveyFinishQuestionBtn -> {
                val activity = view.context as AppCompatActivity
                Toast.makeText(view.context, "newSurveyFinishQuestionBtn", Toast.LENGTH_LONG).show()

                updateQuestionInfo()

                for (item in questionInfoList) {
                    Log.d(tag, "kch item.no : ${item.no}")
                    Log.d(tag, "kch item.content : ${item.contents}")
                    Log.d(tag, "kch item.ImageUri : ${item.imageUri}")
                }

                ActivityUtil.startQrCodeActivity(activity, "https://www.naver.com/")
                activity.finish()
            }
            R.id.questionImageContainer -> {
                val activity = view.context as AppCompatActivity
                val fragment = activity.supportFragmentManager.fragments.last()

                if (CommonUtil.checkStoragePermission(activity)) {
                    ActivityUtil.startGalleryActivity(activity, fragment)
                }
            }
            R.id.questionImgRemoveBtn -> {
                imgPath = Uri.EMPTY
                clearImageLive.postValue(questionNo - 1)
            }
            R.id.surveyInfoSampleBtn -> {
                ActivityUtil.startSampleActivity(view.context as AppCompatActivity)
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
            it.contents = contents
            it.imageUri = imgPath
        }
    }

    private fun deleteQuestionInfo(position: Int) {
        questionInfoList[position - 1].let {
            contents = it.contents
            imgPath = it.imageUri
        }

        questionInfoList.removeAt(position)
    }
}
