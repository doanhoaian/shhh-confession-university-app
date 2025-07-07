package vn.dihaver.tech.shhh.confession.core.domain.post.model

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
