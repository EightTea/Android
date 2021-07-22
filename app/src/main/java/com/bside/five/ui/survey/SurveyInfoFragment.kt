package com.bside.five.ui.survey

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelStoreOwner
import com.bside.five.R
import com.bside.five.base.BaseFragment
import com.bside.five.databinding.FragmentSurveyInfoBinding

class SurveyInfoFragment : BaseFragment<FragmentSurveyInfoBinding, NewSurveyViewModel>() {

    companion object {
        fun newInstance(): SurveyInfoFragment {
            val args = Bundle()
            return SurveyInfoFragment().apply { arguments = args }
        }
    }

    override val layoutResourceId: Int
        get() = R.layout.fragment_survey_info
    override val viewModelClass: Class<NewSurveyViewModel>
        get() = NewSurveyViewModel::class.java
    override val owner: ViewModelStoreOwner
        get() = requireActivity()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.surveyInfoTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.surveyTitle = s?.toString() ?: ""
                viewModel.isEnableStartSurvey.set((s?.length ?: 0) * viewModel.surveyContents.length != 0)
                viewModel.contentsSizeLive.postValue((s?.length ?: 0) * viewModel.surveyContents.length)
            }
        })

        binding.surveyInfoContent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.surveyContents = s?.toString() ?: ""
                viewModel.isEnableStartSurvey.set((s?.length ?: 0) * viewModel.surveyTitle.length != 0)
                viewModel.contentsSizeLive.postValue((s?.length ?: 0) * viewModel.surveyTitle.length)
            }
        })

    }
}