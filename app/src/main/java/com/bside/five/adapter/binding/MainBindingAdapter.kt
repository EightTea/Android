package com.bside.five.adapter.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bside.five.adapter.SurveyAdapter
import com.bside.five.model.Survey

object MainBindingAdapter {

    @BindingAdapter("setSurveyList")
    @JvmStatic
    fun setSurveyList(view: RecyclerView, list: ArrayList<Survey>) {
        val adapter = view.adapter as? SurveyAdapter ?: SurveyAdapter().apply {
            view.adapter = this
        }

        adapter.replaceAll(list)
    }
}