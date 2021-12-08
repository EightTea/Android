package com.bside.five.ui.result

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bside.five.base.BaseViewModel
import com.bside.five.util.ImageUtil

class QrCodeViewModel : BaseViewModel() {

    private val _imageSaveLive: MutableLiveData<String> = MutableLiveData()
    val imageSaveLive: LiveData<String> get() = _imageSaveLive
    val isDownloadQr = ObservableField<Boolean>(false)
    private val _isDownloadQrLive: MutableLiveData<Boolean> = MutableLiveData()
    val isDownloadQrLive: LiveData<Boolean> get() = _isDownloadQrLive

    override fun onClickListener(view: View) {}

    fun saveQr() {
        _isDownloadQrLive.value = true
        val fileName = System.currentTimeMillis().toString() + "_${ImageUtil.TEMP_FILE_NAME}"
        _imageSaveLive.value = fileName
    }

    fun finishDownloadQr() {
        _isDownloadQrLive.value = false
    }
}