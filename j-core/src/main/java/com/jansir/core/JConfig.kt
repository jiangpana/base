package com.jansir.core

import android.content.Context

/**
 * 包名:com.jansir.base
 */
object JConfig {
    lateinit var context: Context
    var isDebug = true
    var defaultLogTag = "jansir"
    var sharedPrefName = "sp"
}