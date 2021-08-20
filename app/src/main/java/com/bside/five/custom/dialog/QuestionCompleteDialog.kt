package com.bside.five.custom.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bside.five.R
import com.bside.five.constants.Constants
import com.bside.five.custom.listener.OnSuccessListener
import com.bside.five.databinding.DialogQuestionCompleteBinding
import com.bside.five.network.repository.SurveyRepository
import com.bside.five.util.FivePreference
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class QuestionCompleteDialog(context: Context, private val surveyId: String, val successListener: OnSuccessListener) :
    Dialog(context, R.style.DefaultDialog) {

    private var binding: DialogQuestionCompleteBinding =
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_question_complete, null, false)

    private val disposable = CompositeDisposable()

    init {
        setContentView(binding.root)
    }

    override fun show() {
        binding.apply {
            questionCompleteOkBtn.setOnClickListener {
                requestSurveyComplete()
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

    private fun requestSurveyComplete() {
        disposable.add(
            SurveyRepository().requestSurveyStateChange(
                FivePreference.getAccessToken(),
                surveyId,
                Constants.STATUS_END
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.isSuccess()) {
                        Snackbar.make(binding.root, R.string.survey_end_msg, Snackbar.LENGTH_SHORT).show()
                        successListener.onSuccess()
                    } else {
                        Snackbar.make(binding.root, response.msg ?: "요청이 실패하였습니다.", Snackbar.LENGTH_SHORT).show()
                    }
                    dismiss()
                }, { t: Throwable? ->
                    t?.printStackTrace()
                    Toast.makeText(context, "다시 시도해주세요.", Toast.LENGTH_LONG).show()
                    dismiss()
                })
        )
    }
}