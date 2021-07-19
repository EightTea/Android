package com.bside.five.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bside.five.model.SurveyFragmentInfo
import com.bside.five.ui.survey.QuestionFragment
import com.bside.five.ui.survey.SurveyInfoFragment

class ScreenSlidePagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragmentInfo = ArrayList<SurveyFragmentInfo>()

    override fun getItemCount(): Int = fragmentInfo.size + 1

    override fun createFragment(position: Int): Fragment {
        if (position == 0) {
            return SurveyInfoFragment.newInstance()
        }

        return QuestionFragment.newInstance(fragmentInfo[position - 1])
    }

    fun getItem(questionItemPosition: Int): SurveyFragmentInfo {
        return fragmentInfo[questionItemPosition]
    }

    fun getQuestionItemCount(): Int = fragmentInfo.size

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