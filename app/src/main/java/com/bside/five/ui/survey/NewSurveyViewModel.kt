package com.bside.five.ui.survey

import android.net.Uri
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bside.five.R
import com.bside.five.adapter.ScreenSlidePagerAdapter
import com.bside.five.base.BaseViewModel
import com.bside.five.model.QuestionInfo
import com.bside.five.model.SurveyFragmentInfo
import com.bside.five.network.repository.SurveyRepository
import com.bside.five.util.FivePreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody


class NewSurveyViewModel : BaseViewModel() {

    companion object {
        const val QUESTION_SIZE_MAX = 10
    }

    private val _pagePositionLive: MutableLiveData<Int> = MutableLiveData()
    val pagePositionLive: LiveData<Int> get() = _pagePositionLive
    private val _contentsSizeLive: MutableLiveData<Int> = MutableLiveData()
    val contentsSizeLive: LiveData<Int> get() = _contentsSizeLive
    private val _clearImageLive: MutableLiveData<Int> = MutableLiveData()
    val clearImageLive: LiveData<Int> get() = _clearImageLive
    private val _snackBarLive: MutableLiveData<Int> = MutableLiveData()
    val snackBarLive: LiveData<Int> get() = _snackBarLive
    private val _isEnableStartSurveyLive: MutableLiveData<Boolean> = MutableLiveData(false)
    val isEnableStartSurveyLive: LiveData<Boolean> get() = _isEnableStartSurveyLive
    private val _createCompleteLive: MutableLiveData<String> = MutableLiveData()
    val createCompleteLive: LiveData<String> get() = _createCompleteLive
    private val _toastLive: MutableLiveData<String> = MutableLiveData()
    val toastLive: LiveData<String> get() = _toastLive
    private val _isSurveyLive: MutableLiveData<Boolean> = MutableLiveData(true)
    val isSurvey: LiveData<Boolean> get() = _isSurveyLive
    var surveyTitle = ""
    var surveyContents = ""
    var questionNo = 1
    var contents = ""
    var imgPath: Uri = Uri.EMPTY
    val questionInfoList = ArrayList<QuestionInfo>()
    lateinit var adapter: ScreenSlidePagerAdapter

    fun setContentsSize(size: Int) {
        _contentsSizeLive.value = size
    }

    fun setEnableStartSurvey(isEnable: Boolean) {
        _isEnableStartSurveyLive.value = isEnable
    }

    override fun onClickListener(view: View) {}

    fun startQuestion() {
        createPage()
        _isSurveyLive.value = false
        _snackBarLive.value = R.string.new_question_title_guide
    }

    fun addQuestion() {
        if (questionInfoList.size < QUESTION_SIZE_MAX) {
            updateQuestionInfo()
            createPage()
        }
    }

    fun removeQuestionImage() {
        imgPath = Uri.EMPTY
        _clearImageLive.value = questionNo - 1
    }

    private fun createPage() {
        addQuestionInfo()

        adapter.addFragment(SurveyFragmentInfo(1, questionNo))
        _pagePositionLive.value = adapter.itemCount - 1
        questionNo += 1
    }

    fun deletePage() {
        val position: Int = questionInfoList.lastIndex

        deleteQuestionInfo(position)

        val item = adapter.getItem(position)
        adapter.removeFragment(item)
        _pagePositionLive.value = adapter.itemCount - 1
        questionNo -= 1
    }

    private fun addQuestionInfo() {
        questionInfoList.add(QuestionInfo(questionNo, "", Uri.EMPTY))
        imgPath = Uri.EMPTY
    }

    fun updateQuestionInfo() {
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

    fun createSurvey(contentsList: ArrayList<MultipartBody.Part>, imgList: ArrayList<MultipartBody.Part>) {
        disposables.add(
            SurveyRepository().createSurvey(
                FivePreference.getAccessToken(),
                surveyTitle,
                surveyContents,
                contentsList,
                imgList
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.isSuccess()) {
                        _createCompleteLive.value = response.data.survey_id
                    }

                    _toastLive.value = response.msg
                }, { t: Throwable? -> t?.printStackTrace() })
        )
    }
}
