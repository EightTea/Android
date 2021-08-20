package com.bside.five.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bside.five.R
import com.bside.five.util.AutoClearedDisposable
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity<VB : ViewDataBinding, VM : ViewModel> : AppCompatActivity() {

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

    fun showSnackBar(res: Int) {
        Snackbar.make(binding.root, res, Snackbar.LENGTH_SHORT)
            .show()
    }

    fun showSnackBarAction(res: Int) {
        Snackbar.make(binding.root, res, Snackbar.LENGTH_LONG)
            .setAction(R.string.confirm_text, {  })
            .show()
    }
}