package com.bside.five.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bside.five.R
import com.bside.five.databinding.LayoutSurveyEndBinding
import com.bside.five.databinding.LayoutSurveyIncompleteBinding
import com.bside.five.databinding.LayoutSurveyUnderBinding
import com.bside.five.model.Survey
import com.bside.five.model.SurveyInfo
import com.bside.five.util.ActivityUtil

class SurveyStateAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_UNDER = 1
        const val TYPE_END = 2
        const val TYPE_INCOMPLETE = 3
    }

    private val tag = this::class.java.simpleName
    private val items = ArrayList<Survey>()

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Survey.Under -> TYPE_UNDER
            is Survey.End -> TYPE_END
            is Survey.Incomplete -> TYPE_INCOMPLETE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_UNDER -> {
                val binding = LayoutSurveyUnderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return UnderViewHolder(binding)
            }
            TYPE_END -> {
                val binding = LayoutSurveyEndBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return EndViewHolder(binding)
            }
            TYPE_INCOMPLETE -> {
                val binding = LayoutSurveyIncompleteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return IncompleteViewHolder(binding)
            }
        }
        return super.createViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UnderViewHolder -> {
                holder.bind((items[position] as Survey.Under).underSurvey)
            }
            is EndViewHolder -> {
                holder.bind((items[position] as Survey.End).underSurvey)
            }
            is IncompleteViewHolder -> {
                holder.bind((items[position] as Survey.Incomplete).underSurvey)
            }
        }
    }

    inner class UnderViewHolder(val binding: LayoutSurveyUnderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SurveyInfo) {
            binding.apply {
                surveyUnderTitle.text = item.title
                surveyUnderAnswerCount.text = root.resources.getString(R.string.answer_now_count, item.answerCount ?: 0)
                val dateStr = "${item.start_date ?: ""} ~ ${item.end_date ?: ""}"
                surveyUnderDate.text = dateStr

                surveyUnderQrBtn.setOnClickListener {
                    ActivityUtil.startQrCodeActivity(it.context as AppCompatActivity, "https://www.naver.com/")
                }

                surveyUnderComplete.setOnClickListener {
                    Toast.makeText(it.context, "surveyUnderComplete", Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    inner class EndViewHolder(val binding: LayoutSurveyEndBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SurveyInfo) {
            binding.apply {
                surveyEndTitle.text = item.title
                surveyEndAnswerCount.text = root.resources.getString(R.string.answer_count, item.answerCount ?: 0)
                val dateStr = "${item.start_date ?: ""} ~ ${item.end_date ?: ""}"
                surveyEndDate.text = dateStr
            }
        }
    }

    inner class IncompleteViewHolder(val binding: LayoutSurveyIncompleteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SurveyInfo) {
            binding.apply {
                surveyIncompleteTitle.text = item.title
                surveyIncompleteDeleteBtn.setOnClickListener {
                    Toast.makeText(it.context, "surveyIncompleteDeleteBtn", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun replaceAll(list: ArrayList<Survey>) {
        items.apply {
            clear()
            addAll(list)
            notifyDataSetChanged()
        }
    }
}