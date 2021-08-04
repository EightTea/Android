package com.bside.five.ui.answer

import android.content.Context
import android.util.Log
import android.view.View
import androidx.databinding.ObservableArrayList
import com.bside.five.base.BaseViewModel
import com.bside.five.model.*
import com.bside.five.network.repository.SurveyRepository
import com.bside.five.network.response.AnswerListResponse
import com.bside.five.network.response.MySurveyDetailResponse
import com.bside.five.util.FivePreference
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AnswerViewModel : BaseViewModel() {

    private val tag = this::class.java.simpleName
    val items = ObservableArrayList<SurveyResult>()
    var surveyId = ""

    override fun onClickListener(view: View) {}

    fun requestAnswerAPI(context: Context) {
        disposables.add(
            SurveyRepository().requestSurveyDetail(FivePreference.getAccessToken(context), surveyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.isSuccess()) {
                        response.data.qrcode_url

                        requestAnswers(response.data.question, context)
                    }
                }, { t: Throwable? ->
                    t?.printStackTrace()
                })
        )
    }

    private fun requestAnswers(questions: ArrayList<MySurveyDetailResponse.Question>, context: Context) {
        val observableArrayList = ArrayList<Single<AnswerListResponse>>()

        questions.forEach {
            observableArrayList.add(
                SurveyRepository().requestSurveyAnswer(
                    FivePreference.getAccessToken(context),
                    surveyId,
                    it.question_id
                )
            )
        }

        val resultList = ArrayList<SurveyResult>()
        var position = 0

        disposables.add(
            Single.concat(observableArrayList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.isSuccess()) {
                        resultList.add(SurveyResult.QuestionUI(questions[position++]))

                        response.data.answer.forEachIndexed { i, v ->
                            if (i == response.data.answer.lastIndex) {
                                v.isMore = response.data.is_more
                            }

                            resultList.add(SurveyResult.AnswerUI(v))
                        }

                        items.addAll(resultList)
                    }

                }, { t: Throwable? -> t?.printStackTrace() })
        )
    }
}