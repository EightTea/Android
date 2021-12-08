package com.bside.five.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bside.five.base.BaseViewModel

class SettingViewModel : BaseViewModel() {

    private val _versionLive: MutableLiveData<String> = MutableLiveData()
    val versionLive: LiveData<String> get() = _versionLive

    fun setVersion(version: String) {
        _versionLive.value = version
    }
}