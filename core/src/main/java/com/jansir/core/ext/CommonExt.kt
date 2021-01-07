package com.jansir.core.ext

import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jansir.core.ContextHolder
import com.jansir.core.util.NetworkUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.Serializable

/**
 * 包名:com.jansir.core.ext
 */

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

fun Any.runOnUIThread(action: () -> Unit) {
    Handler(Looper.getMainLooper()).post { action() }
}

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
/** json相关 **/
fun Any.toJson() = Gson().toJson(this)

inline fun <reified T> String.toBean() = Gson().fromJson<T>(this, object : TypeToken<T>() {}.type)