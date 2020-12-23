package com.jansir.core.util

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.Window
import android.view.WindowManager
import java.lang.ref.WeakReference
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * 包名:com.jansir.core.util
 */
object StatusBarAdapterUtil {

    //屏幕适配
    fun adaptive(activity: WeakReference<Activity>) {
        activity.get()?.apply {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val lp = window.attributes
                    lp.layoutInDisplayCutoutMode =
                        WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER
                    window.attributes = lp
                } else {
                    if (RomUtil.isEmui() && hwHasNotch(this)) {
                        setHuaweiFullScreenWindowLayoutInDisplayCutout(window)
                    }
                    if (RomUtil.isMiui() && xiaomiHasNotch(this)) {
                        setXiaomiFullScreenWindowLayoutInDisplayCutout(window)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 判断小米是否有刘海屏
     */
    private fun xiaomiHasNotch(context: Context): Boolean {
        var ret = false
        try {
            val cl: ClassLoader = context.getClassLoader()
            val SystemProperties = cl.loadClass("android.os.SystemProperties")
            val get: Method = SystemProperties.getMethod(
                "getInt",
                String::class.java,
                Int::class.javaPrimitiveType
            )
            ret = get.invoke(SystemProperties, "ro.miui.notch", 0) as Int == 1
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            return ret
        }
    }

    private fun setXiaomiFullScreenWindowLayoutInDisplayCutout(window: Window?) {
        // 竖屏绘制到耳朵区
        val flag = 0x00000100 or 0x00000200
        try {
            val method: Method = Window::class.java.getMethod(
                "addExtraFlags",
                Int::class.javaPrimitiveType
            )
            method.invoke(window, flag)
        } catch (e: java.lang.Exception) {
            Log.e("test", "addExtraFlags not found.")
        }
    }

    /**
     * 判断华为是否有刘海屏
     */
    private fun hwHasNotch(context: Context): Boolean {
        var ret = false
        try {
            val cl: ClassLoader = context.getClassLoader()
            val HwNotchSizeUtil =
                cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get: Method = HwNotchSizeUtil.getMethod("hasNotchInScreen")
            ret = get.invoke(HwNotchSizeUtil) as Boolean
        } catch (e: ClassNotFoundException) {
            Log.e("test", "hasNotchInScreen ClassNotFoundException")
        } catch (e: NoSuchMethodException) {
            Log.e("test", "hasNotchInScreen NoSuchMethodException")
        } catch (e: java.lang.Exception) {
            Log.e("test", "hasNotchInScreen Exception")
        } finally {
            return ret
        }
    }

    val FLAG_NOTCH_SUPPORT = 0x00010000

    /**
     * 设置应用窗口在华为刘海屏手机使用刘海区
     *
     *
     * 通过添加窗口FLAG的方式设置页面使用刘海区显示
     *
     * @param window 应用页面window对象
     */
    private fun setHuaweiFullScreenWindowLayoutInDisplayCutout(window: Window?) {
        if (window == null) {
            return
        }
        val layoutParams: WindowManager.LayoutParams = window.getAttributes()
        try {
            val layoutParamsExCls =
                Class.forName("com.huawei.android.view.LayoutParamsEx")
            val con =
                layoutParamsExCls.getConstructor(WindowManager.LayoutParams::class.java)
            val layoutParamsExObj: Any = con.newInstance(layoutParams)
            val method =
                layoutParamsExCls.getMethod("addHwFlags", Int::class.javaPrimitiveType)
            method.invoke(layoutParamsExObj, FLAG_NOTCH_SUPPORT)
        } catch (e: ClassNotFoundException) {
            Log.e("test", "hw add notch screen flag api error")
        } catch (e: NoSuchMethodException) {
            Log.e("test", "hw add notch screen flag api error")
        } catch (e: IllegalAccessException) {
            Log.e("test", "hw add notch screen flag api error")
        } catch (e: InstantiationException) {
            Log.e("test", "hw add notch screen flag api error")
        } catch (e: InvocationTargetException) {
            Log.e("test", "hw add notch screen flag api error")
        } catch (e: java.lang.Exception) {
            Log.e("test", "other Exception")
        }
    }
}