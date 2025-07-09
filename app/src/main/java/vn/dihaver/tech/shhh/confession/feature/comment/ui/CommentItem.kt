package vn.dihaver.tech.shhh.confession.feature.comment.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import vn.dihaver.tech.shhh.confession.R
import vn.dihaver.tech.shhh.confession.feature.comment.model.ReactCommentType
import vn.dihaver.tech.shhh.confession.core.ui.component.AnimationLoading
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhSimpleTextWithIcon
import vn.dihaver.tech.shhh.confession.core.ui.component.SimpleReactionButtons
import vn.dihaver.tech.shhh.confession.core.ui.component.fadeClick
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhTheme
import vn.dihaver.tech.shhh.confession.core.util.MockDataProvider
import vn.dihaver.tech.shhh.confession.feature.comment.ui.state.CommentUiModel

@Preview(showSystemUi = false, showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun CommentItemPreview() {
    val coroutineScope = rememberCoroutineScope()

    var commentsState by remember {
        mutableStateOf(
            MockDataProvider.generateMockComments().map { comment ->
                if (comment.totalReply > 0) {
                    comment.copy(canLoadMoreReplies = true)
                } else {
                    comment
                }
            }
        )
    }

    fun updateComment(commentId: String, updateAction: (CommentUiModel) -> CommentUiModel) {
        val index = commentsState.indexOfFirst { it.id == commentId }
        if (index != -1) {
            val updatedList = commentsState.toMutableList()
            updatedList[index] = updateAction(updatedList[index])
            commentsState = updatedList
        }
    }

    ShhhTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            LazyColumn {
                items(items = commentsState, key = { it.id }) { item ->
                    CommentItem(
                        item = item,
                        onCollapseReplies = { commentId ->
                            updateComment(commentId) { it.copy(isExpanded = false) }
                        },
                        onExpandReplies = { commentId ->
                            updateComment(commentId) { it.copy(isExpanded = true) }

                            if (item.replies.isEmpty()) {
                                coroutineScope.launch {
                                    updateComment(commentId) { it.copy(areRepliesLoading = true) }
                                    delay(1000)

                                    val allReplies =
                                        MockDataProvider.generateMockCommentReplies(item.totalReply.toInt())
                                    val firstBatch = allReplies.take(4)

                                    updateComment(commentId) {
                                        it.copy(
                                            areRepliesLoading = false,
                                            replies = firstBatch,
                                            canLoadMoreReplies = allReplies.size > firstBatch.size
                                        )
                                    }
                                }
                            }
                        },
                        onLoadMoreReplies = { commentId ->
                            coroutineScope.launch {
                                val currentItem =
                                    commentsState.find { it.id == commentId } ?: return@launch

                                updateComment(commentId) { it.copy(areRepliesLoading = true) }
                                delay(1500)

                                val existingRepliesCount = currentItem.replies.size

                                val additionalReplies =
                                    MockDataProvider.generateAdditionalMockCommentReplies(
                                        existingCount = existingRepliesCount,
                                        newCount = 5
                                    )

                                val newFullList = currentItem.replies + additionalReplies

                                updateComment(commentId) {
                                    it.copy(
                                        areRepliesLoading = false,
                                        replies = newFullList,
                                        canLoadMoreReplies = newFullList.size < it.totalReply
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CommentItem(
    item: CommentUiModel,
    onExpandReplies: (commentId: String) -> Unit = {},
    onCollapseReplies: (commentId: String) -> Unit = {},
    onLoadMoreReplies: (commentId: String) -> Unit = {},
    onReply: (commentId: String) -> Unit = {},
    onUserClick: (userId: String) -> Unit = {},
    onReact: (commentId: String, reaction: ReactCommentType) -> Unit = { _, _ -> },
    onShowOptions: (commentId: String) -> Unit = {}
) {
    val density = LocalDensity.current

    val contentHeightState = if (!item.isReply && item.totalReply > 0) {
        remember { mutableIntStateOf(0) }
    } else null

    val replyHeightState = if (!item.isReply && item.totalReply > 0) {
        remember { mutableIntStateOf(0) }
    } else null

    val seeMoreHeightState = if (!item.isReply && item.totalReply > 0) {
        remember { mutableIntStateOf(0) }
    } else null

    val startPadding = if (item.isReply) 0.dp else 14.dp
    val avatarSize = if (item.isReply) 28.dp else 34.dp
    val textEdit = if (item.isEdited) " (đã chỉnh sửa)" else ""
    val colorPostOwner =
        if (item.isPostOwner) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(
            alpha = .8f
        )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = startPadding, end = 10.dp, top = 10.dp, bottom = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top
        ) {
            if (!item.isReply) {
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    AsyncImage(
                        model = item.authorAvatarUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(avatarSize)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    if (item.totalReply > 0) {
                        Spacer(Modifier.height(12.dp))
                        Canvas(
                            modifier = Modifier
                                .width(17.dp)
                                .height(with(density) {
                                    ((contentHeightState?.intValue
                                        ?: 0) - (avatarSize.toPx()) - 12.dp.toPx())
                                        .coerceAtLeast(0f)
                                        .toDp()
                                })
                        ) {
                            drawLine(
                                color = Color.Gray.copy(alpha = 0.5f),
                                strokeWidth = 1.dp.toPx(),
                                start = Offset(0f, 0f),
                                end = Offset(0f, size.height)
                            )
                        }
                    }
                }
            } else {
                AsyncImage(
                    model = item.authorAvatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(avatarSize)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.onGloballyPositioned {
                contentHeightState?.intValue = it.size.height
            }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.authorName,
                        style = MaterialTheme.typography.labelMedium,
                        color = colorPostOwner,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = " • ${item.timeAgo}$textEdit",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.height(6.dp))
                if (!isDeleted(item.content)) {
                    Text(
                        text = item.content,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Trả lời",
                            modifier = Modifier
                                .fadeClick {}
                                .padding(vertical = 5.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .8f),
                            fontWeight = FontWeight.W600
                        )
                        Spacer(Modifier.weight(1f, true))

                        SimpleReactionButtons(
                            initialLikeCount = item.totalLikes,
                            initialDislikeCount = item.totalDislikes,
                            isLikedInitially = item.isLiked,
                            isDislikedInitially = item.isDisliked,
                            likeIconRes = R.drawable.svg_phosphor_hands_clapping,
                            likeFilledIconRes = R.drawable.svg_phosphor_fill_hands_clapping,
                            dislikeIconRes = R.drawable.svg_phosphor_thumbs_down,
                            dislikeFilledIconRes = R.drawable.svg_phosphor_fill_thumbs_down
                        )
                    }
                } else {
                    Text(
                        modifier = Modifier.padding(vertical = 2.dp),
                        text = getDeletedMessage(item.content),
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(Modifier.height(6.dp))
                }
            }
        }

        if (item.totalReply > 0 && !item.isReply) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clipToBounds()
            ) {
                Spacer(Modifier.width(17.dp))
                Canvas(
                    modifier = Modifier
                        .width(17.dp)
                        .height(with(density) {
                            ((replyHeightState?.intValue ?: 0) + ((seeMoreHeightState?.intValue
                                ?: 0) / 2) + 4.dp.toPx()).coerceAtLeast(0f)
                                .toDp()
                        })
                ) {
                    val strokeWidth = 1.dp.toPx()
                    val cornerRadius = 12.dp.toPx()
                    val path = Path().apply {
                        moveTo(0f, 0f)
                        lineTo(0f, size.height - cornerRadius)
                        quadraticTo(
                            x1 = 0f,
                            y1 = size.height,
                            x2 = cornerRadius,
                            y2 = size.height
                        )
                        lineTo(size.width, size.height)
                    }
                    drawPath(
                        path = path,
                        color = Color.Gray.copy(alpha = 0.5f),
                        style = Stroke(width = strokeWidth)
                    )
                }
                Spacer(Modifier.width(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    if (item.replies.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .animateContentSize()
                                .onGloballyPositioned {
                                    replyHeightState?.intValue = it.size.height
                                }) {
                            if (item.isExpanded) {
                                item.replies.forEach { reply ->
                                    key(reply.id) {
                                        CommentItem(reply)
                                    }
                                }
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .onGloballyPositioned { seeMoreHeightState?.intValue = it.size.height },
                        contentAlignment = Alignment.CenterStart
                    ) {
                        when {
                            // TRƯỜNG HỢP 1: Đang tải dữ liệu
                            item.areRepliesLoading -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AnimationLoading(modifier = Modifier.size(24.dp))
                                }
                            }

                            // TRƯỜNG HỢP 2: Đã mở rộng và có thể tải thêm
                            item.isExpanded && item.canLoadMoreReplies -> {
                                val remainingCount = item.totalReply - item.replies.size
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    ShhhSimpleTextWithIcon(
                                        text = "Xem thêm $remainingCount câu trả lời",
                                        iconRes = R.drawable.svg_phosphor_caret_down
                                    ) {
                                        onLoadMoreReplies(item.id)
                                    }
                                    ShhhSimpleTextWithIcon(
                                        text = "Thu gọn",
                                        iconRes = R.drawable.svg_phosphor_caret_up
                                    ) {
                                        onCollapseReplies(item.id)
                                    }
                                }
                            }

                            // TRƯỜNG HỢP 3: Đã mở rộng và đã tải hết
                            item.isExpanded && !item.canLoadMoreReplies -> {
                                ShhhSimpleTextWithIcon(
                                    text = "Thu gọn",
                                    iconRes = R.drawable.svg_phosphor_caret_up
                                ) {
                                    onCollapseReplies(item.id)
                                }
                            }

                            // TRƯỜNG HỢP 4 (Mặc định): Đang thu gọn
                            else -> {
                                ShhhSimpleTextWithIcon(
                                    text = "Xem ${item.totalReply} câu trả lời",
                                    iconRes = R.drawable.svg_phosphor_caret_down
                                ) {
                                    onExpandReplies(item.id)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


private fun isDeleted(content: String): Boolean {
    return content.startsWith("@@SYS::vn.dihaver.tech.campus.comments.v1.deleted::")
}

private fun getDeletedMessage(content: String): String {
    val reasonCode = content.substringAfterLast("::", "UNKNOWN")
    return when (reasonCode) {
        "DELETED_BY_OWNER" -> "[Bình luận này đã bị xóa bởi tác giả]"
        "DELETED_BY_MODERATOR" -> "[Bình luận này đã bị xóa bởi quản trị viên]"
        "DELETED_BY_POST_OWNER" -> "[Bình luận này đã bị xóa bởi chủ bài viết]"
        "DELETED_BY_SYSTEM" -> "[Bình luận này đã bị hệ thống xóa]"
        else -> "[Bình luận này đã bị xóa]"
    }
}