package com.bside.five.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions

object GlideUtil {

    fun loadImage(imageView: ImageView, res: Int?) {
        getRequestBuilder(imageView.context, res)
            .into(imageView)
    }

    fun loadImage(imageView: ImageView, drawable: Drawable?) {
        getRequestBuilder(imageView.context, drawable)
            .into(imageView)
    }

    fun loadImage(imageView: ImageView, uri: Uri?) {
        getRequestBuilder(imageView.context, uri)
            .into(imageView)
    }

    fun loadImage(imageView: ImageView, url: String?, scaleType: ImageView.ScaleType) {
        getRequestBuilder(imageView.context, url)
            .apply(getRequestOptions(scaleType))
            .into(imageView)
    }

    fun loadImage(imageView: ImageView, uri: Uri, scaleType: ImageView.ScaleType) {
        getRequestBuilder(imageView.context, uri)
            .apply(getRequestOptions(scaleType))
            .into(imageView)
    }

    private fun getRequestBuilder(
        context: Context,
        url: Any?,
        requestListener: RequestListener<*>? = null
    ): RequestBuilder<*> {
        return Glide.with(context.applicationContext)
            .load(url)
            .listener(requestListener as? RequestListener<Drawable>)
            .transition(DrawableTransitionOptions.withCrossFade())
    }

    private fun getRequestOptions(scaleType: ImageView.ScaleType?): RequestOptions {
        return RequestOptions().apply {
            when (scaleType) {
                ImageView.ScaleType.FIT_CENTER -> fitCenter()
                ImageView.ScaleType.CENTER_CROP -> centerCrop()
                ImageView.ScaleType.CENTER_INSIDE -> centerInside()
                else -> {
                }
            }
        }
    }
}