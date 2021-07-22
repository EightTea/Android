package com.bside.five.adapter.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bside.five.adapter.SurveyStateAdapter
import com.bside.five.model.Survey

object ListBindingAdapter {

    @BindingAdapter("setSurveyList")
    @JvmStatic
    fun setSurveyList(view: RecyclerView, list: ArrayList<Survey>) {
        val adapter = view.adapter as? SurveyStateAdapter ?: SurveyStateAdapter().apply {
            view.adapter = this
        }

        adapter.replaceAll(list)
    }
}