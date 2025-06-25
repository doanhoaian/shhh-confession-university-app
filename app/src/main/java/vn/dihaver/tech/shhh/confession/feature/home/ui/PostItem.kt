package vn.dihaver.tech.shhh.confession.feature.home.ui

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.valentinilk.shimmer.shimmer
import vn.dihaver.tech.shhh.confession.R
import vn.dihaver.tech.shhh.confession.core.ui.component.InfoActionChip
import vn.dihaver.tech.shhh.confession.core.ui.component.ReactionButtons
import vn.dihaver.tech.shhh.confession.core.ui.component.fadeClick
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhDimens
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhTheme
import vn.dihaver.tech.shhh.confession.core.ui.util.ShhhThemeExtended
import vn.dihaver.tech.shhh.confession.core.util.FormatUtils.formatCountCompact
import vn.dihaver.tech.shhh.confession.feature.home.ui.model.PostUiModel

@Preview(showBackground = true)
@Composable
private fun PostItemPreview() {
    val lazyFeedItems = listOf(
        PostUiModel(
            id = "124",
            authorName = "Chú Bé Chăn Cừu",
            authorAvatarUrl = "",
            schoolName = "UTH",
            content = "Mọi người có bao giờ cảm thấy mình đang cố gắng quá nhiều mà vẫn không đủ không? \uD83D\uDE14 Dạo gần đây mình thấy mình cứ xoay vòng giữa deadline, kỳ vọng và nỗi sợ không làm tốt... Mỗi sáng thức dậy là một chuỗi lo lắng. Có ai giống mình không... \uD83D\uDE25",
            images = listOf(),
            timeAgo = "5 giờ",
            totalLike = 154,
            totalComment = 23,
            totalDislike = 0,
            isLiked = false,
            isDisliked = false,
            isSaved = false
        ),
        PostUiModel(
            id = "768",
            authorName = "Anh Nông Dân Vượt Cạn",
            authorAvatarUrl = "",
            schoolName = "UTH",
            content = "Mình mới test thử extension dùng AI để tạo flashcard trực tiếp từ tài liệu PDF, khá tiện. Nhưng có ai đã dùng nó lâu dài chưa? Có rủi ro bảo mật không? Chia sẻ góc nhìn thực tế nhé.",
            images = listOf(),
            timeAgo = "2 phút",
            totalLike = 89,
            totalDislike = 0,
            totalComment = 12,
            isLiked = false,
            isDisliked = false,
            isSaved = false
        ),
        PostUiModel(
            id = "096",
            authorName = "Mèo Ngủ Ngày",
            authorAvatarUrl = "",
            schoolName = "UTH",
            content = "Ủa rồi cuối cùng là người ấy nói chia tay vì hết yêu, hay vì có người khác? \uD83E\uDD28 Vậy từ khi nào mà bạn bắt đầu thấy người ấy lạnh nhạt? Câu chuyện cụ thể hơn là...?",
            images = listOf(),
            timeAgo = "8 giờ",
            totalLike = 98,
            totalDislike = 0,
            totalComment = 21,
            isLiked = false,
            isDisliked = false,
            isSaved = false
        )
    )

    ShhhTheme() {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(
                count = lazyFeedItems.size,
                key = { index ->
                    lazyFeedItems[index].id
                }
            ) { index ->

                val feedItem = lazyFeedItems[index]

                PostItem(
                    item = feedItem,
                    onMoreClick = {},
                    onImageClick = { _, _ -> },
                    onLikeClick = {},
                    onCommentClick = {}
                )
            }
        }
    }
}

@Composable
fun PostItem(
    item: PostUiModel,
    onMoreClick: (id: String) -> Unit,
    onImageClick: (index: Int, images: List<Any>) -> Unit,
    onLikeClick: (id: String) -> Unit,
    onCommentClick: (id: String) -> Unit
) {
    val context = LocalContext.current

    var isSaved by remember { mutableStateOf(item.isSaved) }

    Column(
        Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(15.dp, 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(item.authorAvatarUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f, true)) {
                Text(
                    item.authorName,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "${item.timeAgo} từ ${item.schoolName}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.W400,
                        fontSize = 11.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(15.dp))
            Icon(
                painter = painterResource(R.drawable.svg_phosphor_dots_three_bold),
                contentDescription = "More",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .size(24.dp)
                    .fadeClick {
                        onMoreClick(item.id)
                    }
            )
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(15.dp, 10.dp)
        ) {
            Text(
                item.content,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.W600
                )
            )
        }

        if (item.images.isNotEmpty()) {
            Spacer(Modifier.height(10.dp))
            PostMediaLayout(
                mediaItems = item.images,
                paddingHorizontal = 15.dp
            ) { index, images ->
                onImageClick(index, images)
            }
        }

        Spacer(Modifier.height(20.dp))

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 15.dp),
            thickness = ShhhDimens.StrokeExtraSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .2f)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ReactionButtons(
                    initialLikeCount = item.totalLike,
                    initialDislikeCount = item.totalDislike,
                    isLikedInitially = item.isLiked,
                    isDislikedInitially = item.isDisliked,
                    likeIconRes = R.drawable.svg_phosphor_hands_clapping,
                    likeFilledIconRes = R.drawable.svg_phosphor_fill_hands_clapping,
                    dislikeIconRes = R.drawable.svg_phosphor_thumbs_down,
                    dislikeFilledIconRes = R.drawable.svg_phosphor_fill_thumbs_down
                )

                Spacer(Modifier.width(8.dp))

                InfoActionChip(
                    iconRes = R.drawable.svg_phosphor_chat_circle,
                    text = item.totalComment.formatCountCompact(context),
                    iconContentDescription = "Comment button",
                    onClick = { onCommentClick(item.id) }
                )

                Spacer(Modifier.weight(1f))

                InfoActionChip(
                    isActivated = isSaved,
                    iconRes = R.drawable.svg_phosphor_bookmark_simple,
                    activatedIconRes = R.drawable.svg_phosphor_fill_bookmark_simple,
                    activatedColor = ShhhThemeExtended.extraColors.warning,
                    activatedBgColor = ShhhThemeExtended.extraColors.warning.copy(alpha = .05f),
                    iconContentDescription = "Bookmark button",
                    onClick = { isSaved = !isSaved }
                )
            }
        }
    }
}

@Composable
fun PostItemSkeleton() {
    Column(
        Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface
            )
            .shimmer()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(15.dp, 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f, true)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(20.dp)
                        .background(Color.Gray, shape = MaterialTheme.shapes.small)
                )
                Spacer(Modifier.height(5.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(16.dp)
                        .background(Color.Gray, shape = MaterialTheme.shapes.small)
                )
            }
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(15.dp, 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color.Gray, shape = MaterialTheme.shapes.small)
            )
        }

        Spacer(Modifier.height(20.dp))
    }
}