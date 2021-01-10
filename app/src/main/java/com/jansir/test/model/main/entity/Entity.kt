package com.jansir.test.model.main.entity

/**
 * 包名:com.jansir.test.model.main.entity
 */

data class Chapter(
    val children: List<Any>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)