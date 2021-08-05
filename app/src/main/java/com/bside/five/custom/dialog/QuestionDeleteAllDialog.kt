package com.bside.five.custom.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.bside.five.R
import com.bside.five.databinding.DialogQuestionDeleteAllBinding
import io.reactivex.disposables.CompositeDisposable

class QuestionDeleteAllDialog(context: Context, private val surveyId: String) : Dialog(context, R.style.DefaultDialog) {

    private var binding: DialogQuestionDeleteAllBinding =
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_question_delete_all, null, false)

    private val disposable = CompositeDisposable()

    init {
        setContentView(binding.root)
    }

    override fun show() {
        binding.apply {
            questionDeleteAllOkBtn.setOnClickListener {
                // FIXME : 삭제 구현
            }

            questionDeleteAllCancelBtn.setOnClickListener {
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