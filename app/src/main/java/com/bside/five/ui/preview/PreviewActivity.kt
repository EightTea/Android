package com.bside.five.ui.preview

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.bside.five.R
import com.bside.five.base.BaseActivity
import com.bside.five.constants.Constants
import com.bside.five.databinding.ActivityPreviewBinding
import com.bside.five.util.GlideUtil

class PreviewActivity : BaseActivity<ActivityPreviewBinding, PreviewViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.activity_preview
    override val viewModelClass: Class<PreviewViewModel>
        get() = PreviewViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.getIntExtra(Constants.EXTRA_NO, 0).let {
            viewModel.no.set(getString(R.string.question_no, it))
        }

        intent.getStringExtra(Constants.EXTRA_CONTENT)?.let {
            viewModel.content.set(it)
        }

        intent.getParcelableExtra<Uri>(Constants.EXTRA_IMAGE).let {
            viewModel.image.set(it)
        }

        initToolbar()
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
        setSupportActionBar(binding.previewToolbar as Toolbar)
        supportActionBar?.run {
            setDisplayShowCustomEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }


}