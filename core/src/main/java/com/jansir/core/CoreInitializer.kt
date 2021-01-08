package com.jansir.core

import android.app.Application
import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.startup.Initializer
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.tencent.mmkv.MMKV
import com.wanjian.cockroach.Cockroach
import com.wanjian.cockroach.ExceptionHandler
import com.xuexiang.xui.XUI


/**
 * 包名:com.jansir.core
 */
class CoreInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        ContextHolder.sContext = context
        XUI.init(context as Application?)
        MMKV.initialize(context)
//        installCockroach(context)
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true)
            .methodCount(1)
            .tag("jansir")
            .build();
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }

    private fun installCockroach(context: Application) {
        Cockroach.install(context
            , object : ExceptionHandler() {
                override fun onUncaughtExceptionHappened(thread: Thread?, throwable: Throwable?) {
                    Log.e(
                        "AndroidRuntime",
                        "--->onUncaughtExceptionHappened:" + thread + "<---",
                        throwable
                    );

                }

                override fun onBandageExceptionHappened(throwable: Throwable?) {
                    throwable?.printStackTrace();//打印警告级别log，该throwable可能是最开始的bug导致的，无需关心
                }

                override fun onEnterSafeMode() {
                }

                override fun onMayBeBlackScreen(e: Throwable?) {
                    super.onMayBeBlackScreen(e)
                    val thread = Looper.getMainLooper().thread
                    Log.e(
                        "AndroidRuntime",
                        "--->onUncaughtExceptionHappened:$thread<---",
                        e
                    )
                }
            }
        )
    }


}