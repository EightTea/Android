package com.bside.five.ui.my

import android.content.Context
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.bside.five.base.BaseViewModel
import com.bside.five.model.Survey
import com.bside.five.network.repository.SurveyRepository
import com.bside.five.util.FivePreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MySurveyViewModel : BaseViewModel() {

    companion object {
        const val STATUS_PENDING = "PENDING"
        const val STATUS_IN_PROGRESS = "IN_PROGRESS"
        const val STATUS_END = "END"
    }

    private val tag = this::class.java.simpleName
    var items = ObservableArrayList<Survey>()

    override fun onClickListener(view: View) {
        // FIXME : 필터 추가 예정
    }

    fun requestSurvey(context: Context) {
        disposables.add(
            SurveyRepository().requestSurveyList(FivePreference.getAccessToken(context))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.isSuccess()) {

                        val resultList = ArrayList<Survey>()

                        response.data.survey_list.let { list ->
                            list.filter {
                                STATUS_PENDING.equals(it.status, true)
                            }.forEach {
                                resultList.add(Survey.Under(it))
                            }

                            list.filter {
                                STATUS_END.equals(it.status, true)
                            }.forEach {
                                resultList.add(Survey.End(it))
                            }

                            list.filter {
                                STATUS_IN_PROGRESS.equals(it.status, true)
                            }.forEach {
                                resultList.add(Survey.Incomplete(it))
                            }
                        }

                        items.addAll(resultList)
                    }

                }, { t: Throwable? -> t?.printStackTrace() })
        )
    }
}