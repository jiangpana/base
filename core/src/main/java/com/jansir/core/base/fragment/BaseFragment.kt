package com.jansir.core.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewbinding.ViewBinding
import com.jansir.core.R
import com.jansir.core.databinding.ActivityBaseBinding
import com.jansir.core.ext.findClazzFromSuperclassGeneric
import com.jansir.core.ext.inflateBinding
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
abstract class BaseFragment<VB : ViewBinding> : SupportFragment(), CoroutineScope by MainScope() {

    lateinit var mActivity: SupportActivity
    protected lateinit var baseBinding :ActivityBaseBinding
    protected lateinit var binding: VB
    protected abstract fun initView()
    protected abstract fun initListener()

    // true -> 使用基础标题栏
    protected open val isUseBaseTitleBar: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as SupportActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        baseBinding = inflateBinding(inflater)
        binding = inflateBinding(findClazzFromSuperclassGeneric(ViewBinding::class.java) as Class<VB>,layoutInflater)
        baseBinding.apply {
            root.findViewById<FrameLayout>(R.id.fl_base_container)
                .addView(binding.root)
            mStatusView.apply {
                showContent()
                setOnRetryClickListener {
                    retry()
                }
            }
        }

        return baseBinding.root

    }

    protected open fun retry() {
    }

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

    }


    //覆盖此方法获取布局 id
    protected open val layoutId: Int
        get() = 0

}
