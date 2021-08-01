package com.bside.five.ui.result

import android.graphics.drawable.Drawable
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.bside.five.R
import com.bside.five.base.BaseViewModel
import com.bside.five.util.ImageUtil

class QrCodeViewModel : BaseViewModel() {

    var imageSaveLive: MutableLiveData<String> = MutableLiveData()
    val qrDrawable = ObservableField<Drawable>()
    val isDownloadQr = ObservableField<Boolean>(false)

    override fun onClickListener(view: View) {
        when (view.id) {
            R.id.qrCodeSaveContainer -> {
                isDownloadQr.set(true)
                val fileName = System.currentTimeMillis().toString() + "_${ImageUtil.TEMP_FILE_NAME}"
                imageSaveLive.postValue(fileName)
            }
        }
    }

}