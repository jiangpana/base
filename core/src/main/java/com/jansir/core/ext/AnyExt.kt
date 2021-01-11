package com.jansir.core.ext

import android.content.Context
import android.os.Vibrator
import com.jansir.core.ContextHolder

/**
 * 包名:com.jansir.core.ext
 */

/**
 * Vibrate
 * 震动
 */
fun Any.vibrate() {
    val v =
        ContextHolder.sContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    v.vibrate(100)
}