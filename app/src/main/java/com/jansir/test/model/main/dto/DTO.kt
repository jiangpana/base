package com.jansir.test.model.main.dto

/**
 * 包名:com.jansir.test.model.main.entity
 */

data class ChapterDTO(
    val children: List<Any>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)