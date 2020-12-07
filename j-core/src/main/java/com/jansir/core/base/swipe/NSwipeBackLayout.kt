package com.jansir.core.base.swipe

import android.content.Context
import android.view.MotionEvent
import android.view.ViewConfiguration
import me.yokeyword.fragmentation.SwipeBackLayout
import kotlin.math.abs

/**
 * author: jansir
 * e-mail: xxx
 * date: 2019/9/4.
 */
class NSwipeBackLayout(context: Context) : SwipeBackLayout(context) {


    var isEnable =true
    var downX = 0
    var downY = 0
    var isFirst = true

    //判断方向

    private var touchSlop = 0


    init {
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (!isEnable){
            return  super.dispatchTouchEvent(event)
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x.toInt()
                downY = event.y.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                val moveX = event.x
                val moveY = event.y
                val dY = abs(moveY - downY)
                val dX = abs(moveX - downX)
                if(dY>touchSlop || dX>touchSlop){
                    if (isFirst) {
                        if (dY - dX > touchSlop) {
                            setEnableGesture(false)
                        }
                        isFirst = false
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                isFirst = true
                setEnableGesture(true)
            }
        }
        return super.dispatchTouchEvent(event)
    }


}
