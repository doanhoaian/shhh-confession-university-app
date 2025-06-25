package vn.dihaver.tech.shhh.confession.feature.home.ui.model

data class PostUiModel(
    val id: String,
    val authorName: String,
    val authorAvatarUrl: String,
    val schoolName: String,
    val content: String,
    val images: List<String>,
    val timeAgo: String,
    val totalLike: Long,
    val totalDislike: Long,
    val totalComment: Long,
    val isLiked: Boolean,
    val isDisliked: Boolean,
    val isSaved: Boolean = false
)