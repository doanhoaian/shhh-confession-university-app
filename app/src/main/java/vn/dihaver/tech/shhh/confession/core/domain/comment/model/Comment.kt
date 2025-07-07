package vn.dihaver.tech.shhh.confession.core.domain.comment.model

import vn.dihaver.tech.shhh.confession.core.domain.post.model.Author

data class Comment(
    val id: String,
    val author: Author,
    val content: String,
    val stats: CommentStats,
    val createdAt: Long,
    val updatedAt: Long
)
