package com.bside.five.ui.answer

import android.view.View
import androidx.databinding.ObservableArrayList
import com.bside.five.base.BaseViewModel
import com.bside.five.model.SurveyResult
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

    fun requestAnswerAPI() {
        disposables.add(
            SurveyRepository().requestSurveyDetail(FivePreference.getAccessToken(), surveyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.isSuccess()) {
                        requestAllQuestionsAnswers(response.data.question)
                    }
                }, { t: Throwable? ->
                    t?.printStackTrace()
                })
        )
    }

    private fun requestAllQuestionsAnswers(questions: ArrayList<MySurveyDetailResponse.Question>) {
        val observableArrayList = ArrayList<Single<AnswerListResponse>>()

        questions.forEach {
            observableArrayList.add(
                SurveyRepository().requestSurveyAnswer(
                    FivePreference.getAccessToken(),
                    surveyId,
                    it.question_id
                )
            )
        }

        var position = 0

        disposables.add(
            Single.concat(observableArrayList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.isSuccess()) {
                        val resultList = ArrayList<SurveyResult>()
                        resultList.add(SurveyResult.QuestionUI(questions[position++]))

                        response.data.answer.forEachIndexed { i, v ->
                            if (i == response.data.answer.lastIndex) {
                                v.isMore = response.data.is_more
                            }

                            resultList.add(SurveyResult.AnswerUI(v))
                        }

                        if (response.data.answer.isNotEmpty()) {
                            items.addAll(resultList)
                        }
                    }

                }, { t: Throwable? -> t?.printStackTrace() })
        )
    }

    fun requestAnswers(questionId: String, position: Int, page: Int) {
        disposables.add(
            SurveyRepository().requestSurveyAnswer(
                FivePreference.getAccessToken(),
                surveyId,
                questionId,
                page.toString()
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.isSuccess()) {
                        val resultList = ArrayList<SurveyResult>()

                        response.data.answer.forEachIndexed { i, v ->
                            if (i == response.data.answer.lastIndex) {
                                v.isMore = response.data.is_more
                                v.page = page
                            }

                            resultList.add(SurveyResult.AnswerUI(v))
                        }

                        (items[position] as SurveyResult.AnswerUI).answer.isMore = false
                        items.addAll(position + 1, resultList)
                    }
                }, { t: Throwable? ->
                    t?.printStackTrace()
                })
        )
    }
}