package com.jansir.core.base.activity

import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.viewbinding.ViewBinding
import com.jansir.core.R
import com.jansir.core.databinding.ActivityBaseBinding
import com.jansir.core.ext.findClazzFromSuperclassGeneric
import com.jansir.core.ext.hideKeyboard
import com.jansir.core.ext.inflateBinding
import com.jansir.core.ext.inflateLazyVB
import com.jansir.core.util.ScreenAdapterUtil
import com.jansir.core.util.StatusBarAdapterUtil
import com.jansir.core.util.StatusBarUtil

import kotlinx.coroutines.*
import me.yokeyword.fragmentation.SupportActivity
import java.lang.ref.WeakReference

/**
 * author: jansir
 * e-mail: xxx
 * date: 2019/9/2.
 */
abstract class BaseActivity<VB : ViewBinding> : SupportActivity(),
    CoroutineScope by MainScope() {

    protected val baseBinding by inflateLazyVB<ActivityBaseBinding>()
    protected lateinit var binding: VB

    // true -> 状态栏黑色图标
    protected open val isStatusBarIconDarkMode: Boolean
        get() = true

    // true -> 使用基础标题栏
    protected open val isUseBaseTitleBar: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ScreenAdapterUtil.adapterScreen(this,640,true)
        StatusBarUtil.setTranslucentStatus(this)
        StatusBarAdapterUtil.adaptive(WeakReference(this))
        setContentView(baseBinding.root)
        binding = inflateBinding(findClazzFromSuperclassGeneric(ViewBinding::class.java) as Class<VB>,layoutInflater)
        baseBinding.root.findViewById<FrameLayout>(R.id.fl_base_container)
            .addView(binding.root)

        if (isUseBaseTitleBar) {
            baseBinding.mTitleBarBase.apply {
                visibility = View.VISIBLE
                setLeftClickListener { finish() }
                setTitleColor(resources.getColor(R.color.text_333333))
                setBackgroundColor(Color.WHITE)
            }
        }
        baseBinding.mStatusView.apply {
            showContent()
            setOnRetryClickListener {
                retry()
            }
        }
        initView()
        initListener()
    }



    protected open fun retry() {
    }

    abstract fun initView()
    abstract fun initListener()
    abstract fun initData()

    protected fun setTitleText(@StringRes stringId: Int) {
        if (isUseBaseTitleBar) baseBinding.mTitleBarBase.setTitle(stringId)
    }

    /**
     *
     * 字体不随系统变化
     * 适配 -> https://juejin.im/post/5c18039d5188253b7e74987e
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        if (newConfig.fontScale != 1f) {
            resources
        }
        super.onConfigurationChanged(newConfig)
    }

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
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }


}

