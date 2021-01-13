package com.jansir.test.ui.main

import android.view.View
import android.widget.Button
import com.jansir.core.base.activity.BaseVMActivity
import com.jansir.core.ext.*
import com.jansir.test.R
import com.jansir.test.databinding.ActivityMainBinding
import com.jansir.test.model.main.viewmodel.MainViewModel
import com.orhanobut.logger.Logger

class MainActivity : BaseVMActivity<ActivityMainBinding, MainViewModel>() {



    override fun initView() {
        hookView(ScaleTouchListenerProxy(), MyOnClickListenerProxy())

    }

    override fun initListener() {

        for (i in 0..20){
            val button = Button(this)
            button.setBackgroundColor(color(R.color.colorAccent))
            button.text ="$i"
            button.id = View.generateViewId()
            button.width =dp2px(40f)
            button.height =dp2px(40f)
            binding. ConstraintLayout.addView(button)
            binding.flow.addView(button)
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