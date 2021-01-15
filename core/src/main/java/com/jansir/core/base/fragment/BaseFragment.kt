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

    protected lateinit var mActivity: SupportActivity
    protected lateinit var activityBaseBinding: ActivityBaseBinding
    protected lateinit var binding: VB
    protected abstract fun initView()
    protected abstract fun initListener()

    /**
     * if true use TitleBar
     */
    protected open val isUseBaseTitleBar: Boolean
        get() = false


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mActivity = activity as SupportActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activityBaseBinding = inflateBinding(inflater)
        binding = inflateBinding(
            findClazzFromSuperclassGeneric(ViewBinding::class.java) as Class<VB>,
            layoutInflater
        )
        activityBaseBinding.apply {
            root.findViewById<FrameLayout>(R.id.flContainer)
                .addView(binding.root)
            statusViewBase.apply {
                showContent()
                setOnRetryClickListener {
                    retry()
                }
            }
        }

        return activityBaseBinding.root

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

}
