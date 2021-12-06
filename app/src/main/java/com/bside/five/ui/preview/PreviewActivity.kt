package com.bside.five.ui.preview

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.bside.five.R
import com.bside.five.base.BaseActivity
import com.bside.five.constants.Constants
import com.bside.five.custom.dialog.DialogPreview
import com.bside.five.databinding.ActivityPreviewBinding

class PreviewActivity : BaseActivity<ActivityPreviewBinding, PreviewViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.activity_preview
    override val viewModelClass: Class<PreviewViewModel>
        get() = PreviewViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkExtra()
        initToolbar()
        listener()
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

    private fun checkExtra() {
        intent.getIntExtra(Constants.EXTRA_NO, 0).let {
            viewModel.setNo(getString(R.string.question_no, it))
        }

        intent.getStringExtra(Constants.EXTRA_CONTENTS)?.let {
            viewModel.setContents(it)
        }

        intent.getParcelableExtra<Uri>(Constants.EXTRA_IMAGE).let {
            viewModel.setImage(it)
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding.previewToolbar as Toolbar)
        supportActionBar?.run {
            setDisplayShowCustomEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.toolbar_preview)
        }
    }

    private fun listener() {
        binding.previewImg.setOnClickListener {
            viewModel.imageLive.value?.let {
                val dialogPreview = DialogPreview(this, it)
                dialogPreview.show()
            }
        }
    }
}