package com.bside.five.ui.survey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.bside.five.R
import com.bside.five.adapter.ScreenSlidePagerAdapter
import com.bside.five.base.BaseActivity
import com.bside.five.databinding.ActivityNewSurveyBinding

class NewSurveyActivity : BaseActivity<ActivityNewSurveyBinding, NewSurveyViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.activity_new_survey
    override val viewModelClass: Class<NewSurveyViewModel>
        get() = NewSurveyViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        viewModel.init(this)
        binding.newSurveyPager.adapter = viewModel.adapter
        binding.newSurveyPager.isUserInputEnabled = false

        viewModel.pagePositionLive.observe(this, Observer<Int?> { position ->
            position?.let {
                binding.newSurveyPager.currentItem = it
            }
        })
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
                return true
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.question, menu)
        return true
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