package com.bside.five.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bside.five.R
import com.bside.five.databinding.LayoutAnswerEmptyLowBinding
import com.bside.five.databinding.LayoutAnswerLowBinding
import com.bside.five.databinding.LayoutAnswerQrBtnBinding
import com.bside.five.databinding.LayoutQuestionLowBinding
import com.bside.five.model.SurveyResult
import com.bside.five.network.ApiClient
import com.bside.five.network.repository.SurveyRepository
import com.bside.five.network.response.AnswerListResponse
import com.bside.five.network.response.MySurveyDetailResponse
import com.bside.five.util.ActivityUtil
import com.bside.five.util.CommonUtil
import com.bside.five.util.FivePreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.collections.ArrayList

class AnswerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_QUESTION = 1
        const val TYPE_ANSWER = 2
        const val TYPE_HEADER = 3
        const val TYPE_FOOTER = 4
    }

    private val items = ArrayList<SurveyResult>()
    private var surveyId = ""
    private val disposable = CompositeDisposable()

    override fun getItemViewType(position: Int): Int {
        if (items.isEmpty()) {
            return TYPE_HEADER
        }

        if (position == items.size) {
            return TYPE_FOOTER
        }

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
            TYPE_FOOTER -> {
                val binding = LayoutAnswerQrBtnBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return FooterViewHolder(binding)
            }
            TYPE_HEADER -> {
                val binding = LayoutAnswerEmptyLowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return HeaderViewHolder(binding)
            }
        }

        return super.createViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        return items.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is QuestionViewModel -> holder.bind((items[position] as SurveyResult.QuestionUI).question)
            is AnswerViewModel -> holder.bind((items[position] as SurveyResult.AnswerUI).answer)
            is HeaderViewHolder -> holder.bind()
            is FooterViewHolder -> holder.bind()
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        disposable.dispose()
    }

    inner class QuestionViewModel(val binding: LayoutQuestionLowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MySurveyDetailResponse.Question) {
            binding.apply {
                questionLowNo.text = root.context.getString(R.string.survey_no, item.no)
                questionLowTitle.text = item.content
            }
        }
    }

    inner class AnswerViewModel(val binding: LayoutAnswerLowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AnswerListResponse.Answer) {
            binding.apply {
                answerLowContents.text = item.comment
                val dateText = CommonUtil.convertFormat(item.date, "yyyy-MM-dd")
                answerLowDate.text = root.context.getString(R.string.survey_answer_date, dateText)
                answerLowMoreContainer.isVisible = item.isMore

                answerLowMoreBtn.setOnClickListener {
                    Toast.makeText(it.context, "answerLowMoreBtn api call", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    inner class FooterViewHolder(val binding: LayoutAnswerQrBtnBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.apply {
                qrCodeBtn.setOnClickListener {
                    ActivityUtil.startQrCodeActivity(it.context as AppCompatActivity, surveyId)
                }

                customerSurveyBtn.setOnClickListener {
                    val url = ApiClient.BASE_URL + surveyId + "/view"
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    root.context.startActivity(i)
                }
            }
        }
    }

    inner class HeaderViewHolder(val binding: LayoutAnswerEmptyLowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.apply {
                answerEmptyQrCodeBtn.setOnClickListener {
                    ActivityUtil.startQrCodeActivity(it.context as AppCompatActivity, surveyId)
                }

                answerEmptySurveyBtn.setOnClickListener {
                    val url = ApiClient.BASE_URL + surveyId + "/view"
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    root.context.startActivity(i)
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
                oldItem.answer.answer_no == newItem.answer.answer_no
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

    fun setSurveyId(id: String) {
        surveyId = id
    }
}