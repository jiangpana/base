package com.jansir.test

import com.jansir.core.base.activity.BaseVMActivity
import com.jansir.core.ext.click
import com.jansir.core.ext.toast
import com.jansir.test.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : BaseVMActivity<ActivityMainBinding, MainViewModel>() {


    override fun initListener() {
        binding.tvTest.click {
            toast("点击了")
        }
    }

    override fun initView() {
        binding.tvTest.text = "测试"
        launch {

        }
    }

    override fun initData() {
    }


}