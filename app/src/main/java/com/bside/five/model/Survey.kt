package com.bside.five.model

sealed class Survey {
    class Under(val underSurvey: SurveyInfo) : Survey()
    class Incomplete(val underSurvey: SurveyInfo) : Survey()
    class End(val underSurvey: SurveyInfo) : Survey()
}