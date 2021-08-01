package com.bside.five.ui.survey

import android.net.Uri
import android.provider.OpenableColumns
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
import com.bside.five.network.repository.SurveyRepository
import com.bside.five.util.ActivityUtil
import com.bside.five.util.CommonUtil
import com.bside.five.util.FivePreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.Okio


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
                updateQuestionInfo()

                val contentsList = ArrayList<MultipartBody.Part>()
                val imgList = ArrayList<MultipartBody.Part>()

                for (item in questionInfoList) {
                    if (item.imageUri != Uri.EMPTY) {
                        getMultipartBody(view, item.imageUri)?.let {
                            imgList.add(it)
                        }
                    } else {
                        val emptyPart: MultipartBody.Part = MultipartBody.Part.createFormData("questionFileList", "")
                        imgList.add(emptyPart)
                    }

                    contentsList.add(MultipartBody.Part.createFormData("questionContentList", item.contents))
                }

                // FIXME : 질문 내용과 이미지 인덱스를 맞추기 위해 빈값을 무엇을 넣을지 찾아봐야함

                Log.d(tag, "kch contentsList size : ${contentsList.size}")
                Log.d(tag, "kch imgList size : ${imgList.size}")

                disposables.add(
                    SurveyRepository().createSurvey(
                        FivePreference.getAccessToken(view.context),
                        surveyTitle,
                        surveyContents,
                        contentsList,
                        imgList
                    )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ response ->
                            if (response.isSuccess()) {
                                ActivityUtil.startQrCodeActivity(activity, response.data.qrcode_url)
                                activity.finish()
                            }

                            Toast.makeText(view.context, response.msg, Toast.LENGTH_LONG).show()
                        }, { t: Throwable? -> t?.printStackTrace() })
                )
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

    private fun getMultipartBody(view: View, imageUri: Uri): MultipartBody.Part? {
        val name = "questionFileList"

        return view.context.contentResolver.query(imageUri, null, null, null, null)?.let {
            if (it.moveToNext()) {
                val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                val requestBody = object : RequestBody() {
                    override fun contentType(): MediaType? {
                        return view.context.contentResolver.getType(imageUri)
                            ?.let { type -> MediaType.parse(type) }
                    }

                    override fun writeTo(sink: BufferedSink) {
                        sink.writeAll(Okio.source(view.context.contentResolver.openInputStream(imageUri)))
                    }
                }
                it.close()

                MultipartBody.Part.createFormData(name, displayName, requestBody)
            } else {
                it.close()
                null
            }
        }
    }
}
