package com.jansir.core.ext

import android.os.Parcelable
import com.tencent.mmkv.MMKV

/**
 * 包名:com.jansir.core.ext
 */

/*
* 使用:
* "".putMMkvValue(kvName,value)
* "".getMMkvValue(kvName)
* "".getMMkvValueParcelable(kvName)
* */

fun openMMkv(name: String): MMKV {
    if (name.isEmpty()) {
        return MMKV.defaultMMKV()
    } else {
        return MMKV.mmkvWithID(name)
    }
}

fun String.encodeValue(kvName: String = "", value: Any) {
    when (value) {
        is String -> openMMkv(kvName).encode(this, value)
        is Int -> openMMkv(kvName).encode(this, value)
        is Boolean -> openMMkv(kvName).encode(this, value)
        is Float -> openMMkv(kvName).encode(this, value)
        is Double -> openMMkv(kvName).encode(this, value)
        is ByteArray -> openMMkv(kvName).encode(this, value)
        is Parcelable -> openMMkv(kvName).encode(this, value)
    }

}

fun String.decodeBool(kvName: String = "", defaultValue: Boolean = true) =
    openMMkv(kvName).decodeBool(this, defaultValue)

inline fun <reified T> String.decode(kvName: String = ""): T {
    return when (T::class) {
        String::class -> openMMkv(kvName).decodeString(this, "") as T
        Int::class -> openMMkv(kvName).decodeInt(this) as T
        Double::class -> openMMkv(kvName).decodeDouble(this) as T
        Long::class -> openMMkv(kvName).decodeLong(this) as T
        Set::class -> openMMkv(kvName).decodeStringSet(this, setOf()) as T
        ByteArray::class -> openMMkv(kvName).decodeBytes(this, byteArrayOf()) as T
        else -> throw RuntimeException("not support ${T::class.java.simpleName} decode")
    }

}

inline fun <reified T : Parcelable> String.decodeParcelable(
    kvName: String = "",
    putValue: () -> T? = { null }
): T {
    val value = openMMkv(kvName).decodeParcelable(this, T::class.java)
    if (value == null) {
        openMMkv(kvName).encode(this, putValue())
    } else return value
    return openMMkv(kvName).decodeParcelable(this, T::class.java)
}