package com.bside.five.ui.answer

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.motion.widget.MotionLayout
import com.bside.five.R
import com.bside.five.base.BaseActivity
import com.bside.five.databinding.ActivityAnswerBinding

class AnswerActivity : BaseActivity<ActivityAnswerBinding, AnswerViewModel>() {

    private val tag = this::class.java.simpleName

    override val layoutResourceId: Int
        get() = R.layout.activity_answer
    override val viewModelClass: Class<AnswerViewModel>
        get() = AnswerViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()
//        viewModel.requestAnswerAPI()

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

    private fun initToolbar() {
        setSupportActionBar(binding.answerToolbar as Toolbar)
        supportActionBar?.run {
            setDisplayShowCustomEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.toolbar_answer)
        }
    }
}