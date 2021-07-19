package com.bside.five.ui.survey

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.bside.five.R
import com.bside.five.base.BaseActivity
import com.bside.five.databinding.ActivityNewSurveyBinding
import com.bside.five.util.ActivityUtil

class NewSurveyActivity : BaseActivity<ActivityNewSurveyBinding, NewSurveyViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.activity_new_survey
    override val viewModelClass: Class<NewSurveyViewModel>
        get() = NewSurveyViewModel::class.java

    private var textCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        viewModel.init(this)
        binding.newSurveyPager.adapter = viewModel.adapter
        binding.newSurveyPager.isUserInputEnabled = false

        subscribe()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                overridePendingTransition(0, 0)
                return true
            }
            R.id.action_preview -> {
                viewModel.questionInfoList.last().let {
                    ActivityUtil.startPreviewActivity(this, it.no, viewModel.content, viewModel.imgPath)
                }

                return true
            }
            R.id.action_delete -> {
                viewModel.deletePage()
                return true
            }
        }
        return false
    }


    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.question, menu)

        menu?.findItem(R.id.action_delete)?.let {
            it.isEnabled = viewModel.adapter.getQuestionItemCount() > 1
        }

        menu?.findItem(R.id.action_preview)?.let {
            it.isEnabled = textCount != 0
            binding.newSurveyAddQuestionBtn.isEnabled = textCount != 0
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

    private fun subscribe() {
        viewModel.pagePositionLive.observe(this, Observer<Int?> { position ->
            position?.let {
                textCount = viewModel.content.length
                binding.newSurveyPager.currentItem = it
                invalidateOptionsMenu()
            }
        })

        viewModel.contentSizeLive.observe(this, Observer<Int?> {
            textCount = it ?: 0
            invalidateOptionsMenu()
        })
    }
}