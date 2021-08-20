package com.bside.five.ui.answer

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import com.bside.five.R
import com.bside.five.adapter.AnswerAdapter
import com.bside.five.base.BaseActivity
import com.bside.five.constants.Constants
import com.bside.five.custom.listener.OnMoreListener
import com.bside.five.databinding.ActivityAnswerBinding

class AnswerActivity : BaseActivity<ActivityAnswerBinding, AnswerViewModel>() {

    private val tag = this::class.java.simpleName

    private var title = ""
    private var answerCount = 0
    private var status = Constants.STATUS_IN_PROGRESS

    override val layoutResourceId: Int
        get() = R.layout.activity_answer
    override val viewModelClass: Class<AnswerViewModel>
        get() = AnswerViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initIntent()
        initToolbar()
        initView()
        initRecyclerView()
        viewModel.requestAnswerAPI()

        binding.answerContainer.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

            override fun onTransitionCompleted(p0: MotionLayout?, currentId: Int) {
                if (currentId == R.id.end) {
                    binding.answerTopView.transitionToEnd()
                } else {
                    binding.answerTopView.transitionToStart()
                }
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
        }
        return false
    }

    private fun initIntent() {
        intent.let {
            viewModel.surveyId = it.getStringExtra(Constants.EXTRA_SURVEY_ID) ?: ""
            title = it.getStringExtra(Constants.EXTRA_TITLE) ?: ""
            answerCount = it.getIntExtra(Constants.EXTRA_ANSWER_COUNT, 0)
            status = it.getStringExtra(Constants.EXTRA_STATUS) ?: Constants.STATUS_IN_PROGRESS
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding.answerToolbar as Toolbar)
        supportActionBar?.run {
            setDisplayShowCustomEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.toolbar_answer)
        }
    }

    private fun initRecyclerView() {
        val adapter = AnswerAdapter(object : OnMoreListener {
            override fun onMore(questionId: String, position: Int, page: Int) {
                viewModel.requestAnswers(questionId, position, page + 1)
            }
        })
        adapter.setSurveyId(viewModel.surveyId)
        binding.answerRecyclerView.adapter = adapter
    }

    private fun initView() {

        when (status) {
            Constants.STATUS_IN_PROGRESS -> {
                binding.answerStateContainer.setBackgroundResource(R.drawable.border_primary)
                binding.answerState.setText(R.string.survey_state_ing)
                binding.answerState.setTextColor(ContextCompat.getColor(this, R.color.primary))
            }
            Constants.STATUS_PENDING -> {
                binding.answerStateContainer.setBackgroundResource(R.drawable.border_757575)
                binding.answerState.setText(R.string.survey_state_before)
                binding.answerState.setTextColor(ContextCompat.getColor(this, R.color.contents_text))
            }
            Constants.STATUS_END -> {
                binding.answerStateContainer.setBackgroundResource(R.color.complete)
                binding.answerState.setText(R.string.survey_state_end)
                binding.answerState.setTextColor(ContextCompat.getColor(this, R.color.complete_text))
            }
        }

        binding.answerSurveyTitle.text = title
        binding.answerSurveySmallTitle.text = title

        if (answerCount == 0) {
            binding.answerCountInfo.text = "아직 답변이 없어요!"
        } else {
            binding.answerCountInfo.text = "소중한 4개의 답변이 있어요."
        }
    }
}