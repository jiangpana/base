package com.jansir.core.base.annotation

/**
 * author: jansir
 * e-mail: xxx
 * date: 2019/12/23.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class BindLayout(val id: Int = 0)
