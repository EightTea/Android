package com.bside.five.custom.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.bside.five.R
import com.bside.five.databinding.DialogQuestionDeleteBinding
import io.reactivex.disposables.CompositeDisposable

class QuestionDeleteDialog(context: Context, val listener: View.OnClickListener) : Dialog(context, R.style.DefaultDialog) {

    private var binding: DialogQuestionDeleteBinding =
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_question_delete, null, false)

    private val disposable = CompositeDisposable()

    init {
        setContentView(binding.root)
    }

    override fun show() {
        binding.apply {
            questionDeleteOkBtn.setOnClickListener {
                listener.onClick(it)
                dismiss()
            }

            questionDeleteCancelBtn.setOnClickListener {
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