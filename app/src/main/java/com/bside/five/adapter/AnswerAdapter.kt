package com.bside.five.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bside.five.databinding.LayoutAnswerLowBinding
import com.bside.five.databinding.LayoutQuestionLowBinding
import com.bside.five.model.Answer
import com.bside.five.model.Question
import com.bside.five.model.SurveyResult

class AnswerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_QUESTION = 1
        const val TYPE_ANSWER = 2
    }

    private val items = ArrayList<SurveyResult>()

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is SurveyResult.QuestionUI -> TYPE_QUESTION
            is SurveyResult.AnswerUI -> TYPE_ANSWER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_QUESTION -> {
                val binding = LayoutQuestionLowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return QuestionViewModel(binding)
            }
            TYPE_ANSWER -> {
                val binding = LayoutAnswerLowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return AnswerViewModel(binding)
            }
        }

        return super.createViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is QuestionViewModel -> holder.bind((items[position] as SurveyResult.QuestionUI).question)
            is AnswerViewModel -> holder.bind((items[position] as SurveyResult.AnswerUI).answer)
        }
    }

    inner class QuestionViewModel(val binding: LayoutQuestionLowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Question) {
            binding.apply {
                questionLowNo.text = "Q${item.no}"
                questionLowTitle.text = item.title
            }
        }
    }

    inner class AnswerViewModel(val binding: LayoutAnswerLowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Answer) {
            binding.apply {
                answerLowContents.text = item.answer
//                answerLowDate.text = "${item.date} 답변"
                answerLowDate.text = "2021.03.12 답변"
                answerLowMoreContainer.isVisible = item.isMore

                answerLowMoreBtn.setOnClickListener {
                    Toast.makeText(it.context, "answerLowMoreBtn api call", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private class AnswerDiff(private val oldItems: List<SurveyResult>, private val newItems: List<SurveyResult>) :
        DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return if (oldItem is SurveyResult.QuestionUI && newItem is SurveyResult.QuestionUI) {
                oldItem.question.no == newItem.question.no
            } else if (oldItem is SurveyResult.AnswerUI && newItem is SurveyResult.AnswerUI) {
                oldItem.answer.questionNo == newItem.answer.questionNo
            } else {
                oldItem == newItem
            }
        }

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return oldItem == newItem
        }
    }

    fun replaceAll(list: ArrayList<SurveyResult>) {
        val diff = AnswerDiff(items, list)
        val diffResult = DiffUtil.calculateDiff(diff)
        items.clear()
        items.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}