package com.bside.five.ui.my

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.bside.five.R
import com.bside.five.adapter.SurveyStateAdapter
import com.bside.five.base.BaseActivity
import com.bside.five.constants.Constants
import com.bside.five.custom.listener.OnSuccessListener
import com.bside.five.databinding.ActivityMySurveyBinding
import com.bside.five.util.ActivityUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MySurveyActivity : BaseActivity<ActivityMySurveyBinding, MySurveyViewModel>(), View.OnClickListener {

    private lateinit var adapter: SurveyStateAdapter

    private val state = linkedMapOf(
        "전체" to "",
        "진행중" to Constants.STATUS_IN_PROGRESS,
        "수정중" to Constants.STATUS_PENDING,
        "종료" to Constants.STATUS_END
    )
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mySurveyFilterBtn -> {
                showFilterDialog()
            }
            R.id.mySurveyEmptyBtn -> {
                ActivityUtil.startNewSurveyActivity(this)
                finish()
            }
        }
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

    private fun showFilterDialog() {
        val filterItems = state.keys.toTypedArray()

        MaterialAlertDialogBuilder(this)
            .setTitle("상태")
            .setItems(filterItems) { dialog, which ->
                binding.mySurveyFilterTitle.text = filterItems[which]
                viewModel.filter(state[filterItems[which]] ?: "")
            }
            .show()
    }
}