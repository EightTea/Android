package com.bside.five.ui.survey

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.bside.five.R
import com.bside.five.adapter.ScreenSlidePagerAdapter
import com.bside.five.base.BaseActivity
import com.bside.five.custom.dialog.CompleteCheckDialog
import com.bside.five.custom.dialog.QuestionDeleteDialog
import com.bside.five.databinding.ActivityNewSurveyBinding
import com.bside.five.model.QuestionInfo
import com.bside.five.util.ActivityUtil
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.Okio
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class NewSurveyActivity : BaseActivity<ActivityNewSurveyBinding, NewSurveyViewModel>() {

    private val tag = NewSurveyActivity::class.java.simpleName
    private var textCount = 0

    override val layoutResourceId: Int
        get() = R.layout.activity_new_survey
    override val viewModelClass: Class<NewSurveyViewModel>
        get() = NewSurveyViewModel::class.java


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()
        initPager()
        subscribe()
        listener()
        showSnackBarAction(R.string.new_survey_title_guide)
    }

    private fun listener() {
        binding.newSurveySampleBtn.setOnClickListener {
            ActivityUtil.startSampleActivity(this)
        }
        binding.newSurveyFinishQuestionBtn.setOnClickListener {
            val dialog = CompleteCheckDialog(this, View.OnClickListener {
                viewModel.updateQuestionInfo()
                val (contentsList, imgList) = convertMultipart(viewModel.questionInfoList)
                viewModel.createSurvey(contentsList, imgList)
            })

            dialog.show()
        }
    }

    private fun convertMultipart(questionInfoList: ArrayList<QuestionInfo>): Pair<ArrayList<MultipartBody.Part>, ArrayList<MultipartBody.Part>> {
        val contentsList = ArrayList<MultipartBody.Part>()
        val imgList = ArrayList<MultipartBody.Part>()

        for (item in questionInfoList) {
            if (item.imageUri != Uri.EMPTY) {
                val img = getMultipartBody(item.imageUri)

                if (img != null) {
                    imgList.add(img)
                } else {
                    getEmptyMultipartBody().let {
                        imgList.add(it)
                    }
                }
            } else {
                getEmptyMultipartBody().let {
                    imgList.add(it)
                }
            }

            contentsList.add(MultipartBody.Part.createFormData("questionContentList", item.contents))
        }
        return Pair(contentsList, imgList)
    }


    private fun getEmptyMultipartBody(): MultipartBody.Part {
        val emptyFileName = "empty.jpeg"
        val cacheFile = File(cacheDir, emptyFileName)

        var os: OutputStream? = null
        try {
            os = BufferedOutputStream(FileOutputStream(cacheFile))
            os.write("".toByteArray())
            os.close()
        } finally {
            os?.close()
        }

        val requestBody: RequestBody = RequestBody.create(MediaType.parse("image/jpeg"), cacheFile)
        val emptyPart: MultipartBody.Part = MultipartBody.Part.createFormData("questionFileList", emptyFileName, requestBody)
        return emptyPart
    }

    private fun getMultipartBody(imageUri: Uri): MultipartBody.Part? {
        val name = "questionFileList"

        return contentResolver.query(imageUri, null, null, null, null)?.let {
            if (it.moveToNext()) {
                val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                val requestBody = object : RequestBody() {
                    override fun contentType(): MediaType? {
                        return contentResolver.getType(imageUri)
                            ?.let { type -> MediaType.parse(type) }
                    }

                    override fun writeTo(sink: BufferedSink) {
                        sink.writeAll(Okio.source(contentResolver.openInputStream(imageUri)))
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                overridePendingTransition(0, 0)
                return true
            }
            R.id.action_preview -> {
                if (viewModel.adapter.getQuestionItemCount() == 0) {
                    ActivityUtil.startSampleActivity(this, viewModel.surveyTitle, viewModel.surveyContents)
                    return true
                }

                viewModel.questionInfoList.last().let {
                    ActivityUtil.startPreviewActivity(this, it.no, viewModel.contents, viewModel.imgPath)
                }

                return true
            }
            R.id.action_delete -> {
                if (viewModel.adapter.getQuestionItemCount() > 1) {
                    val dialog = QuestionDeleteDialog(this, View.OnClickListener {
                        viewModel.deletePage()
                        showSnackBar(R.string.question_delete_complete_msg)
                    })
                    dialog.show()
                } else {
                    showSnackBar(R.string.question_first_delete_guide)
                }
                return true
            }
        }
        return false
    }


    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.question, menu)

        if (viewModel.adapter.getQuestionItemCount() > 1) {
            menu?.findItem(R.id.action_delete)?.setIcon(R.drawable.ic_nav_delete)
        } else {
            menu?.findItem(R.id.action_delete)?.setIcon(R.drawable.ic_nav_delete_disable)
        }

        menu?.findItem(R.id.action_preview)?.let {
            it.isEnabled = textCount != 0
            binding.newSurveyAddQuestionBtn.isEnabled = (textCount != 0) && viewModel.questionInfoList.size < NewSurveyViewModel.QUESTION_SIZE_MAX
            binding.newSurveyFinishQuestionBtn.isEnabled = textCount != 0
        }

        menu?.findItem(R.id.action_delete)?.isVisible = viewModel.adapter.itemCount != 1

        return true
    }

    private fun initToolbar() {
        setSupportActionBar(binding.newSurveyToolbar as Toolbar)
        supportActionBar?.run {
            setDisplayShowCustomEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.toolbar_add_survey)
        }
    }

    private fun initPager() {
        viewModel.adapter = ScreenSlidePagerAdapter(supportFragmentManager, lifecycle)
        binding.newSurveyPager.adapter = viewModel.adapter
        binding.newSurveyPager.isUserInputEnabled = false
    }

    private fun subscribe() {
        viewModel.pagePositionLive.observe(this, Observer<Int?> { position ->
            position?.let {
                textCount = viewModel.contents.length
                binding.newSurveyPager.currentItem = it
                invalidateOptionsMenu()
            }
        })

        viewModel.contentsSizeLive.observe(this, Observer<Int?> {
            textCount = it ?: 0
            invalidateOptionsMenu()
        })

        viewModel.snackBarLive.observe(this, Observer<Int?> { msg ->
            msg?.let {
                showSnackBarAction(it)
            }
        })

        viewModel.createCompleteLive.observe(this, Observer<String?> { survey_id ->
            survey_id?.let {
                ActivityUtil.startQrCodeActivity(this@NewSurveyActivity, it, true)
                finish()
            }
        })

        viewModel.toastLive.observe(this, Observer<String?> { msg ->
            Toast.makeText(this@NewSurveyActivity, msg, Toast.LENGTH_LONG).show()
        })
    }
}