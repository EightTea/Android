package com.bside.five.ui.my

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.bside.five.R
import com.bside.five.base.BaseViewModel
import com.bside.five.constants.Constants
import com.bside.five.model.Survey
import com.bside.five.network.repository.SurveyRepository
import com.bside.five.network.response.MySurveyListResponse
import com.bside.five.util.ActivityUtil
import com.bside.five.util.FivePreference
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MySurveyViewModel : BaseViewModel() {

    private val tag = this::class.java.simpleName
    var items = ObservableArrayList<Survey>()
    var totalItems = ObservableArrayList<MySurveyListResponse.MySurveyInfo>()
    val filterTitle = ObservableField<String>("전체")
    private var filterList = ""

    private val state = linkedMapOf(
        "전체" to "",
        "진행중" to Constants.STATUS_IN_PROGRESS,
        "수정중" to Constants.STATUS_PENDING,
        "종료" to Constants.STATUS_END
    )

    override fun onClickListener(view: View) {
        when (view.id) {
            R.id.mySurveyFilterBtn -> {
                val filterItems = state.keys.toTypedArray()

                MaterialAlertDialogBuilder(view.context)
                    .setTitle("상태")
                    .setItems(filterItems) { dialog, which ->
                        filterTitle.set(filterItems[which])

                        items.clear()
                        filterList = state[filterItems[which]] ?: ""
                        val resultList = filterList(totalItems)
                        items.addAll(resultList)
                    }
                    .show()
            }
            R.id.mySurveyEmptyBtn -> {
                val activity = view.context as AppCompatActivity
                ActivityUtil.startNewSurveyActivity(activity)
                activity.finish()
            }
        }
    }

    fun requestSurvey(context: Context) {
        disposables.add(
            SurveyRepository().requestSurveyList(FivePreference.getAccessToken(context))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.isSuccess()) {
                        totalItems.addAll(response.data.survey_list)

                        val resultList = filterList(response.data.survey_list)
                        items.clear()
                        items.addAll(resultList)
                    }

                }, { t: Throwable? -> t?.printStackTrace() })
        )
    }

    private fun filterList(list: ArrayList<MySurveyListResponse.MySurveyInfo>): ArrayList<Survey> {
        val resultList = ArrayList<Survey>()

        list.filter {
            Constants.STATUS_IN_PROGRESS.equals(it.status, true) && (filterList.equals(Constants.STATUS_IN_PROGRESS, true) || filterList.isEmpty())
        }.forEach {
            resultList.add(Survey.Under(it))
        }

        list.filter {
            Constants.STATUS_END.equals(it.status, true) && (filterList.equals(Constants.STATUS_END, true) || filterList.isEmpty())
        }.forEach {
            resultList.add(Survey.End(it))
        }

        list.filter {
            Constants.STATUS_PENDING.equals(it.status, true) && (filterList.equals(Constants.STATUS_PENDING, true) || filterList.isEmpty())
        }.forEach {
            resultList.add(Survey.Incomplete(it))
        }

        return resultList
    }
}