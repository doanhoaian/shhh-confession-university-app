package vn.dihaver.tech.shhh.confession.core.domain.model

import vn.dihaver.tech.shhh.confession.feature.comment.model.CommentStats

data class Comment(
    val id: String,
    val author: Author,
    val content: String,
    val stats: CommentStats,
    val createdAt: Long,
    val updatedAt: Long
)
