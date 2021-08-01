package com.bside.five.ui.answer

import android.content.Context
import android.util.Log
import android.view.View
import androidx.databinding.ObservableArrayList
import com.bside.five.base.BaseViewModel
import com.bside.five.model.*
import com.bside.five.network.repository.SurveyRepository
import com.bside.five.util.FivePreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AnswerViewModel : BaseViewModel() {

    private val tag = this::class.java.simpleName
    val items = ObservableArrayList<SurveyResult>()

    override fun onClickListener(view: View) {}

    fun requestAnswerAPI(context: Context) {

        val responseList = ArrayList<Question>()

        responseList.add(Question(1, "우리 가게 방문 이유 확인하고 싶어요!"))
        responseList.add(Question(2, "가게 인테리어 컨셉 어떻게 하면 좋을까요?"))
        responseList.add(Question(3, "우리 가게 메뉴를 평가해주세요."))
        responseList.add(Question(4, "우리 가게 메뉴를 평가해주세요."))
        responseList.add(Question(5, "불편한점을 알려주세요!"))

        val oneList = ArrayList<Answer>()

        oneList.add(Answer(1, "보라돌이 라뗴가 너무 맛있어서요! 진짜, 인간적으로 너무 맛있어요~", 123, false))
        oneList.add(Answer(1, "인테리어가 뭔가 집중하기 좋아요. 시끄러운 음악도 없고, 뭔가 여기 오는 사람들 매너가 좋은 듯~ 작업하기 좋아서 가요~", 123, false))
        oneList.add(Answer(1, "조용해서 공부하기 좋아서 계속 방문하고 있어요.", 123, true))

        oneList.add(Answer(2, "깨끗해요.", 123, false))
        oneList.add(Answer(2, "분위기가 좋아서 다시 방문하고 싶네요.", 123, false))
        oneList.add(Answer(2, "조용해서 공부하기 좋아서 계속 방문하고 있어요.", 123, false))
        oneList.add(Answer(2, "깨끗해요.", 123, false))
        oneList.add(Answer(2, "분위기가 좋아서 다시 방문하고 싶네요.", 123, false))
        oneList.add(Answer(2, "조용해서 공부하기 좋아서 계속 방문하고 있어요.", 123, false))
        oneList.add(Answer(2, "깨끗해요.", 123, false))
        oneList.add(Answer(2, "분위기가 좋아서 다시 방문하고 싶네요.", 123, false))
        oneList.add(Answer(2, "조용해서 공부하기 좋아서 계속 방문하고 있어요.", 123, false))


        val resultList = ArrayList<SurveyResult>()

        responseList.forEachIndexed { index, item ->
            resultList.add(SurveyResult.QuestionUI(item))
            oneList.filter {
                it.questionNo == item.no
            }.forEach { data ->
                resultList.add(SurveyResult.AnswerUI(data))
            }
        }

        Log.d(tag, "kch List : ${resultList.size}")

        items.addAll(resultList)


    }
}