package com.bside.five.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bside.five.model.SurveyFragmentInfo
import com.bside.five.ui.survey.QuestionFragment

class ScreenSlidePagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragmentInfo = ArrayList<SurveyFragmentInfo>()

    override fun getItemCount(): Int = fragmentInfo.size

    override fun createFragment(position: Int): Fragment = QuestionFragment.newInstance(fragmentInfo[position])

    fun getItem(position: Int): SurveyFragmentInfo {
        return fragmentInfo[position]
    }

    fun replaceFragment(list: ArrayList<SurveyFragmentInfo>) {
        fragmentInfo.apply {
            clear()
            addAll(list)
            notifyDataSetChanged()
        }
    }

    fun addFragment(info: SurveyFragmentInfo) {
        fragmentInfo.add(info)
        notifyItemInserted(itemCount - 1)
    }

    fun removeFragment(info: SurveyFragmentInfo) {
        val position = fragmentInfo.indexOf(info)
        fragmentInfo.remove(info)
        notifyItemRemoved(position)
    }
}