package com.jansir.core

import android.app.Application
import android.content.Context

/**
 * 包名:com.jansir.base
 */
class JApplication :Application() {
    companion object{
        lateinit var sContext:Context
    }

    override fun onCreate() {
        super.onCreate()
        sContext=applicationContext
    }
}