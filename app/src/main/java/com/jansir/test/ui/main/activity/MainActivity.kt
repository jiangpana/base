package com.jansir.test.ui.main.activity

import com.jansir.core.base.activity.BaseVMActivity
import com.jansir.core.ext.ScaleTouchListenerProxy
import com.jansir.core.ext.click
import com.jansir.core.ext.hookView
import com.jansir.core.ext.toast
import com.jansir.test.data.main.viewmodel.MainViewModel
import com.jansir.test.databinding.ActivityMainBinding
import com.jansir.test.ui.main.MyOnClickListenerProxy
import com.orhanobut.logger.Logger


class MainActivity : BaseVMActivity<ActivityMainBinding, MainViewModel>() {


    override fun initView() {
        hookView(
            ScaleTouchListenerProxy(),
            MyOnClickListenerProxy()
        )

    }

    override fun initListener() {
        binding.tvTest.click {
            viewModel.test()
        }
    }

    override fun initObserver() {
        viewModel.user.observeForever {
            Logger.e(it)
        }
        viewModel.testLivedata.observeForever {
            toast(it.toString())
            Logger.e(it.toString())
        }
    }

    override fun initData() {

    }


}