package com.bside.five.model

data class Answer(val questionNo: Int, val answer: String, val date: Int, val isLast: Boolean = false)