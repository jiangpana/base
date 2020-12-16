package com.jansir.core.base.swipe

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.jansir.core.base.activity.BaseActivity
import me.yokeyword.fragmentation.SwipeBackLayout
import me.yokeyword.fragmentation_swipeback.core.ISwipeBackActivity

/**
 * author: jansir
 * e-mail: xxx
 * date: 2019/9/4.
 */
abstract  class BaseSwipeActivity <VB:ViewBinding>:
    BaseActivity<VB>(), ISwipeBackActivity {
    internal val mDelegate = NSwipeBackActivityDelegate(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDelegate.onCreate(savedInstanceState)
        setEdgeLevel(SwipeBackLayout.EdgeLevel.MAX)
        //超过0.4
        swipeBackLayout.setScrollThresHold(0.34.toFloat())

        mDelegate.setSwipeBackEnable(false)
    }


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mDelegate.onPostCreate(savedInstanceState)
    }

    override fun getSwipeBackLayout(): SwipeBackLayout{
        return mDelegate.getSwipeBackLayout()
    }

    /**
     * 是否可滑动
     * @param enable
     */
    override fun setSwipeBackEnable(enable: Boolean) {
        mDelegate.setSwipeBackEnable(enable)
    }

    override fun setEdgeLevel(edgeLevel: SwipeBackLayout.EdgeLevel) {
        mDelegate.setEdgeLevel(edgeLevel)
    }

    override fun setEdgeLevel(widthPixel: Int) {
        mDelegate.setEdgeLevel(widthPixel)
    }

    /**
     * 限制SwipeBack的条件,默认栈内Fragment数 <= 1时 , 优先滑动退出Activity , 而不是Fragment
     *
     * @return true: Activity优先滑动退出;  false: Fragment优先滑动退出
     */
    override fun swipeBackPriority(): Boolean {
        return mDelegate.swipeBackPriority()
    }



    override fun finish() {
        super.finish()
        overridePendingTransition(
            com.jansir.core.R.anim.slide_left_in,
            com.jansir.core.R.anim.slide_right_out
        )
    }
}