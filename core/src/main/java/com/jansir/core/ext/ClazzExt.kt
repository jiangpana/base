package com.jansir.core.ext

import java.lang.reflect.ParameterizedType

/**
 * 包名:com.jansir.core.ext
 */

//查找此对象是否有clazz及子类的泛型，没有找到则抛出异常
fun <T> Any.findClazzFromSuperclassGeneric(clazz: Class<T>): Class<T> {
    try {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            for (i in type.actualTypeArguments.indices) {
                if ((clazz).isAssignableFrom(type.actualTypeArguments[i] as Class<*>)) {
                    return type.actualTypeArguments[i] as Class<T>
                }
            }
        }

    } catch (e: NoSuchMethodException) {
        e.printStackTrace()
    }
    throw RuntimeException("A generic type of clazz for the given condition was not found")
}

//判断此对象是否有给定的clazz类型注解
fun Any.hasAnnotation(clazz: Class<out Annotation>):Boolean {

    return javaClass.isAnnotationPresent(clazz)
}
