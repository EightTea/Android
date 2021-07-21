package com.bside.five.ui.preview

import android.net.Uri
import android.view.View
import androidx.databinding.ObservableField
import com.bside.five.R
import com.bside.five.base.BaseViewModel
import com.bside.five.custom.dialog.DialogPreview

class PreviewViewModel : BaseViewModel() {

    val no = ObservableField<String>("")
    val contents = ObservableField<String>("")
    val image = ObservableField<Uri>()

    override fun onClickListener(view: View) {
        if (view.id == R.id.previewImg) {
            image.get()?.let {
                val dialogPreview = DialogPreview(view.context, it)
                dialogPreview.show()
            }
        }
    }

}