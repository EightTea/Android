package com.bside.five.base

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseApplicationViewModel(application: Application) : AndroidViewModel(application) {

    protected val disposables: CompositeDisposable = CompositeDisposable()

    abstract fun onClickListener(view: View)
}