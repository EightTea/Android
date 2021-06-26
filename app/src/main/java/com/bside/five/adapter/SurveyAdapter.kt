package com.bside.five.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bside.five.R
import com.bside.five.databinding.LayoutSurveyLowBinding
import com.bside.five.databinding.LayoutSurveyLowEmptyBinding
import com.bside.five.model.Survey

class SurveyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_FOOTER = 1
    }

    private val items = ArrayList<Survey>()

    override fun getItemViewType(position: Int): Int {
        if (items.isEmpty()) {
            return TYPE_FOOTER
        }

        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_FOOTER) {
            val binding = LayoutSurveyLowEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return EmptyViewHolder(binding)
        }

        val binding = LayoutSurveyLowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SurveyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        if (items.isEmpty()) {
            return 1
        }

        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SurveyViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    inner class SurveyViewHolder(val binding: LayoutSurveyLowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Survey) {
            binding.apply {
                surveyLowTitle.text = item.title

                surveyLowResultCount.text = root.resources.getString(R.string.answer_count, item.answerCount ?: 0)

                when (item.state) {
                    "b" -> {
                        surveyLowState.text = root.resources.getString(R.string.survey_state_before)
                        surveyLowState.setTextColor(ContextCompat.getColor(root.context, R.color.white))
                        surveyLowStateContainer.setBackgroundResource(R.drawable.border_progress_rect)
                    }
                    "i" -> {
                        surveyLowState.text = root.resources.getString(R.string.survey_state_ing)
                        surveyLowState.setTextColor(ContextCompat.getColor(root.context, R.color.white))
                        surveyLowStateContainer.setBackgroundResource(R.drawable.border_progress_rect)
                    }
                    "e" -> {
                        surveyLowState.text = root.resources.getString(R.string.survey_state_end)
                        surveyLowState.setTextColor(ContextCompat.getColor(root.context, R.color.complete_text))
                        surveyLowStateContainer.setBackgroundResource(R.drawable.border_complete_rect)
                    }
                }
            }
        }
    }

    inner class EmptyViewHolder(binding: LayoutSurveyLowEmptyBinding) : RecyclerView.ViewHolder(binding.root)

    fun replaceAll(list: ArrayList<Survey>) {
        items.apply {
            clear()
            addAll(list)
            notifyDataSetChanged()
        }
    }
}