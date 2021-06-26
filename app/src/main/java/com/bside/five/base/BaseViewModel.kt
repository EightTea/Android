package com.bside.five.base

import android.view.View
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    protected val disposables: CompositeDisposable = CompositeDisposable()

    abstract fun onClickListener(view: View)
}