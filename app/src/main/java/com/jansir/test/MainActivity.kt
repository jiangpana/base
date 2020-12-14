package com.jansir.test

import android.app.Activity
import android.os.Bundle
import com.jansir.core.base.activity.BaseActivity
import com.jansir.core.base.annotation.BindLayout

@BindLayout(R.layout.activity_main)
class MainActivity : BaseActivity() {
    override val isUseBaseTitleBar: Boolean
        get() = true

    override fun initView() {

    }

    override fun initListener() {
    }

}