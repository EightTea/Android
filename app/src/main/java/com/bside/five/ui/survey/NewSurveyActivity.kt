package com.bside.five.ui.survey

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.bside.five.R
import com.bside.five.adapter.ScreenSlidePagerAdapter
import com.bside.five.base.BaseActivity
import com.bside.five.custom.dialog.QuestionDeleteDialog
import com.bside.five.databinding.ActivityNewSurveyBinding
import com.bside.five.util.ActivityUtil

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
        showSnackBarAction(R.string.new_survey_title_guide)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
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

        viewModel.snackbarLive.observe(this, Observer<Int?> { msg ->
            msg?.let {
                showSnackBarAction(it)
            }
        })
    }
}