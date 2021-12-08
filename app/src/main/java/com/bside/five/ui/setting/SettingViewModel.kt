package com.bside.five.ui.setting

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bside.five.base.BaseViewModel

class SettingViewModel : BaseViewModel() {

    private val tag = this::class.java
    private val _versionLive: MutableLiveData<String> = MutableLiveData()
    val versionLive: LiveData<String> get() = _versionLive

    override fun onClickListener(view: View) {}

    fun setVersion(version: String) {
        _versionLive.value = version
    }
}