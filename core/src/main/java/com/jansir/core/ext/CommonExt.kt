package com.jansir.core.ext


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.ColorStateList
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
fun Context.toast(msg: CharSequence) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
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


/** json相关 **/
fun Any.toJson() = Gson().toJson(this)

inline fun <reified T> String.toBean() = Gson().fromJson<T>(this, object : TypeToken<T>() {}.type)


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


/** 网络相关 **/
/**
 * 当前网络是否有连接
 */
fun Any.isNetworkConnected() = NetworkUtils.isConnected()

/**
 * 当前是否是Wifi连接
 */
fun Any.isWifiConnected() = NetworkUtils.isWifiConnected()

/**
 * 当前是否是移动数据连接
 */
fun Any.isMobileConnected() = NetworkUtils.isMobileData()


/**
 * 数组转bundle
 */
fun Array<out Pair<String, Any?>>.toBundle(): Bundle? {
    return Bundle().apply {
        forEach { it ->
            val value = it.second
            when (value) {
                null -> putSerializable(it.first, null as Serializable?)
                is Int -> putInt(it.first, value)
                is Long -> putLong(it.first, value)
                is CharSequence -> putCharSequence(it.first, value)
                is String -> putString(it.first, value)
                is Float -> putFloat(it.first, value)
                is Double -> putDouble(it.first, value)
                is Char -> putChar(it.first, value)
                is Short -> putShort(it.first, value)
                is Boolean -> putBoolean(it.first, value)
                is Serializable -> putSerializable(it.first, value)
                is Parcelable -> putParcelable(it.first, value)

                is IntArray -> putIntArray(it.first, value)
                is LongArray -> putLongArray(it.first, value)
                is FloatArray -> putFloatArray(it.first, value)
                is DoubleArray -> putDoubleArray(it.first, value)
                is CharArray -> putCharArray(it.first, value)
                is ShortArray -> putShortArray(it.first, value)
                is BooleanArray -> putBooleanArray(it.first, value)

                is Array<*> -> when {
                    value.isArrayOf<CharSequence>() -> putCharSequenceArray(
                        it.first,
                        value as Array<CharSequence>
                    )
                    value.isArrayOf<String>() -> putStringArray(it.first, value as Array<String>)
                    value.isArrayOf<Parcelable>() -> putParcelableArray(
                        it.first,
                        value as Array<Parcelable>
                    )
                }
            }
        }
    }

}


fun Any.runOnUIThread(action: () -> Unit) {
    Handler(Looper.getMainLooper()).post { action() }
}

/**
 * 将Bitmap保存到相册
 */
fun Bitmap.saveToAlbum(
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
    quality: Int = 100,
    filename: String = "",
    callback: ((path: String?, uri: Uri?) -> Unit)? = null
) {
    GlobalScope.launch {
        try {
            //1. create path
            val dirPath =
                Environment.getExternalStorageDirectory().absolutePath + "/" + Environment.DIRECTORY_PICTURES
            val dirFile = File(dirPath)
            if (!dirFile.exists()) dirFile.mkdirs()
            val ext = when (format) {
                Bitmap.CompressFormat.PNG -> ".png"
                Bitmap.CompressFormat.JPEG -> ".jpg"
                Bitmap.CompressFormat.WEBP -> ".webp"
                else -> ""
            }
            val target = File(
                dirPath,
                (if (filename.isEmpty()) System.currentTimeMillis().toString() else filename) + ext
            )
            if (target.exists()) target.delete()
            target.createNewFile()
            //2. save
            compress(format, quality, FileOutputStream(target))
            //3. notify
            MediaScannerConnection.scanFile(
                ContextHolder.sContext, arrayOf(target.absolutePath),
                arrayOf("image/$ext")
            ) { path, uri ->
                runOnUIThread {
                    callback?.invoke(path, uri)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            runOnUIThread { callback?.invoke(null, null) }
        }
    }
}

//******************* hook view *******************//
fun Dialog.hookView() {
    window?.decorView?.viewTreeObserver?.addOnGlobalLayoutListener {
        try {
            setViewProxy(window?.decorView as ViewGroup)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

    }
}

fun Activity.hookView() {
    window?.decorView?.viewTreeObserver?.addOnGlobalLayoutListener {
        try {
            setViewProxy(window?.decorView as ViewGroup)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

    }
    try {
        setViewProxy(window?.decorView as ViewGroup)
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    } catch (e: InvocationTargetException) {
        e.printStackTrace()
    }
}

private fun setViewProxy(viewGroup: ViewGroup) {
    val count = viewGroup.childCount
    for (i in 0 until count) {
        if (viewGroup.getChildAt(i) is ViewGroup) {
            hook(viewGroup.getChildAt(i))
            setViewProxy(viewGroup.getChildAt(i) as ViewGroup)
        } else {
            hook(viewGroup.getChildAt(i))
        }
    }
}

@SuppressLint("PrivateApi", "DiscouragedPrivateApi")
private fun hook(view: View) {
    try {
        if (view is Button) {
            view.setOnTouchListener(OnTouchListenerProxy())
        }
        val getListenerInfo = View::class.java.getDeclaredMethod("getListenerInfo")
        getListenerInfo.isAccessible = true
        val listenerInfo = getListenerInfo.invoke(view)
        val listenerInfoClazz = Class.forName("android.view.View\$ListenerInfo")
        val mOnClickListener = listenerInfoClazz.getDeclaredField("mOnClickListener")
        mOnClickListener.isAccessible = true
        val originOnClickListener = mOnClickListener.get(listenerInfo) ?: return
        if (originOnClickListener is WrapperOnClickListener) {
            return
        } else {
            val proxyOnClick =
                WrapperOnClickListener(originOnClickListener as View.OnClickListener?)
            mOnClickListener.set(listenerInfo, proxyOnClick)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

class WrapperOnClickListener(onClickListener: View.OnClickListener?) : View.OnClickListener {
    override fun onClick(v: View?) {
    }

}

class OnTouchListenerProxy : View.OnTouchListener {
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

//*******************  aes 256 cbc *******************//
val aesKey = "CC0071FBA83435EAE99321471E6265A7"
val aesIv = "B64EF8829EEFC32C"
fun Context.getAssetsText256AesDecrypt(name: String): String {
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

fun openMMkv(name: String): MMKV {
    if (name.isEmpty()) {
        return MMKV.defaultMMKV()
    } else {
        return MMKV.mmkvWithID(name)
    }
}

fun String.putMMkvValue(name: String = "", value: Any) {
    when (value) {
        is String -> openMMkv(name).encode(this, value)
        is Int -> openMMkv(name).encode(this, value)
        is Boolean -> openMMkv(name).encode(this, value)
        is Float -> openMMkv(name).encode(this, value)
        is Double -> openMMkv(name).encode(this, value)
        is ByteArray -> openMMkv(name).encode(this, value)
        is Parcelable -> openMMkv(name).encode(this, value)
    }

}

inline fun <reified T > String.getMMkvValue(name: String = ""): T? {
    return when (T::class) {
        String::class -> openMMkv(name).decodeString(this) as T
        Int::class -> openMMkv(name).decodeInt(this) as T
        Boolean::class -> openMMkv(name).decodeBool(this, true) as T
        Double::class -> openMMkv(name).decodeDouble(this) as T
        Long::class -> openMMkv(name).decodeLong(this) as T
        Set::class -> openMMkv(name).decodeStringSet(this) as T
        ByteArray::class -> openMMkv(name).decodeBytes(this) as T
        else -> null
    }

}

inline fun <reified T :Parcelable> String.getMMkvValueParcelable(name: String = ""): T? {
    return   openMMkv(name).decodeParcelable(this, T::class.java) as T
}