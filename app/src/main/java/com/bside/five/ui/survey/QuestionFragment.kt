package com.bside.five.ui.survey

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelStoreOwner
import com.bside.five.R
import com.bside.five.base.BaseFragment
import com.bside.five.databinding.FragmentQuestionBinding
import com.bside.five.model.SurveyFragmentInfo

class QuestionFragment : BaseFragment<FragmentQuestionBinding, NewSurveyViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.fragment_question
    override val viewModelClass: Class<NewSurveyViewModel>
        get() = NewSurveyViewModel::class.java
    override val owner: ViewModelStoreOwner
        get() = requireActivity()

    var fragmentInfo: SurveyFragmentInfo? = null

    companion object {
        private const val KEY = "fragment_info"

        fun newInstance(info: SurveyFragmentInfo): QuestionFragment {
            val args = Bundle()
            args.putSerializable(KEY, info)
            return QuestionFragment().apply { arguments = args }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(tag, "kch onViewCreated()")

        fragmentInfo = arguments?.getSerializable(KEY) as SurveyFragmentInfo?

        binding.questionNo.text = getString(R.string.question_no, fragmentInfo?.childId)

        binding.questionContent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.contentSizeLive.postValue(count)
            }
        })
    }


}