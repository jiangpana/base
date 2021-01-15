package com.jansir.core.base.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.jansir.core.base.dialog.LoadingDialog
import com.jansir.core.base.viewmodel.BaseViewModel
import com.jansir.core.base.viewmodel.StateActionEvent
import com.jansir.core.ext.findClazzFromSuperclassGeneric

/**
 * author: jansir
 * e-mail: xxx
 * date: 2019/9/2.
 */

abstract class BaseVMFragment<VM : BaseViewModel<*>, VB : ViewBinding> : BaseFragment<VB>() {

    protected val viewModel: VM by lazy {
        getVM()
    }

    private fun getVM(): VM {
        return ViewModelProvider(mActivity).get(findClazzFromSuperclassGeneric(ViewModel::class.java) as Class<VM>)
    }

    private val loading: LoadingDialog by lazy {
        LoadingDialog.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startObserve()
    }

    protected open fun startObserve() {
        viewModel.let { baseViewModel ->
            baseViewModel.stateLiveData.observe(this, Observer { stateActionState ->
                when (stateActionState) {
                    StateActionEvent.LoadingState -> showLoading()
                    StateActionEvent.SuccessState -> {
                        showContentView()
                        dismissLoading()
                    }
                    StateActionEvent.DataErrorState -> {
                        dismissLoading()
                        handleDataError()
                    }
                    is StateActionEvent.NetErrorState -> {
                        dismissLoading()
                        handleNetWorkError()
                    }
                }
            })
        }
    }


    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        initData()
    }

    abstract fun initData()

    open fun showLoading() {
        loading.show(mActivity.supportFragmentManager, "loading")
    }

    open fun dismissLoading() {
        loading.dismiss()
    }

    private fun showContentView() {
        activityBaseBinding.statusViewBase.showContent()
    }

    open fun handleNetWorkError() {
        if (isShowNoNetView) activityBaseBinding.statusViewBase.showNoNetwork()
    }

    open fun handleDataError() {
        if (isShowNoDataView) {
            activityBaseBinding.statusViewBase.showError()
        }
    }

    protected open val isShowNoDataView: Boolean
        get() = false
    protected open val isShowNoNetView: Boolean
        get() = true
}