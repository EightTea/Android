package com.bside.five.model

sealed class SurveyResult {
    class QuestionUI(val question: Question) : SurveyResult()
    class AnswerUI(val answer: Answer) : SurveyResult()
}