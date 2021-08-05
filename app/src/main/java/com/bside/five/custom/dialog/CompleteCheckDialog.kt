package com.bside.five.custom.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.bside.five.R
import com.bside.five.databinding.DialogCompleteCheckBinding
import io.reactivex.disposables.CompositeDisposable

class CompleteCheckDialog(context: Context, private val okListener: View.OnClickListener) : Dialog(context, R.style.DefaultDialog) {

    private var binding: DialogCompleteCheckBinding =
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_complete_check, null, false)

    private val disposable = CompositeDisposable()

    init {
        setContentView(binding.root)
    }

    override fun show() {
        binding.apply {
            completeCheckOkBtn.setOnClickListener {
                okListener.onClick(it)
                dismiss()
            }

            completeCheckCancelBtn.setOnClickListener {
                dismiss()
            }
        }

        super.show()
    }

    override fun dismiss() {
        disposable.dispose()
        super.dismiss()
    }
}