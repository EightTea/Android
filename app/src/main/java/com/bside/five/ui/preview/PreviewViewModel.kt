package com.bside.five.ui.preview

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bside.five.base.BaseViewModel

class PreviewViewModel : BaseViewModel() {

    private val _noLive: MutableLiveData<String> = MutableLiveData("")
    val noLive: LiveData<String> get() = _noLive
    private val _contentsLive: MutableLiveData<String> = MutableLiveData("")
    val contentsLive: LiveData<String> get() = _contentsLive
    private val _imageLive: MutableLiveData<Uri> = MutableLiveData()
    val imageLive: LiveData<Uri> get() = _imageLive

    fun setNo(no: String) {
        _noLive.value = no
    }

    fun setContents(contents: String) {
        _contentsLive.value = contents
    }

    fun setImage(uri: Uri?) {
        _imageLive.value = uri
    }
}