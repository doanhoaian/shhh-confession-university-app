package vn.dihaver.tech.shhh.confession.feature.home.ui.model

data class CommentUiModel(
    val id: String,
    val authorName: String,
    val authorAvatarUrl: String,
    val schoolName: String,
    val content: String,
    val timeAgo: String,
    val isEdited: Boolean,
    val totalReply: Long
)