package com.jansir.test

import com.jansir.core.base.activity.BaseActivity
import com.jansir.core.base.annotation.BindLayout

@BindLayout(R.layout.activity_main)
class MainActivity : BaseActivity() {

    override val isUseBaseTitleBar: Boolean
        get() = true

    override val isStatusBarIconDarkMode: Boolean
        get() = true

    override fun initView() {

    }

    override fun initListener() {
    }

}