package com.bside.five.ui.survey

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
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

        viewModel.createPage()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                overridePendingTransition(0, 0)
                return true
            }
            R.id.action_preview -> {
                Toast.makeText(this, "action_preview", Toast.LENGTH_LONG).show()
                for (d in viewModel.questionInfoList) {
                    Log.d("tag", "kch action_preview no ${d.no}")
                    Log.d("tag", "kch action_preview content ${d.content}")
                    Log.d("tag", "kch action_preview imageUri ${d.imageUri}")
                }

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
        menu?.findItem(R.id.action_preview)?.let {
            it.isEnabled = textCount != 0
            binding.newSurveyAddQuestionBtn.isEnabled = textCount != 0
            binding.newSurveyFinishQuestionBtn.isEnabled = textCount != 0
        }
        menu?.findItem(R.id.action_delete)?.let {
            it.isEnabled = viewModel.adapter.itemCount > 1
        }

        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Permission granted
                Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show()
            } else {
                // Permission denied or canceled
                Toast.makeText(this, "Permission denied or canceled", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding.newSurveyToolbar as Toolbar)
        supportActionBar?.run {
            setDisplayShowCustomEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }

}