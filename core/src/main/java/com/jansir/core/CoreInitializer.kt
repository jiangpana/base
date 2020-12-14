package com.jansir.core

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import com.tencent.mmkv.MMKV
import com.xuexiang.xui.XUI

/**
 * 包名:com.jansir.core
 */
class CoreInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        ContextHolder.sContext = context
        XUI.init(context as Application?)
        MMKV.initialize(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }

}