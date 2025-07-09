package vn.dihaver.tech.shhh.confession.feature.home.data.mapper

import vn.dihaver.tech.shhh.confession.core.domain.model.Author
import vn.dihaver.tech.shhh.confession.core.domain.model.FeedItem
import vn.dihaver.tech.shhh.confession.core.domain.model.Post
import vn.dihaver.tech.shhh.confession.feature.post.model.PostStats
import vn.dihaver.tech.shhh.confession.feature.post.model.PostType
import vn.dihaver.tech.shhh.confession.feature.post.model.UserInteraction
import vn.dihaver.tech.shhh.confession.core.util.DateTimeUtils.isoToEpochMillis
import vn.dihaver.tech.shhh.confession.core.util.DateTimeUtils.toRelativeTime
import vn.dihaver.tech.shhh.confession.feature.home.data.local.entity.InteractionEntity
import vn.dihaver.tech.shhh.confession.feature.home.data.local.entity.PostEntity
import vn.dihaver.tech.shhh.confession.feature.home.data.remote.dto.InteractionStatusDto
import vn.dihaver.tech.shhh.confession.feature.home.data.remote.dto.PostDto
import vn.dihaver.tech.shhh.confession.feature.post.ui.state.PostUiModel

// Dto > Entity
fun PostDto.toEntity(): PostEntity = PostEntity(
    id = id,
    userId = userId,
    schoolId = schoolId,
    postType = postType,
    avatarUrl = avatarUrl,
    displayName = displayName,
    schoolShortName = schoolShortName,
    content = content,
    images = images,
    status = status,
    commentPermission = commentPermission,
    viewPermission = viewPermission,
    totalLike = totalLike,
    totalDislike = totalDislike,
    totalComment = totalComment,
    createdAt = createdAt,
    updatedAt = updatedAt,
    lastUpdated = System.currentTimeMillis()
)

fun InteractionStatusDto.toEntity(postId: String, userId: String): InteractionEntity = InteractionEntity(
    postId = postId,
    userId = userId,
    isLiked = isLiked,
    isDisliked = isDisliked
)

// Entity > Domain
fun PostEntity.toDomain(): Post = Post(
    id = id,
    postType = PostType.fromString(postType),
    author = Author(
        id = userId,
        displayName = displayName,
        avatarUrl = avatarUrl,
        schoolShortName = schoolShortName
    ),
    content = content,
    images = images,
    stats = PostStats(
        likes = totalLike,
        dislikes = totalDislike,
        comments = totalComment
    ),
    createdAt = createdAt.isoToEpochMillis(),
    updatedAt = updatedAt.isoToEpochMillis()
)

fun InteractionEntity.toDomain(): UserInteraction = UserInteraction(
    isLiked = isLiked,
    isDisliked = isDisliked
)

// Domain > UI Model
fun FeedItem.toUiModel(): PostUiModel = PostUiModel(
    id = post.id,
    authorName = post.author.displayName,
    authorAvatarUrl = post.author.avatarUrl,
    schoolName = post.author.schoolShortName,
    content = post.content,
    images = post.images,
    timeAgo = post.createdAt.toRelativeTime(),
    totalLike = post.stats.likes,
    totalDislike = post.stats.dislikes,
    totalComment = post.stats.comments,
    isLiked = userInteraction?.isLiked ?: false,
    isDisliked = userInteraction?.isDisliked ?: false
)