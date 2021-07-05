package com.bside.five.ui.survey

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        var fragmentInfo = arguments?.getSerializable(KEY) as SurveyFragmentInfo?

    }


}