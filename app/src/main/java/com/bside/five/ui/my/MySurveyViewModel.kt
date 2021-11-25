package com.bside.five.ui.my

import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bside.five.base.BaseViewModel
import com.bside.five.constants.Constants
import com.bside.five.model.Survey
import com.bside.five.network.repository.SurveyRepository
import com.bside.five.network.response.MySurveyListResponse
import com.bside.five.util.FivePreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MySurveyViewModel : BaseViewModel() {

    private val tag = this::class.java.simpleName
    var items = ArrayList<Survey>()
    private val _itemsLive: MutableLiveData<ArrayList<Survey>> = MutableLiveData()
    val itemsLive: LiveData<ArrayList<Survey>> get() = _itemsLive
    var totalItems = ObservableArrayList<MySurveyListResponse.MySurveyInfo>()

    override fun onClickListener(view: View) {}

    fun filter(filterType: String) {
        clearItem()
        val resultList = filterList(totalItems, filterType)
        addAllItem(resultList)
    }

    fun requestSurvey() {
        disposables.add(
            SurveyRepository().requestSurveyList(FivePreference.getAccessToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.isSuccess()) {
                        totalItems.addAll(response.data.survey_list)

                        val resultList = filterList(response.data.survey_list)

                        clearItem()
                        addAllItem(resultList)
                    }

                }, { t: Throwable? -> t?.printStackTrace() })
        )
    }

    private fun filterList(list: ArrayList<MySurveyListResponse.MySurveyInfo>, filterType: String = ""): ArrayList<Survey> {
        val resultList = ArrayList<Survey>()

        list.filter {
            Constants.STATUS_IN_PROGRESS.equals(it.status, true) && (filterType.equals(Constants.STATUS_IN_PROGRESS, true) || filterType.isEmpty())
        }.forEach {
            resultList.add(Survey.Under(it))
        }

        list.filter {
            Constants.STATUS_END.equals(it.status, true) && (filterType.equals(Constants.STATUS_END, true) || filterType.isEmpty())
        }.forEach {
            resultList.add(Survey.End(it))
        }

        list.filter {
            Constants.STATUS_PENDING.equals(it.status, true) && (filterType.equals(Constants.STATUS_PENDING, true) || filterType.isEmpty())
        }.forEach {
            resultList.add(Survey.Incomplete(it))
        }

        return resultList
    }

    fun addAllItem(list: ArrayList<Survey>) {
        items.addAll(list)
        _itemsLive.value = items
    }

    fun clearItem() {
        items.clear()
        _itemsLive.value = items
    }
}