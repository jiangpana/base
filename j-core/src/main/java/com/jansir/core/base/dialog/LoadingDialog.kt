package com.jansir.core.base.dialog

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.jansir.core.R

/**
 * author: jansir
 * e-mail: xxx
 * date: 2020/6/10.
 */
class LoadingDialog : DialogFragment() {
    companion object {
        fun getInstance(): LoadingDialog = LoadingDialog()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            val params = attributes?.apply {
                gravity = Gravity.CENTER
                dimAmount = 0.0f;
            }
            dialog?.window?.setDimAmount(0F)
            setAttributes(params)
            // 背景透明
            setBackgroundDrawableResource(android.R.color.transparent);


//            设置动画
//            setWindowAnimations(R.style.BottomDialog_Animation);
//            设置边距
//            val dm =  DisplayMetrics();
//            getActivity()?.getWindowManager()?.getDefaultDisplay()?.getMetrics(dm);
//            setLayout(((dm.widthPixels * 0.72).toInt()), ViewGroup.LayoutParams.WRAP_CONTENT);
        }


    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setCanceledOnTouchOutside(false);
            setCancelable(false)
        }
        return inflater.inflate(R.layout.dialog_base_loading, container);
    }
}