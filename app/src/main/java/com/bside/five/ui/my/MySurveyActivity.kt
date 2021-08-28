package com.bside.five.ui.my

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.bside.five.R
import com.bside.five.adapter.SurveyStateAdapter
import com.bside.five.base.BaseActivity
import com.bside.five.custom.listener.OnSuccessListener
import com.bside.five.databinding.ActivityMySurveyBinding

class MySurveyActivity : BaseActivity<ActivityMySurveyBinding, MySurveyViewModel>() {

    private lateinit var adapter: SurveyStateAdapter

    override val layoutResourceId: Int
        get() = R.layout.activity_my_survey
    override val viewModelClass: Class<MySurveyViewModel>
        get() = MySurveyViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()
        initRecycler()
        viewModel.requestSurvey()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                overridePendingTransition(0, 0)
                return true
            }
        }
        return false
    }

    private fun initToolbar() {
        setSupportActionBar(binding.mySurveyToolbar as Toolbar)
        supportActionBar?.run {
            setDisplayShowCustomEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.toolbar_my_survey)
        }
    }

    private fun initRecycler() {
        adapter = SurveyStateAdapter(object : OnSuccessListener {
            override fun onSuccess() {
                viewModel.requestSurvey()
            }
        })

        binding.mySurveyRecyclerView.adapter = adapter
    }

}