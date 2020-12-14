package com.jansir.core.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.jansir.core.R
import com.jansir.core.base.annotation.BindLayout
import com.jansir.core.ext.visible
import com.xuexiang.xui.widget.actionbar.TitleBar
import com.xuexiang.xui.widget.statelayout.MultipleStatusView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import me.yokeyword.fragmentation.SupportActivity
import me.yokeyword.fragmentation.SupportFragment
import me.yokeyword.fragmentation.anim.DefaultVerticalAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator

/**
 * author: jansir
 * e-mail: xxx
 * date: 2019/9/4.
 */
abstract class BaseFragment : SupportFragment(), CoroutineScope by MainScope() {

    lateinit var mContext: SupportActivity

    protected abstract fun initView()
    protected abstract fun initListener()
    private lateinit var unBinder: Unbinder
    lateinit var mTitleBar: TitleBar

    // true -> 使用基础标题栏
    protected open val isUseBaseTitleBar: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity as SupportActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val clazz = this@BaseFragment::class.java
        return inflater.inflate(
            R.layout.activity_base,
            null
        ).apply {
            if (isUseBaseTitleBar) {
                mTitleBar = findViewById<TitleBar>(R.id.mTitleBarBase)
                mTitleBar.visible()
            }
            inflater.inflate(
                if (clazz.isAnnotationPresent(BindLayout::class.java))
                    clazz.getAnnotation(
                        BindLayout::class.java
                    )?.id ?: layoutId
                else layoutId,
                findViewById(R.id.fl_base_container)
            )
            unBinder = ButterKnife.bind(this)
            mStatusView = findViewById<MultipleStatusView>(R.id.mStatusView).apply {
                showContent()
            }
            mStatusView.setOnRetryClickListener {
                retry()
            }
        }
    }

    protected open fun retry() {
    }

    protected lateinit var mStatusView: MultipleStatusView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        return DefaultVerticalAnimator()
    }

    override fun onDestroy() {
        super.onDestroy()
        unBinder.unbind()

    }


    //覆盖此方法获取布局 id
    protected open val layoutId: Int
        get() = 0

}
