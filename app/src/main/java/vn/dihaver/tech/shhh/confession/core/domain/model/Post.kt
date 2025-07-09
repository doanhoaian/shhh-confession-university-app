package vn.dihaver.tech.shhh.confession.core.domain.model

import vn.dihaver.tech.shhh.confession.feature.post.model.PostStats
import vn.dihaver.tech.shhh.confession.feature.post.model.PostType

data class Post(
    val id: String,
    val postType: PostType,
    val author: Author,
    val content: String,
    val images: List<String>,
    val stats: PostStats,
    val createdAt: Long,
    val updatedAt: Long
)
