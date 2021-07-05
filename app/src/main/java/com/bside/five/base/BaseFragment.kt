package com.bside.five.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.bside.five.util.AutoClearedDisposable
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<VB : ViewDataBinding, VM : BaseViewModel> : Fragment() {

    lateinit var binding: VB
    abstract val layoutResourceId: Int
    lateinit var viewModel: VM
    abstract val viewModelClass: Class<VM>
    abstract val owner: ViewModelStoreOwner
    lateinit var disposables: AutoClearedDisposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(owner).get(viewModelClass)

        binding.also {
            it.lifecycleOwner = this
            it.setVariable(BR.viewModel, viewModel)
        }

        disposables = AutoClearedDisposable(activity as AppCompatActivity, false, CompositeDisposable())
        lifecycle.addObserver(disposables)
    }
}