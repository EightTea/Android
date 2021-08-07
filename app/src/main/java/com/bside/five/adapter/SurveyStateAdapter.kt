package com.bside.five.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bside.five.R
import com.bside.five.custom.dialog.QuestionCompleteDialog
import com.bside.five.custom.dialog.QuestionDeleteAllDialog
import com.bside.five.databinding.LayoutSurveyEndBinding
import com.bside.five.databinding.LayoutSurveyIncompleteBinding
import com.bside.five.databinding.LayoutSurveyUnderBinding
import com.bside.five.model.Survey
import com.bside.five.network.repository.SurveyRepository
import com.bside.five.network.response.MySurveyListResponse
import com.bside.five.util.ActivityUtil
import com.bside.five.util.CommonUtil
import com.bside.five.util.FivePreference
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.collections.ArrayList

class SurveyStateAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_UNDER = 1
        const val TYPE_END = 2
        const val TYPE_INCOMPLETE = 3
    }

    private val tag = this::class.java.simpleName
    private val items = ArrayList<Survey>()
    private val disposable = CompositeDisposable()

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

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        disposable.dispose()
    }

    inner class UnderViewHolder(val binding: LayoutSurveyUnderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MySurveyListResponse.MySurveyInfo) {
            binding.apply {
                surveyUnderTitle.text = item.title
                surveyUnderAnswerCount.text = root.resources.getString(R.string.answer_now_count, item.answer_cnt ?: 0)

                val start = CommonUtil.convertFormat(item.start_date, "yyyy-MM-dd")
                val end = CommonUtil.convertFormat(item.end_date, "yyyy-MM-dd")
                val dateStr = "$start ~ $end"
                surveyUnderDate.text = dateStr

                surveyUnderQrBtn.setOnClickListener {
                    ActivityUtil.startQrCodeActivity(it.context as AppCompatActivity, item.survey_id)
                }

                surveyUnderComplete.setOnClickListener {
                    val dialog = QuestionCompleteDialog(it.context, item.survey_id)
                    dialog.show()
                }

                root.setOnClickListener {
                    ActivityUtil.startAnswerActivity(it.context as AppCompatActivity, item.survey_id, item.title, item.answer_cnt)
                }
            }
        }
    }

    inner class EndViewHolder(val binding: LayoutSurveyEndBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MySurveyListResponse.MySurveyInfo) {
            binding.apply {
                surveyEndTitle.text = item.title
                surveyEndAnswerCount.text = root.resources.getString(R.string.answer_count, item.answer_cnt ?: 0)

                val start = CommonUtil.convertFormat(item.start_date, "yyyy-MM-dd")
                val end = CommonUtil.convertFormat(item.end_date, "yyyy-MM-dd")
                val dateStr = "$start ~ $end"
                surveyEndDate.text = dateStr

                root.setOnClickListener {
                    ActivityUtil.startAnswerActivity(it.context as AppCompatActivity, item.survey_id, item.title, item.answer_cnt)
                }
            }
        }
    }

    inner class IncompleteViewHolder(val binding: LayoutSurveyIncompleteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MySurveyListResponse.MySurveyInfo) {
            binding.apply {
                surveyIncompleteTitle.text = item.title
                surveyIncompleteDeleteBtn.setOnClickListener {
                    Toast.makeText(it.context, "surveyIncompleteDeleteBtn", Toast.LENGTH_LONG).show()

                    val dialog = QuestionDeleteAllDialog(root.context, item.survey_id)
                    dialog.show()
                }

                root.setOnClickListener {
                    Toast.makeText(it.context, "구현중인 질문 표시", Toast.LENGTH_LONG).show()
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