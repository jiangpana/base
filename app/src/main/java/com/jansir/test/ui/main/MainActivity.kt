package com.jansir.test.ui.main

import com.jansir.core.base.activity.BaseVMActivity
import com.jansir.core.ext.ScaleTouchListenerProxy
import com.jansir.core.ext.click
import com.jansir.core.ext.hookView
import com.jansir.core.ext.toast
import com.jansir.test.databinding.ActivityMainBinding
import com.jansir.test.data.main.viewmodel.MainViewModel
import com.orhanobut.logger.Logger
import kotlinx.coroutines.*

class MainActivity : BaseVMActivity<ActivityMainBinding, MainViewModel>() {


    override fun initView() {
        hookView(ScaleTouchListenerProxy(), MyOnClickListenerProxy())
    }

    override fun initListener() {
        binding.tvTest.click {
            toast("点击了")
            launch() {
                delay(30000)
                val a = withContext(Dispatchers.IO) {
                    Logger.e("测试")
                    delay(10000)
                   1
                }
                Logger.e("a = ${a}")
                val b = async(Dispatchers.IO) {
                    delay(3000)
                   2
                }
                val c = async {
                    delay(3000)
                    2
                }
                Logger.e((b.await()+c.await()).toString())
            }

        }

    }

    override fun initObserver() {

    }

    override fun initData() {

    }


}