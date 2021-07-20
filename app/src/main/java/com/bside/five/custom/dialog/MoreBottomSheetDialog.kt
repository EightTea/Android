package com.bside.five.custom.dialog

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bside.five.R
import com.bside.five.databinding.DialogBottomSheetMoreBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class MoreBottomSheetDialog(context: Context) : BottomSheetDialog(context, R.style.RadiusBottomSheetDialogTheme) {

    private var binding: DialogBottomSheetMoreBinding =
        DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_bottom_sheet_more, null, false)

    init {
        setContentView(binding.root)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
    }

    override fun show() {
        binding.moreBottomSheetLogoutBtn.setOnClickListener {
            Toast.makeText(context, "Logout", Toast.LENGTH_LONG).show()
        }

        binding.moreBottomSheetDeleteAccountBtn.setOnClickListener {
            val dialog = DeleteAccountDialog(context)
            dialog.show()
            Toast.makeText(context, "Delete Account", Toast.LENGTH_LONG).show()
        }

        super.show()
    }
}