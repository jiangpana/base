package com.jansir.test.ui.main

import com.jansir.core.base.activity.BaseVMActivity
import com.jansir.core.ext.ScaleTouchListenerProxy
import com.jansir.core.ext.click
import com.jansir.core.ext.hookView
import com.jansir.core.ext.toast
import com.jansir.test.databinding.ActivityMainBinding
import com.jansir.test.data.main.viewmodel.MainViewModel

class MainActivity : BaseVMActivity<ActivityMainBinding, MainViewModel>() {


    override fun initView() {
        hookView(ScaleTouchListenerProxy(),MyOnClickListenerProxy())
    }

    override fun initListener() {
        binding.tvTest.click {
            toast("点击了")
        }
    }

    override fun initObserver() {

    }

    override fun initData() {

    }


}