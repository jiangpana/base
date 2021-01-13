package com.jansir.core.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import com.jansir.core.R
import com.jansir.core.databinding.LayoutSuperEdittextBinding
import com.jansir.core.ext.appendColorSpan
import com.jansir.core.ext.dp2px
import com.jansir.core.ext.invisible
import com.jansir.core.ext.visible


/**
 * author: jansir
 * e-mail: xxx
 * date: 2020/5/13.
 */
class SuperEditText @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr) {

    var mEditTextHeight = dp2px(40f)
    var mTitleText = ""
    var mHintText = ""
    var isBottomText = false
    var isPassword = false
    private var bind: LayoutSuperEdittextBinding

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.SuperEditText)
        mEditTextHeight = ta.getDimension(R.styleable.SuperEditText_set_editTextHeight, 40F).toInt()
        mTitleText = ta.getString(R.styleable.SuperEditText_set_titleString) ?: ""
        mHintText = ta.getString(R.styleable.SuperEditText_set_bottomString) ?: ""
        isBottomText = ta.getBoolean(R.styleable.SuperEditText_set_isBottomText, false)
        isPassword = ta.getBoolean(R.styleable.SuperEditText_set_isPassword, false)
        ta.recycle()
        bind = LayoutSuperEdittextBinding.bind(this)
        applyAttr()
    }

    private fun applyAttr() {
        if (mTitleText == "") {
            bind.tv.invisible()
        } else {
            bind.tv.text = mTitleText
            bind.tv.appendColorSpan("*", Color.parseColor("#FF0000"))

        }
        if (isBottomText) {
            bind.tvBottom.apply {
                text = mHintText
                visible()
            }
        } else {
            if (isPassword) {
                bind.mEditPassword.apply {
                    visible()
                    clearFocus()
                }
            } else {
                bind.mEdit.apply {
                    visible()
                    clearFocus()
                    hint = mHintText
                    height = mEditTextHeight
                }
            }
        }
    }
}