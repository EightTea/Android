package com.bside.five.custom.dialog

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.bside.five.R
import com.bside.five.databinding.DialogPreviewBinding
import com.bside.five.util.GlideUtil

class DialogPreview(context: Context, val uri: Uri) : Dialog(context, R.style.ImageDialog) {
    private var binding: DialogPreviewBinding =
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_preview, null, false)

    // FIXME : 테마 추가 했으나 위아래 너무 길면 닫기 아래로 내려가고, 좌우를 좁히면 좌우가 긴 사진은 등록 이미지 크기보다 작게 나옴
    init {
        setContentView(binding.root)
    }

    override fun show() {
        GlideUtil.loadImage(binding.dialogPreviewImg, uri, ImageView.ScaleType.FIT_XY)

        binding.dialogPreviewCloseBtn.setOnClickListener {
            dismiss()
        }

        super.show()
    }
}