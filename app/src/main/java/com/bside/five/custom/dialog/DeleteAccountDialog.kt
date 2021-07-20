package com.bside.five.custom.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bside.five.R
import com.bside.five.databinding.DialogDeleteAccoutBinding

class DeleteAccountDialog(context: Context) : Dialog(context, R.style.Theme_AppCompat_Dialog_Alert) {

    private var binding: DialogDeleteAccoutBinding =
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_delete_accout, null, false)

    init {
        setContentView(binding.root)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    override fun show() {

        binding.deleteAccountOkBtn.setOnClickListener {
            Toast.makeText(context, "Delete Account", Toast.LENGTH_LONG).show()
        }

        binding.deleteAccountCancelBtn.setOnClickListener {
            Toast.makeText(context, "Cancel", Toast.LENGTH_LONG).show()
            dismiss()
        }

        super.show()
    }
}