package com.bside.five.ui.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.bside.five.R
import com.bside.five.constants.Constants
import com.bside.five.databinding.ActivitySampleBinding

class SampleActivity : AppCompatActivity() {

    private val binding: ActivitySampleBinding by lazy {
        DataBindingUtil.setContentView<ActivitySampleBinding>(this, R.layout.activity_sample)
    }

    private var isSample = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.getStringExtra(Constants.EXTRA_TITLE)?.let {
            binding.sampleTitle.text = it
            isSample = false
        }

        intent.getStringExtra(Constants.EXTRA_CONTENTS)?.let {
            binding.sampleContents.text = it
        }

        initToolbar()

        binding.sampleBtn.setOnClickListener {
            finish()
        }
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
        setSupportActionBar(binding.sampleToolbar as Toolbar)
        supportActionBar?.run {
            setDisplayShowCustomEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            if (isSample) {
                setTitle(R.string.toolbar_sample)
            } else {
                setTitle(R.string.toolbar_preview)
            }
        }
    }
}