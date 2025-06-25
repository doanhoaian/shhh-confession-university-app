package vn.dihaver.tech.shhh.confession.feature.home.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import vn.dihaver.tech.shhh.confession.R
import vn.dihaver.tech.shhh.confession.core.ui.component.fadeClick
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhTheme
import vn.dihaver.tech.shhh.confession.core.util.ImageUtils
import vn.dihaver.tech.shhh.confession.feature.home.ui.model.PostUiModel
import kotlin.math.roundToInt

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun InteractivePostLayoutPreview() {
    ShhhTheme {
        val allImageUrls = remember {
            listOf(
                "https://i.pinimg.com/736x/6e/25/54/6e25545f2fd65c8caa37160b7206cdde.jpg",
                "https://i.pinimg.com/736x/50/49/be/5049be6b12c93fe7b5bb52207f37d8d8.jpg",
                "https://i.pinimg.com/736x/04/2b/4e/042b4ee5dbbb2c6231a5f3f930df4362.jpg",
                "https://i.pinimg.com/736x/cc/e7/9b/cce79b6e163f2c17ced2e337d72010c9.jpg",
                "https://i.pinimg.com/736x/a2/2d/9b/a22d9bee8d7230f5f6e57454cb300bf0.jpg",
                "https://i.pinimg.com/736x/35/1a/bb/351abbcf764f20fef6427c9ce057adec.jpg",
                "https://i.pinimg.com/736x/f4/c3/a2/f4c3a2749afb582fd84e0fcfaa225f98.jpg",
                "https://i.pinimg.com/736x/18/50/6a/18506a71a8ed8d401170286088d32f1d.jpg"
            )
        }

        var imageCount by remember { mutableFloatStateOf(3f) }

        val displayedImageUrls = allImageUrls.take(imageCount.roundToInt())

        val postUi =
            PostUiModel(
                id = "124",
                authorName = "Bà Ngoại Hiền Hậu",
                authorAvatarUrl = "https://res.cloudinary.com/dwc9ztnjh/image/upload/avatar-elderly-grandma-svgrepo-com_e9r45j.svg",
                schoolName = "UTH",
                content = "Mình mới test thử extension dùng AI để tạo flashcard trực tiếp từ tài liệu PDF, khá tiện. Nhưng có ai đã dùng nó lâu dài chưa? Có rủi ro bảo mật không? Chia sẻ góc nhìn thực tế nhé.",
                images = displayedImageUrls,
                timeAgo = "5 giờ",
                totalLike = 130,
                totalDislike = 12,
                totalComment = 528,
                isLiked = true,
                isDisliked = false,
                isSaved = false
            )


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Số lượng ảnh: ${imageCount.roundToInt()}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                    Slider(
                        value = imageCount,
                        onValueChange = { newValue ->
                            imageCount = newValue
                        },
                        valueRange = 1f..allImageUrls.size.toFloat(),
                        steps = allImageUrls.size - 2
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .animateContentSize()
                ) {
                    PostItem(
                        item = postUi,
                        onMoreClick = {},
                        onImageClick = { _, _ -> },
                        onLikeClick = {}
                    ) { }
                }
            }
        }
    }
}


@Composable
fun ImageLazyRow(
    imageUrls: List<String>,
    desiredHeight: Dp,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 8.dp,
    contentPadding: Dp = 15.dp,
    horizontalSpacing: Dp = 8.dp,
    onImageClick: (Int, String) -> Unit
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
        contentPadding = PaddingValues(horizontal = contentPadding)
    ) {
        itemsIndexed(imageUrls) { index, url ->
            ImageItem(
                url = url,
                index = index,
                desiredHeight = desiredHeight,
                cornerRadius = cornerRadius,
                onImageClick = onImageClick
            )
        }
    }
}

@Composable
private fun ImageItem(
    url: String,
    index: Int,
    desiredHeight: Dp,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 8.dp,
    onImageClick: (Int, String) -> Unit
) {
    val context = LocalContext.current

    val size = ImageUtils.parseImageSize(url)
    val aspectRatio = size?.let { it.first.toFloat() / it.second } ?: 1f

    Box(
        modifier = modifier
            .height(desiredHeight)
            .aspectRatio(aspectRatio)
            .clip(RoundedCornerShape(cornerRadius))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .fadeClick {
                onImageClick(index, url)
            }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(url)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
    }
}

@Composable
fun DeletableMediaItem(
    modifier: Modifier = Modifier,
    onRemove: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier) {
        content()
        IconButton(
            onClick = onRemove,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.4f))
                .size(20.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.svg_phosphor_x),
                contentDescription = "Xóa",
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
fun MediaPreviewSection(
    mediaItems: List<Any>,
    onAddMore: () -> Unit,
    onRemoveItem: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            PostMediaLayout(
                mediaItems = mediaItems,
                paddingHorizontal = 0.dp,
                onImageClick = { _, _ -> /* Xử lý khi click vào ảnh*/ },
                onRemoveItem = onRemoveItem
            )

            Spacer(modifier = Modifier.height(4.dp))

            MediaActionBar(
                count = mediaItems.size,
                onAddMore = onAddMore
            )
        }
    }
}

@Composable
private fun MediaActionBar(
    count: Int,
    onAddMore: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        TextButton(onClick = onAddMore, enabled = count < 10) {
            Text("Thêm ($count/10)", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun PostMediaLayout(
    mediaItems: List<Any>,
    paddingHorizontal: Dp = 15.dp,
    onRemoveItem: ((Int) -> Unit)? = null,
    onImageClick: (index: Int, url: List<Any>) -> Unit
) {
    Box(Modifier.padding(horizontal = paddingHorizontal)) {
        when (mediaItems.size) {
            1 ->
                SinglePostImage(
                    media = mediaItems[0],
                    percentRadius = 6,
                    paddingHorizontal = paddingHorizontal,
                    onClick = { onImageClick(0, mediaItems) },
                    onRemove = onRemoveItem?.let { { it(0) } }
                )

            2 ->
                SideBySideImageLayout(
                    mediaItems = mediaItems,
                    onClick = { index -> onImageClick(index, mediaItems) },
                    onRemoveItem = onRemoveItem
                )

            3 ->
                FeaturedImageWithTwoSmallLayout(
                    mediaItems = mediaItems,
                    onClick = { index -> onImageClick(index, mediaItems) },
                    onRemoveItem = onRemoveItem
                )

            4 ->
                SymmetricalImageGrid(
                    mediaItems = mediaItems,
                    columns = 2,
                    onClick = { index -> onImageClick(index, mediaItems) },
                    onRemoveItem = onRemoveItem
                )

            5 ->
                AsymmetricalFiveImageLayout(
                    mediaItems = mediaItems,
                    onClick = { index -> onImageClick(index, mediaItems) },
                    onRemoveItem = onRemoveItem
                )

            else ->
                ImageGridWithOverflowIndicator(
                    mediaItems = mediaItems,
                    columns = 3,
                    maxVisible = 6,
                    onClick = { index -> onImageClick(index, mediaItems) },
                    onRemoveItem = onRemoveItem
                )
        }
    }
}


@Composable
fun SinglePostImage(
    media: Any,
    paddingHorizontal: Dp = 15.dp,
    maxHeight: Dp = 500.dp,
    percentRadius: Int = 10,
    onClick: () -> Unit,
    onRemove: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    val maxWidth = LocalConfiguration.current.screenWidthDp.dp - (paddingHorizontal * 2)
    val cornerShape = RoundedCornerShape(percent = percentRadius)

    val calculatedHeight = if (media is String && "w=" in media && "h=" in media) {
        val size = ImageUtils.parseImageSize(media)
        val aspectRatio = size?.let {
            it.first.toFloat() / it.second
        } ?: 1f

        remember(aspectRatio, maxWidth) {
            val pxWidth = with(density) { maxWidth.toPx() }
            val pxHeight = pxWidth / aspectRatio
            val dpHeight = with(density) { pxHeight.toDp() }
            dpHeight.coerceAtMost(maxHeight)
        }
    } else {
        (maxWidth * 3f / 4f).coerceAtMost(maxHeight)
    }

    val imageContent = @Composable {
        Box(
            modifier = Modifier
                .fadeClick { onClick() }
                .fillMaxWidth()
                .height(calculatedHeight)
                .clip(cornerShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)

        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(media)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        }
    }

    if (onRemove != null) {
        DeletableMediaItem(onRemove = onRemove) {
            imageContent()
        }
    } else {
        imageContent()
    }
}

@Composable
fun SideBySideImageLayout(
    mediaItems: List<Any>,
    percentRadius: Int = 10,
    spacing: Dp = 8.dp,
    onClick: (Int) -> Unit,
    onRemoveItem: ((Int) -> Unit)? = null
) {
    val context = LocalContext.current
    val cornerShape = RoundedCornerShape(percent = percentRadius)

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(spacing)) {
        mediaItems.forEachIndexed { i, media ->
            val imageContent = @Composable {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(cornerShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .fadeClick { onClick(i) }
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(media)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )
                }
            }

            val modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)

            if (onRemoveItem != null) {
                DeletableMediaItem(modifier = modifier, onRemove = { onRemoveItem(i) }) {
                    imageContent()
                }
            } else {
                Box(modifier = modifier) {
                    imageContent()
                }
            }
        }
    }
}

@Composable
fun FeaturedImageWithTwoSmallLayout(
    mediaItems: List<Any>,
    percentRadius: Int = 10,
    spacing: Dp = 8.dp,
    onClick: (Int) -> Unit,
    onRemoveItem: ((Int) -> Unit)? = null
) {
    val context = LocalContext.current
    val cornerShapeMedium = RoundedCornerShape(percent = (percentRadius / 1.6).toInt())
    val cornerShape = RoundedCornerShape(percent = percentRadius)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        val largeImageModifier = Modifier
            .weight(2f)
            .fillMaxHeight()

        val largeImageContent = @Composable {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(cornerShapeMedium)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .fadeClick { onClick(0) }
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context).data(mediaItems[0]).crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            }
        }

        if (onRemoveItem != null) {
            DeletableMediaItem(modifier = largeImageModifier, onRemove = { onRemoveItem(0) }) {
                largeImageContent()
            }
        } else {
            Box(modifier = largeImageModifier) {
                largeImageContent()
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            listOf(1, 2).forEach { index ->
                val smallImageContent = @Composable {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(cornerShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .fadeClick { onClick(index) }
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context).data(mediaItems[index])
                                .crossfade(true).build(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.matchParentSize()
                        )
                    }
                }
                val modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .aspectRatio(1f)

                if (onRemoveItem != null) {
                    DeletableMediaItem(modifier = modifier, onRemove = { onRemoveItem(index) }) {
                        smallImageContent()
                    }
                } else {
                    Box(modifier = modifier) {
                        smallImageContent()
                    }
                }
            }
        }
    }
}

@Composable
fun SymmetricalImageGrid(
    mediaItems: List<Any>,
    columns: Int = 2,
    percentRadius: Int = 10,
    spacing: Dp = 8.dp,
    onClick: (Int) -> Unit,
    onRemoveItem: ((Int) -> Unit)? = null
) {
    val context = LocalContext.current
    val rows = mediaItems.chunked(columns)
    val cornerShape = RoundedCornerShape(percent = percentRadius)

    Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
        rows.forEachIndexed { rowIndex, rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing),
                modifier = Modifier.fillMaxWidth()
            ) {
                rowItems.forEachIndexed { colIndex, media ->
                    val itemIndex = rowIndex * columns + colIndex
                    val imageContent = @Composable {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(cornerShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .fadeClick { onClick(itemIndex) }
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context).data(media).crossfade(true)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.matchParentSize()
                            )
                        }
                    }

                    val modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)

                    if (onRemoveItem != null) {
                        DeletableMediaItem(
                            modifier = modifier,
                            onRemove = { onRemoveItem(itemIndex) }) {
                            imageContent()
                        }

                    } else {
                        Box(modifier = modifier) {
                            imageContent()
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun AsymmetricalFiveImageLayout(
    mediaItems: List<Any>,
    percentRadius: Int = 10,
    spacing: Dp = 8.dp,
    onClick: (Int) -> Unit,
    onRemoveItem: ((Int) -> Unit)? = null
) {
    val context = LocalContext.current
    val cornerShape = RoundedCornerShape(percent = percentRadius)

    val createDeletableImage: @Composable (Int, Modifier) -> Unit = { index, modifier ->
        val imageContent = @Composable {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(cornerShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .fadeClick { onClick(index) }
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context).data(mediaItems[index]).crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            }
        }
        if (onRemoveItem != null) {
            DeletableMediaItem(modifier = modifier, onRemove = { onRemoveItem(index) }) {
                imageContent()
            }
        } else {
            Box(modifier = modifier) {
                imageContent()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            createDeletableImage(
                0, Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            )
            createDeletableImage(
                1, Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            createDeletableImage(
                2, Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            )
            createDeletableImage(
                3, Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            )
            createDeletableImage(
                4, Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            )
        }
    }
}

@Composable
fun ImageGridWithOverflowIndicator(
    mediaItems: List<Any>,
    columns: Int = 3,
    maxVisible: Int = 6,
    percentRadius: Int = 10,
    spacing: Dp = 8.dp,
    onClick: (Int) -> Unit,
    onRemoveItem: ((Int) -> Unit)? = null
) {
    val context = LocalContext.current
    val visibleMediaItems = mediaItems.take(maxVisible)
    val remainingCount = mediaItems.size - maxVisible
    val rows = visibleMediaItems.chunked(columns)
    val cornerShape = RoundedCornerShape(percent = percentRadius)

    Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
        rows.forEachIndexed { rowIndex, rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing),
                modifier = Modifier.fillMaxWidth()
            ) {
                rowItems.forEachIndexed { colIndex, media ->
                    val itemIndex = rowIndex * columns + colIndex
                    val isLastVisibleItem = itemIndex == maxVisible - 1
                    val hasMoreImages = remainingCount > 0

                    val imageContent = @Composable {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(cornerShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .fadeClick { onClick(itemIndex) },
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context).data(media).crossfade(true)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.matchParentSize()
                            )

                            if (isLastVisibleItem && hasMoreImages) {
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .background(Color.Black.copy(alpha = 0.5f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "+$remainingCount",
                                        color = Color.White,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    val modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                    if (onRemoveItem != null) {
                        DeletableMediaItem(
                            modifier = modifier,
                            onRemove = { onRemoveItem(itemIndex) }) {
                            imageContent()
                        }
                    } else {
                        Box(modifier = modifier) {
                            imageContent()
                        }
                    }
                }
                val emptySpaces = columns - rowItems.size
                if (emptySpaces > 0) {
                    for (i in 1..emptySpaces) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}