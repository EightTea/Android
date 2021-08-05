package com.bside.five.custom.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bside.five.R
import com.bside.five.databinding.DialogQuestionCompleteBinding
import com.bside.five.network.repository.SurveyRepository
import com.bside.five.util.FivePreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class QuestionCompleteDialog(context: Context, private val surveyId: String) : Dialog(context, R.style.DefaultDialog) {

    private var binding: DialogQuestionCompleteBinding =
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_question_complete, null, false)

    private val disposable = CompositeDisposable()

    init {
        setContentView(binding.root)
    }

    override fun show() {
        binding.apply {
            questionCompleteOkBtn.setOnClickListener {
                requestSurveyComplete(it)
            }

            questionCompleteCancelBtn.setOnClickListener {
                dismiss()
            }
        }

        super.show()
    }

    override fun dismiss() {
        disposable.dispose()
        super.dismiss()
    }

    private fun requestSurveyComplete(view: View) {
        disposable.add(
            SurveyRepository().requestSurveyStateChange(
                FivePreference.getAccessToken(view.context),
                surveyId,
                "complete"
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    Toast.makeText(view.context, response.msg, Toast.LENGTH_LONG).show()
                    dismiss()
                }, { t: Throwable? ->
                    t?.printStackTrace()
                    Toast.makeText(view.context, "다시 시도해주세요.", Toast.LENGTH_LONG).show()
                    dismiss()
                })
        )
    }
}