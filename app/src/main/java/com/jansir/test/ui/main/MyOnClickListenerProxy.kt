package com.jansir.test.ui.main

import android.view.View
import com.jansir.core.ext.AbsOnClickListenerProxy
import com.jansir.core.ext.e

/**
 * 包名:com.jansir.test.ui.main
 */
class MyOnClickListenerProxy : AbsOnClickListenerProxy() {
    override fun onClick(v: View?) {
        "代理了".e()
        super.onClick(v)
    }
}