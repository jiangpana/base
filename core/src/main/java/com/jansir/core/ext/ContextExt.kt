package com.jansir.core.ext


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.*
import android.util.Base64
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jansir.core.ContextHolder
import com.jansir.core.util.AES256Util
import com.jansir.core.util.NetworkUtils
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.Serializable
import java.lang.reflect.InvocationTargetException


/**
 * Description:  通用扩展
 * Create by dance, at 2018/12/5
 */

/** dp和px转换 **/
fun Context.dp2px(dpValue: Float): Int {
    return (dpValue * resources.displayMetrics.density + 0.5f).toInt()
}

fun Context.px2dp(pxValue: Float): Int {
    return (pxValue / resources.displayMetrics.density + 0.5f).toInt()
}

fun Context.sp2px(spValue: Float): Int {
    return (spValue * resources.displayMetrics.scaledDensity + 0.5f).toInt()
}

fun Context.px2sp(pxValue: Float): Int {
    return (pxValue / resources.displayMetrics.scaledDensity + 0.5f).toInt()
}


fun Fragment.dp2px(dpValue: Float): Int {
    return context!!.dp2px(dpValue)
}

fun Fragment.px2dp(pxValue: Float): Int {
    return context!!.px2dp(pxValue)
}

fun Fragment.sp2px(dpValue: Float): Int {
    return context!!.sp2px(dpValue)
}

fun Fragment.px2sp(pxValue: Float): Int {
    return context!!.px2sp(pxValue)
}


fun View.px2dp(pxValue: Float): Int {
    return context!!.px2dp(pxValue)
}

fun View.dp2px(dpValue: Float): Int {
    return context!!.dp2px(dpValue)
}

fun View.sp2px(dpValue: Float): Int {
    return context!!.sp2px(dpValue)
}

fun View.px2sp(pxValue: Float): Int {
    return context!!.px2sp(pxValue)
}

fun RecyclerView.ViewHolder.px2dp(pxValue: Float): Int {
    return itemView.px2dp(pxValue)
}

fun RecyclerView.ViewHolder.dp2px(dpValue: Float): Int {
    return itemView.dp2px(dpValue)
}

fun RecyclerView.ViewHolder.sp2px(dpValue: Float): Int {
    return itemView.sp2px(dpValue)
}

fun RecyclerView.ViewHolder.px2sp(pxValue: Float): Int {
    return itemView.px2sp(pxValue)
}

/** 动态创建Drawable **/
fun Context.createDrawable(
    color: Int = Color.TRANSPARENT, radius: Float = 0f,
    strokeColor: Int = Color.TRANSPARENT, strokeWidth: Int = 0,
    enableRipple: Boolean = true,
    rippleColor: Int = Color.parseColor("#88999999")
): Drawable {
    val content = GradientDrawable().apply {
        setColor(color)
        cornerRadius = radius
        setStroke(strokeWidth, strokeColor)
    }
    if (Build.VERSION.SDK_INT >= 21 && enableRipple) {
        return RippleDrawable(ColorStateList.valueOf(rippleColor), content, null)
    }
    return content
}

fun Fragment.createDrawable(
    color: Int = Color.TRANSPARENT, radius: Float = 0f,
    strokeColor: Int = Color.TRANSPARENT, strokeWidth: Int = 0,
    enableRipple: Boolean = true,
    rippleColor: Int = Color.parseColor("#88999999")
): Drawable {
    return context!!.createDrawable(
        color,
        radius,
        strokeColor,
        strokeWidth,
        enableRipple,
        rippleColor
    )
}

fun View.createDrawable(
    color: Int = Color.TRANSPARENT, radius: Float = 0f,
    strokeColor: Int = Color.TRANSPARENT, strokeWidth: Int = 0,
    enableRipple: Boolean = true,
    rippleColor: Int = Color.parseColor("#88999999")
): Drawable {
    return context!!.createDrawable(
        color,
        radius,
        strokeColor,
        strokeWidth,
        enableRipple,
        rippleColor
    )
}

fun RecyclerView.ViewHolder.createDrawable(
    color: Int = Color.TRANSPARENT, radius: Float = 0f,
    strokeColor: Int = Color.TRANSPARENT, strokeWidth: Int = 0,
    enableRipple: Boolean = true,
    rippleColor: Int = Color.parseColor("#88999999")
): Drawable {
    return itemView.createDrawable(
        color,
        radius,
        strokeColor,
        strokeWidth,
        enableRipple,
        rippleColor
    )
}


/** toast相关 **/
fun Any.toast(msg: CharSequence) {
    Toast.makeText(ContextHolder.sContext, msg, Toast.LENGTH_SHORT).show()
}

fun Context.longToast(msg: CharSequence) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}

fun Fragment.toast(msg: CharSequence) {
    context?.toast(msg)
}

fun Fragment.longToast(msg: CharSequence) {
    context?.longToast(msg)
}

fun View.toast(msg: CharSequence) {
    context?.toast(msg)
}

fun View.longToast(msg: CharSequence) {
    context?.longToast(msg)
}


/** Window相关 **/
fun Context.windowWidth(): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return windowManager.defaultDisplay.width
}

fun Context.windowHeight(): Int {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return windowManager.defaultDisplay.height
}

fun Fragment.windowWidth(): Int {
    return context!!.windowWidth()
}

fun Fragment.windowHeight(): Int {
    return context!!.windowHeight()
}

fun View.windowWidth(): Int {
    return context!!.windowWidth()
}

fun View.windowHeight(): Int {
    return context!!.windowHeight()
}

fun RecyclerView.ViewHolder.windowWidth(): Int {
    return itemView.windowWidth()
}

fun RecyclerView.ViewHolder.windowHeight(): Int {
    return itemView.windowHeight()
}

/**
 * Dialog hook view
 */
fun Dialog.hookView(absOnTouch: AbsOnTouchListenerProxy, absOnClick: AbsOnClickListenerProxy) {
    window?.decorView?.viewTreeObserver?.addOnGlobalLayoutListener {
        try {
            setViewProxy(window?.decorView as ViewGroup, absOnTouch, absOnClick)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

    }
}

fun Activity.hookView(absOnTouch: AbsOnTouchListenerProxy, absOnClick: AbsOnClickListenerProxy) {
    window?.decorView?.viewTreeObserver?.addOnGlobalLayoutListener {
        try {
            setViewProxy(window?.decorView as ViewGroup, absOnTouch, absOnClick)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

    }
    try {
        setViewProxy(window?.decorView as ViewGroup, absOnTouch, absOnClick)
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    } catch (e: InvocationTargetException) {
        e.printStackTrace()
    }
}

private fun setViewProxy(
    viewGroup: ViewGroup,
    absOnTouch: AbsOnTouchListenerProxy,
    absOnClick: AbsOnClickListenerProxy
) {
    val count = viewGroup.childCount
    for (i in 0 until count) {
        if (viewGroup.getChildAt(i) is ViewGroup) {
            hook(viewGroup.getChildAt(i), absOnTouch, absOnClick)
            setViewProxy(viewGroup.getChildAt(i) as ViewGroup, absOnTouch, absOnClick)
        } else {
            hook(viewGroup.getChildAt(i), absOnTouch, absOnClick)
        }
    }
}

@SuppressLint("PrivateApi", "DiscouragedPrivateApi")
private fun hook(
    view: View,
    absOnTouch: AbsOnTouchListenerProxy,
    absOnClick: AbsOnClickListenerProxy
) {
    try {
        if (view is Button) {
            view.setOnTouchListener(absOnTouch)
        }
        val getListenerInfo = View::class.java.getDeclaredMethod("getListenerInfo")
        getListenerInfo.isAccessible = true
        val listenerInfo = getListenerInfo.invoke(view)
        val listenerInfoClazz = Class.forName("android.view.View\$ListenerInfo")
        val mOnClickListener = listenerInfoClazz.getDeclaredField("mOnClickListener")
        mOnClickListener.isAccessible = true
        val originOnClickListener = mOnClickListener.get(listenerInfo) ?: return
        if (originOnClickListener is AbsOnClickListenerProxy) {
            return
        } else {
            val newAbsOnClick = absOnClick.javaClass.getDeclaredConstructor().newInstance()
            newAbsOnClick.setOriginOnClickListener(originOnClickListener as View.OnClickListener?)
            mOnClickListener.set(listenerInfo, newAbsOnClick)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

abstract class AbsOnTouchListenerProxy : View.OnTouchListener {

}

abstract class AbsOnClickListenerProxy() : View.OnClickListener {

    private var clickListener: View.OnClickListener? = null
    fun setOriginOnClickListener(onClickListener: View.OnClickListener?) {
        clickListener = onClickListener
    }

    override fun onClick(v: View?) {
        clickListener?.onClick(v)
    }
}

class ScaleTouchListenerProxy : AbsOnTouchListenerProxy() {
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                v.scaleX = 0.95f
                v.scaleY = 0.95f
            }
            MotionEvent.ACTION_UP -> {
                v.scaleX = 1.0f
                v.scaleY = 1.0f
            }
        }
        return false
    }
}


//******************* 获取androidmanifest 中meta数据 *******************//
fun Context.getMetaData(key: String): String {
    try {
        val packageManager =
            packageManager
        val applicationInfo = packageManager.getApplicationInfo(
            packageName, PackageManager.GET_META_DATA
        )
        return applicationInfo.metaData.getString(key).toString()
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
    return ""
}

//******************* 获取屏幕物理尺寸  *******************//
fun Context.getScreenSizeOfDevice(show: () -> Unit, hide: () -> Unit) {
    val dm = getResources().getDisplayMetrics();
    val width = dm.widthPixels
    val height = dm.heightPixels
    val x = Math.pow(width.toDouble(), 2.0)
    val y = Math.pow(height.toDouble(), 2.0)
    val diagonal = Math.sqrt(x + y)
    val dens = dm.densityDpi
    val screenInches = diagonal / dens.toDouble()
    Log.e("TAG", "The screenInches $screenInches")

}


fun Context.isTabletDevice(): Boolean {
    return resources
        .configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >=
            Configuration.SCREENLAYOUT_SIZE_LARGE
}


/*
* 对AssetsText,进行aes 256 cbc 解密
* aesKey = "CC0071FBA83435EAE99321471E6265A7"
* aesIv = "B64EF8829EEFC32C"
* */
fun Context.getAssetsText256AesDecrypt(aesKey: String, aesIv: String, name: String): String {
    try {
        val inputStream = assets.open(name)
        val size: Int = inputStream.available()
        val len = -1
        val bytes = ByteArray(size)
        inputStream.read(bytes)
        inputStream.close()
        val string = AES256Util.decrypt(aesKey, aesIv, Base64.decode(bytes, Base64.NO_WRAP))
        return String(string)
    } catch (e: Exception) {
        e.printStackTrace();
    }
    return "";
}

fun Context.getAssetsText(name: String): String {
    try {
        val inputStream = assets.open(name)
        val size: Int = inputStream.available()
        val len = -1
        val bytes = ByteArray(size)
        inputStream.read(bytes)
        inputStream.close()
        val string = String(bytes)
        return string
    } catch (e: Exception) {
        e.printStackTrace();
    }
    return "";
}