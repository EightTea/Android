package com.bside.five.ui.my

import android.content.Context
import android.util.Log
import android.view.View
import androidx.databinding.ObservableArrayList
import com.bside.five.base.BaseViewModel
import com.bside.five.model.Survey
import com.bside.five.model.SurveyInfo
import com.bside.five.network.repository.SurveyRepository
import com.bside.five.util.FivePreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MySurveyViewModel : BaseViewModel() {

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
                        // FIXME : 호출 확인 데이터가 안나옴

                    }

                }, { t: Throwable? -> t?.printStackTrace() })
        )

        val responseList = ArrayList<SurveyInfo>()

        responseList.add(SurveyInfo("우리 가게 방문 이유 확인하고 싶어요!", 123, 123, "i", 15))
        responseList.add(SurveyInfo("가게 인테리어 컨셉 어떻게 하면 좋을까요?", 123, 123, "e", 15))
        responseList.add(SurveyInfo("우리 가게 메뉴를 평가해주세요.", 123, 123, "e", 15))
        responseList.add(SurveyInfo("이벤트 설문조사", 123, 123, "i", 15))
        responseList.add(SurveyInfo("불편한점을 알려주세요!", 123, 123, "i", 15))
        responseList.add(SurveyInfo("테스트", 123, 123, "s", 15))

        // FIXME : 하단은 필터 적용시 사용하면 될거 같구, 리스트 순서 대로 표시되는 로직 생각해야함
        
        val list = ArrayList<Survey>()

        responseList.filter {
            "i".equals(it.state, true)
        }.forEach {
            list.add(Survey.Under(it))
        }

        responseList.filter {
            "e".equals(it.state, true)
        }.forEach {
            list.add(Survey.End(it))
        }

        responseList.filter {
            "s".equals(it.state, true)
        }.forEach {
            list.add(Survey.Incomplete(it))
        }

        Log.d(tag, "kch List : ${list.size}")

        items.addAll(list)
    }
}