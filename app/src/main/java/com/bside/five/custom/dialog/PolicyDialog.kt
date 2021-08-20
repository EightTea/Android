package com.bside.five.custom.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bside.five.R
import com.bside.five.custom.listener.OnConfirmListener
import com.bside.five.databinding.DialogPolicyBinding
import com.bside.five.network.ApiClient
import com.bside.five.util.ActivityUtil


class PolicyDialog(context: Context, val listener: OnConfirmListener) : Dialog(context, R.style.DefaultDialog) {

    private val tag = this::class.java.simpleName
    private var binding: DialogPolicyBinding =
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_policy, null, false)

    init {
        setContentView(binding.root)
    }

    override fun show() {
        binding.privacyPolicyClickText.setOnClickListener {
            ActivityUtil.startPrivacyPolicy(context)
        }
        binding.privacyPolicyCheck.setOnClickListener {
            binding.privacyPolicyCheck.isSelected = !binding.privacyPolicyCheck.isSelected
        }

        binding.policyConfirmBtn.setOnClickListener {
            if (binding.privacyPolicyCheck.isSelected) {
                listener.onConfirm()
            } else {
                Toast.makeText(context, R.string.policy_guide_text, Toast.LENGTH_LONG).show()
            }
        }

        binding.policyCancelBtn.setOnClickListener {
            dismiss()
        }

        super.show()
    }
}