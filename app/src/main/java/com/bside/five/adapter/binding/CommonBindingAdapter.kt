package com.bside.five.adapter.binding

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bside.five.util.GlideUtil

object CommonBindingAdapter {

    @BindingAdapter("isEnabled")
    @JvmStatic
    fun isEnabled(view: View, isEnabled: Boolean) {
        view.isEnabled = isEnabled
    }

    @BindingAdapter("isVisible")
    @JvmStatic
    fun isVisible(view: View, isVisible: Boolean) {
        view.isVisible = isVisible
    }

    @BindingAdapter("isSelected")
    @JvmStatic
    fun isSelected(view: View, isSelected: Boolean) {
        view.isSelected = isSelected
    }

    @BindingAdapter("setImage")
    @JvmStatic
    fun setImage(view: ImageView, any: Any?) {
        when (any) {
            is Uri -> {
                GlideUtil.loadImage(view, any)
            }
            is Int -> {
                GlideUtil.loadImage(view, any)
            }
            is Drawable -> {
                GlideUtil.loadImage(view, any)
            }
        }
    }
}