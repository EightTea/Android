package com.bside.five.adapter.binding

import android.view.View
import androidx.databinding.BindingAdapter

object CommonBindingAdapter {

    @BindingAdapter("isEnabled")
    @JvmStatic
    fun isEnabled(view: View, isEnabled: Boolean) {
        view.isEnabled = isEnabled
    }
}