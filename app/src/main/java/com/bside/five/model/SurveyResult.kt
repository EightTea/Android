package com.bside.five.model

import com.bside.five.network.response.AnswerListResponse
import com.bside.five.network.response.MySurveyDetailResponse

sealed class SurveyResult {
    class QuestionUI(val question: MySurveyDetailResponse.Question) : SurveyResult()
    class AnswerUI(val answer: AnswerListResponse.Answer) : SurveyResult()
}