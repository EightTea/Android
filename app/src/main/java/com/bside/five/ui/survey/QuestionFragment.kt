package com.bside.five.ui.survey

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import com.bside.five.R
import com.bside.five.base.BaseFragment
import com.bside.five.constants.Constants
import com.bside.five.databinding.FragmentQuestionBinding
import com.bside.five.model.SurveyFragmentInfo
import com.bside.five.util.GlideUtil

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

        fragmentInfo = arguments?.getSerializable(KEY) as SurveyFragmentInfo?

        fragmentInfo?.childId?.let {
            viewModel.questionInfoList[it - 1].let { info ->
                binding.questionNo.text = getString(R.string.question_no, info.no)
                binding.questionContent.setText(info.content)
                binding.questionImageContainer.isVisible = info.imageUri == Uri.EMPTY
                binding.questionImageVisibleContainer.isVisible = info.imageUri != Uri.EMPTY
                GlideUtil.loadImage(binding.questionImg, info.imageUri)
                viewModel.contentSizeLive.postValue(info.content.length)
            }
        }

        binding.questionContent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.content = s?.toString() ?: ""
                viewModel.contentSizeLive.postValue(s?.length ?: 0)
            }
        })

        viewModel.clearImageLive.observe(viewLifecycleOwner, Observer<Int?> { questionNo ->
            fragmentInfo?.childId?.let { position ->
                if (questionNo == viewModel.questionInfoList[position - 1].no) {
                    binding.questionImageContainer.visibility = View.VISIBLE
                    binding.questionImageVisibleContainer.visibility = View.GONE
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.REQUEST_CODE_GALLERY) {
                data?.getParcelableExtra<Uri>("key")?.let {
                    GlideUtil.loadImage(binding.questionImg, it)
                    binding.questionImageContainer.visibility = View.GONE
                    binding.questionImageVisibleContainer.visibility = View.VISIBLE

                    viewModel.imgPath = it
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}