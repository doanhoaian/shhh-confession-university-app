package vn.dihaver.tech.shhh.confession.feature.comment.ui.state

data class CommentUiModel(
    val id: String,
    val authorName: String,
    val authorAvatarUrl: String,
    val schoolName: String,
    val content: String,
    val timeAgo: String,
    val isEdited: Boolean,
    val totalLikes: Long,
    val totalDislikes: Long,
    val totalReply: Long,

    val isLiked: Boolean,
    val isDisliked: Boolean,

    val isReply: Boolean = false,
    val replies: List<CommentUiModel> = emptyList(),
    val areRepliesLoading: Boolean = false,
    val canLoadMoreReplies: Boolean = false,
    val isExpanded: Boolean = false,

    val isPostOwner: Boolean = false
)