package com.jansir.core.ext

import java.text.SimpleDateFormat
import java.util.*

/**
 * Description: 时间日期相关
 * Create by lxj, at 2018/12/7
 */

/**
 *  字符串日期格式（比如：2018-4-6)转为毫秒
 *  @param format 时间的格式，默认是按照yyyy-MM-dd HH:mm:ss来转换，如果您的格式不一样，则需要传入对应的格式
 */
fun String.toDateMills(format: String = "yyyy-MM-dd HH:mm:ss") = SimpleDateFormat(format, Locale.getDefault()).parse(this).time


/**
 * Long类型时间戳转为字符串的日期格式
 * @param format 时间的格式，默认是按照yyyy-MM-dd HH:mm:ss来转换，如果您的格式不一样，则需要传入对应的格式
 */
fun Long.toDateString(format: String = "yyyy-MM-dd HH:mm:ss") = SimpleDateFormat(format, Locale.getDefault()).format(Date(this))

fun Int.toDateString(format: String = "yyyy-MM-dd HH:mm:ss") = SimpleDateFormat(format, Locale.getDefault()).format(Date(this.toLong()))

//获取一个月总天数
fun Any.getMonthAmount(year: Int, month: Int): Int {
    val a: java.util.Calendar = java.util.Calendar.getInstance()
    a[java.util.Calendar.YEAR] = year
    a[java.util.Calendar.MONTH] = month - 1
    a[java.util.Calendar.DATE] = 1 //把日期设置为当月第一天
    a.roll(java.util.Calendar.DATE, -1) //日期回滚一天，也就是最后一天
    val maxDate: Int = a[java.util.Calendar.DATE]
    return maxDate
}