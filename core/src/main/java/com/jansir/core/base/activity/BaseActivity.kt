package com.jansir.core.base.activity

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.StringRes
import butterknife.ButterKnife
import butterknife.Unbinder
import com.jansir.core.R
import com.jansir.core.base.annotation.BindLayout
import com.jansir.core.ext.hideKeyboard
import com.jansir.core.util.RomUtil
import com.jansir.core.util.StatusBarUtil

import com.xuexiang.xui.widget.actionbar.TitleBar
import com.xuexiang.xui.widget.statelayout.MultipleStatusView
import kotlinx.coroutines.*
import me.yokeyword.fragmentation.SupportActivity
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * author: jansir
 * e-mail: xxx
 * date: 2019/9/2.
 */
abstract class BaseActivity : SupportActivity(), CoroutineScope by MainScope() {


    // true -> 状态栏黑色图标
    protected open val isStatusBarIconDarkMode: Boolean
        get() = true

    // true -> 使用基础标题栏
    protected open val isUseBaseTitleBar: Boolean
        get() = true

    protected open fun onGetBundle(bundle: Bundle) {}
    var mTitleBar: TitleBar? = null
    protected lateinit var mStatusView: MultipleStatusView
    private lateinit var mUnBinder: Unbinder


    //覆盖此方法获取layout id
    protected open val layoutId: Int
        get() = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setTranslucentStatus(this)
        val clazz = this@BaseActivity::class.java
        val contentView =
            (View.inflate(this, R.layout.activity_base, null) as ViewGroup).apply {
                setContentView(this)
                View.inflate(
                    this@BaseActivity, if (clazz.isAnnotationPresent(BindLayout::class.java))
                        clazz.getAnnotation(BindLayout::class.java)?.id
                            ?: layoutId else layoutId, findViewById(R.id.fl_base_container)
                )
            }

        if (isUseBaseTitleBar) {
            mTitleBar = contentView.findViewById<TitleBar>(R.id.mTitleBarBase).apply {
                visibility = View.VISIBLE
                setLeftClickListener { finish() }
                setTitleColor(resources.getColor(R.color.text_333333))
                setBackgroundColor(Color.WHITE)
            }
        }
        mUnBinder = ButterKnife.bind(this)
        intent?.extras?.let {
            onGetBundle(it)
        }
        mStatusView = contentView.findViewById<MultipleStatusView>(R.id.mStatusView).apply {
            showContent()
        }
        mStatusView.setOnRetryClickListener {
            retry()
        }
        initView()
        initListener()
        adaptScreen()
    }

    private fun adaptScreen() {
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

    /**
     * 判断小米是否有刘海屏
     */
    open fun xiaomiHasNotch(context: Context): Boolean {
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

    open fun setXiaomiFullScreenWindowLayoutInDisplayCutout(window: Window?) {
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
    open fun hwHasNotch(context: Context): Boolean {
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
    open fun setHuaweiFullScreenWindowLayoutInDisplayCutout(window: Window?) {
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

    protected open fun retry() {
    }

    abstract fun initView()
    abstract fun initListener()

    protected fun getTitleBar() = if (isUseBaseTitleBar) mTitleBar else null
    protected fun setTitleText(@StringRes stringId: Int) {
        if (isUseBaseTitleBar) mTitleBar?.setTitle(stringId)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        if (newConfig.fontScale != 1f) {
            resources
        }
        super.onConfigurationChanged(newConfig)
    }

    /**
     * 适配 -> https://juejin.im/post/5c18039d5188253b7e74987e
     */
    override fun getResources(): Resources {
        val res = super.getResources()
        if (res.configuration.fontScale != 1f) {//非默认值
            val newConfig = Configuration()
            newConfig.setToDefaults()//设置默认
            res.updateConfiguration(newConfig, res.displayMetrics)
        }
        return res
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                val view = currentFocus
                hideKeyboard(ev, view)    //调用方法判断是否需要隐藏键盘
            }
            else -> {
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onResume() {
        super.onResume()
        if (isStatusBarIconDarkMode) {
            StatusBarUtil.setStatusBarDarkTheme(this, true)
        } else {
            StatusBarUtil.setStatusBarDarkTheme(this, false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
        mUnBinder.unbind()

    }


}

