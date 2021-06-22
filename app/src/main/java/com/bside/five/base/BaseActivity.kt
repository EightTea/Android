package com.bside.five.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProvider
import com.bside.five.util.AutoClearedDisposable
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity<VB : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity() {

    lateinit var binding: VB
    abstract val layoutResourceId: Int
    lateinit var viewModel: VM
    abstract val viewModelClass: Class<VM>
    lateinit var disposables: AutoClearedDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResourceId)
        viewModel = ViewModelProvider(this).get(viewModelClass)
        binding.also {
            it.lifecycleOwner = this
            it.setVariable(BR.viewModel, viewModel)
        }

        disposables = AutoClearedDisposable(this, false, CompositeDisposable())
        lifecycle.addObserver(disposables)
    }
}