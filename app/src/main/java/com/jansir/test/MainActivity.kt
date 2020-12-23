package com.jansir.test

import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import com.jansir.core.base.activity.BaseVMActivity
import com.jansir.core.ext.click
import com.jansir.core.ext.loge
import com.jansir.core.ext.toast
import com.jansir.test.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType

class MainActivity : BaseVMActivity<ActivityMainBinding, MainViewModel>() {


    override fun initListener() {

    }

    override fun initView() {
val bind=DataBindingUtil.bind<ActivityMainBinding>(binding.root)

    }

    override fun initData() {
    }


}