package com.bside.five.model

import com.bside.five.network.response.MySurveyListResponse

sealed class Survey {
    class Under(val underSurvey: MySurveyListResponse.MySurveyInfo) : Survey()
    class Incomplete(val underSurvey: MySurveyListResponse.MySurveyInfo) : Survey()
    class End(val underSurvey: MySurveyListResponse.MySurveyInfo) : Survey()
}