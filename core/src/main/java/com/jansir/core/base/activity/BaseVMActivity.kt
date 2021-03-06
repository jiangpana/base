package com.jansir.core.base.activity

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
abstract class BaseVMActivity<VB : ViewBinding, VM : BaseViewModel<*>> : BaseActivity<VB>() {

    protected val viewModel: VM by lazy {
        getVM()
    }

    private fun getVM(): VM {
        return ViewModelProvider(this).get(findClazzFromSuperclassGeneric(ViewModel::class.java)) as VM
    }


    private val loading: LoadingDialog by lazy {
        LoadingDialog.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startObserve()
        initObserver()
    }

    abstract fun initObserver()


    protected open fun startObserve() {
        viewModel.stateLiveData.observe(this, Observer { stateActionState ->
            when (stateActionState) {
                StateActionEvent.LoadingState -> showLoading()
                StateActionEvent.SuccessState -> {
                    dismissLoading()
                    showContentView()
                }
                StateActionEvent.DataErrorState -> {
                    dismissLoading()
                    handleDataError()
                }
                StateActionEvent.NetErrorState -> {
                    dismissLoading()
                    handleNetWorkError()
                }
            }
        })
    }


    private fun showContentView() {
        activityBaseBinding.statusViewBase.showContent()
    }


    open fun showLoading() {
        loading.show(supportFragmentManager, "loading")
    }

    open fun dismissLoading() {
        loading.dismiss()
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