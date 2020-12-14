package com.jansir.core.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import com.jansir.core.R
import com.jansir.core.ext.appendColorSpan
import com.jansir.core.ext.dp2px
import com.jansir.core.ext.invisible
import com.jansir.core.ext.visible
import kotlinx.android.synthetic.main.layout_super_edittext.view.*


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

    init {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.SuperEditText)
        mEditTextHeight = ta.getDimension(R.styleable.SuperEditText_set_editTextHeight, 40F).toInt()
        mTitleText = ta.getString(R.styleable.SuperEditText_set_titleString) ?: ""
        mHintText = ta.getString(R.styleable.SuperEditText_set_bottomString) ?: ""
        isBottomText = ta.getBoolean(R.styleable.SuperEditText_set_isBottomText, false)
        isPassword = ta.getBoolean(R.styleable.SuperEditText_set_isPassword, false)
        ta.recycle()
        inflate(context, R.layout.layout_super_edittext, this)
        applyAttr()
    }

    private fun applyAttr() {
        if (mTitleText == "") {
            tv.invisible()
        } else {
            tv.text = mTitleText
            tv.appendColorSpan("*", Color.parseColor("#FF0000"))

        }
        if (isBottomText) {
            tvBottom.apply {
                text = mHintText
                visible()
            }
        } else {
            if (isPassword) {
                mEditPassword.apply {
                    visible()
                    clearFocus()
                }
            } else {
                mEdit.apply {
                    visible()
                    clearFocus()
                    hint = mHintText
                    height = mEditTextHeight
                }
            }
        }
    }
}