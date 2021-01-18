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

    lateinit var binding: LayoutSuperEdittextBinding
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
        binding = LayoutSuperEdittextBinding.bind(this)
        applyAttr()
    }

    private fun applyAttr() {
        if (mTitleText == "") {
            binding.tv.invisible()
        } else {
            binding.tv.text = mTitleText
            binding.tv.appendColorSpan("*", Color.parseColor("#FF0000"))

        }
        if (isBottomText) {
            binding.tvBottom.apply {
                text = mHintText
                visible()
            }
        } else {
            if (isPassword) {
                binding.mEditPassword.apply {
                    visible()
                    clearFocus()
                }
            } else {
                binding.mEdit.apply {
                    visible()
                    clearFocus()
                    hint = mHintText
                    height = mEditTextHeight
                }
            }
        }
    }
}