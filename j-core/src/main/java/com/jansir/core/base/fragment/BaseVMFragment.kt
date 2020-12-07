package com.jansir.core.base.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jansir.core.base.dialog.LoadingDialog
import com.jansir.core.base.viewmodel.*
import java.lang.reflect.ParameterizedType

/**
 * author: jansir
 * e-mail: xxx
 * date: 2019/9/2.
 */

abstract class BaseVMFragment<VM : BaseViewModel> : BaseFragment() {

    protected val viewModel: VM by lazy {
        getVM()
    }



    private fun getVM(): VM {
        return ViewModelProvider(this@BaseVMFragment).get(getVmClazz(this))
    }

    private fun getVmClazz(obj: Any): Class<VM> {
        return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
    }

    private var loading: LoadingDialog? = null


    private var mIsHasData = false//是否加载过数据

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startObserve()
    }

    protected open fun startObserve() {
        viewModel.let { baseViewModel ->
            baseViewModel.mStateLiveData.observe(this, Observer { stateActionState ->
                when (stateActionState) {
                    LoadState -> showLoading()
                    SuccessState ->{
                        showContentView()
                        dismissLoading()
                    }
                    DataErrorState ->{
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
    }


    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        initData()
    }

    abstract fun initData()

    open fun showLoading() {
        if (loading == null) {
            loading = LoadingDialog.getInstance()
        }
        loading?.show(activity?.supportFragmentManager!!, "loading")
    }

    open fun dismissLoading() {
        loading?.dismiss()
    }

    fun showContentView() {
        mStatusView.showContent()
    }

    open fun handleNetWorkError() {
        if(isShowNoNetView) mStatusView.showNoNetwork()
    }

    open fun handleDataError() {
       if(isShowNoDataView){
           mStatusView.showError()
       }
    }

    protected open val isShowNoDataView: Boolean
        get() = false
    protected open val isShowNoNetView: Boolean
        get() = true
}