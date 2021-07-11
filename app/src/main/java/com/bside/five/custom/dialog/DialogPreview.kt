package com.bside.five.custom.dialog

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.bside.five.R
import com.bside.five.databinding.DialogPreviewBinding
import com.bside.five.util.GlideUtil

class DialogPreview(context: Context, val uri: Uri) : Dialog(context) {
    private var binding: DialogPreviewBinding =
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_preview, null, false)

    // FIXME : 레이아웃 축소 현상 수정해야함
    init {
        setContentView(binding.root)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        val params = window?.attributes
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        params?.height = WindowManager.LayoutParams.MATCH_PARENT
        window?.attributes = params
    }

    override fun show() {
        GlideUtil.loadImage(binding.dialogPreviewImg, uri)

        binding.dialogPreviewCloseBtn.setOnClickListener {
            dismiss()
        }

        super.show()
    }
}