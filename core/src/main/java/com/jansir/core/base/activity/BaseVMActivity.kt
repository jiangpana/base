package com.jansir.core.base.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.jansir.core.base.dialog.LoadingDialog
import com.jansir.core.base.viewmodel.*
import com.jansir.core.ext.findClazzFromSuperclassGeneric
import java.lang.reflect.ParameterizedType

/**
 * author: jansir
 * e-mail: xxx
 * date: 2019/9/2.
 */
abstract class BaseVMActivity<VB:ViewBinding,VM : BaseViewModel> : BaseActivity<VB>() {

    protected val viewModel: VM by lazy {
        getVM()
    }

    private fun getVM(): VM {
        return ViewModelProvider(this).get(findClazzFromSuperclassGeneric(ViewModel::class.java)) as VM
    }


    private var loading: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startObserve()
        initObserver()
    }

    abstract fun initObserver()


    protected open fun startObserve() {
        viewModel.mStateLiveData.observe(this, Observer { stateActionState ->
            when (stateActionState) {
                LoadState -> showLoading()
                SuccessState -> {
                    showContentView()
                    dismissLoading()
                }
                DataErrorState -> {
                    dismissLoading()
                    handleDataError()
                }
                is NetErrorState -> {
                    dismissLoading()
                    handleNetWorkError()
                }
            }
        })
    }


    private fun showContentView() {
        baseBinding.mStatusView.showContent()
    }


    open fun showLoading() {
        if (loading == null) {
            loading = LoadingDialog.getInstance()
        }
        loading?.show(supportFragmentManager, "loading")
    }

    open fun dismissLoading() {
        loading?.dismiss()
    }

    open fun handleNetWorkError() {
        if (isShowNoNetView) baseBinding.mStatusView.showNoNetwork()
    }

    open fun handleDataError() {
        if (isShowNoDataView) {
            baseBinding.mStatusView.showError()
        }
    }

    protected open val isShowNoDataView: Boolean
        get() = false

    protected open val isShowNoNetView: Boolean
        get() = true
}