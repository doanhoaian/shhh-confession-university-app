package vn.dihaver.tech.shhh.confession.feature.comment.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import vn.dihaver.tech.shhh.confession.R
import vn.dihaver.tech.shhh.confession.core.ui.component.AnimationLoading
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhErrorInternet
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhErrorUnknown
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhTextField
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhTextFieldStyle
import vn.dihaver.tech.shhh.confession.core.ui.component.bounceClick
import vn.dihaver.tech.shhh.confession.core.ui.state.PagingState
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhTheme
import vn.dihaver.tech.shhh.confession.core.util.MockDataProvider
import vn.dihaver.tech.shhh.confession.feature.comment.CommentViewModel
import vn.dihaver.tech.shhh.confession.feature.comment.ui.state.CommentSheetUiState
import vn.dihaver.tech.shhh.confession.feature.comment.ui.state.CommentUiModel
import java.io.IOException


@Composable
fun CommentSheet(
    viewModel: CommentViewModel = hiltViewModel(),
    listState: LazyListState,
) {
    val uiState = viewModel.uiState.collectAsState().value

    CommentSheetContent(uiState, listState)
}

fun Modifier.bottomSheetScrollFix(
    listState: LazyListState,
    scope: CoroutineScope
): Modifier = this.pointerInput(listState, scope) {
    var isListOwningGesture: Boolean? = null

    detectDragGestures(
        onDragStart = { isListOwningGesture = null },
        onDragEnd = { isListOwningGesture = null },
        onDragCancel = { isListOwningGesture = null },
        onDrag = { change, dragAmount ->
            if (isListOwningGesture == null) {
                val isScrollingDown = dragAmount.y > 0
                val isScrollingUp = dragAmount.y < 0
                val isListAtTop = listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0

                isListOwningGesture = isScrollingUp || (isScrollingDown && !isListAtTop)
            }

            if (isListOwningGesture == true) {
                scope.launch {
                    listState.scrollBy(-dragAmount.y)
                }
                change.consume()
            }
        }
    )
}

@Composable
private fun CommentSheetContent(
    uiState: CommentSheetUiState,
    listState: LazyListState = rememberLazyListState()
) {
    val userSession = uiState.userSession
    val commentState = uiState.commentState

    var commentText by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction = .66f)
    ) {
        ShhhHeaderSheet(title = "Bình luận", listState = listState)

        LazyColumn(
            modifier = Modifier.weight(1f).nestedScroll(rememberNestedScrollInteropConnection()),
            state = listState
        ) {
            if (commentState.isInitialLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimationLoading()
                    }
                }
            }

            commentState.initialError?.let { error ->
                item {
                    if (error is IOException) {
                        ShhhErrorInternet(
                            modifier = Modifier.wrapContentHeight(),
                            onRetry = {}
                        )
                    } else {
                        ShhhErrorUnknown(
                            modifier = Modifier.wrapContentHeight(),
                            onRetry = {}
                        )
                    }
                }
            }

            items(items = commentState.items, key = { it.id }) { item ->
                CommentItem(
                    item = item,
                    onExpandReplies = {},
                    onCollapseReplies = {},
                    onLoadMoreReplies = {},
                    onReply = {},
                    onUserClick = {},
                    onReact = { _, _ -> },
                    onShowOptions = {}
                )
            }

            if (commentState.isAppending) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            commentState.appendError?.let {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Lỗi tải thêm bình luận",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                thickness = 1.dp,
                modifier = Modifier.shadow(3.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = userSession.avatarUrl,
                    contentDescription = "Your Avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
                ShhhTextField(
                    modifier = Modifier.weight(1f, true),
                    value = commentText,
                    onValueChange = { commentText = it },
                    hint = "Thêm bình luận...",
                    style = ShhhTextFieldStyle.Transparent,
                    maxLength = 280,
                    lines = 2,
                    autoGrow = true
                )
                AnimatedVisibility(
                    visible = commentText.isNotBlank(),
                    enter = fadeIn() + slideInHorizontally { it / 2 },
                    exit = fadeOut() + slideOutHorizontally { it / 2 }
                ) {
                    Row(
                        Modifier
                            .bounceClick { }
                            .background(
                                MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.large
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.svg_phosphor_arrow_up),
                            contentDescription = "Post",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ShhhHeaderSheet(
    modifier: Modifier = Modifier,
    title: String,
    listState: LazyListState
) {
    val isScrolled = remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
        }
    }

    val dividerAlpha by animateFloatAsState(
        targetValue = if (isScrolled.value) 0.2f else 0f,
        label = "dividerAlpha"
    )

    Column(
        modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Spacer(Modifier.height(10.dp))
        HorizontalDivider(
            modifier = Modifier.alpha(dividerAlpha),
            color = MaterialTheme.colorScheme.outline,
            thickness = 1.dp,
        )
    }
}


@Preview(showBackground = false)
@Composable
private fun CommentSheetContentPreview() {
    var commentState by remember {
        mutableStateOf(
            PagingState(
                items = MockDataProvider.generateMockComments().map {
                    if (it.totalReply > 0) it.copy(canLoadMoreReplies = true) else it
                }
            )
        )
    }

    fun updateReplies(
        commentId: String,
        isLoading: Boolean,
        newReplies: List<CommentUiModel>? = null
    ) {
        val index = commentState.items.indexOfFirst { it.id == commentId }
        if (index != -1) {
            val updatedList = commentState.items.toMutableList()
            val currentComment = updatedList[index]
            updatedList[index] = currentComment.copy(
                isExpanded = true,
                areRepliesLoading = isLoading,
                replies = newReplies ?: currentComment.replies,
                canLoadMoreReplies = if (newReplies != null) newReplies.size < currentComment.totalReply else currentComment.canLoadMoreReplies
            )
            commentState = commentState.copy(items = updatedList)
        }
    }


    ShhhTheme(useDarkTheme = true) {
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = .8f))
        ) {
            Spacer(Modifier.height(280.dp))
            ModalBottomSheetLayoutPreviewWrapper {
                CommentSheetContent(
                    uiState = CommentSheetUiState(
                        userSession = MockDataProvider.getMockUserSession(),
                        commentState = commentState
                    )
                )
            }
        }
        /*commentState = commentState,
            currentUserAvatarUrl = "https://example.com/avatar.png", // URL avatar mẫu
            commentText = text,
            onCommentTextChanged = { text = it },
            onSendComment = {
                // Logic gửi comment ở đây
                println("Gửi bình luận: $text")
                text = ""
            },
            onRetry = {
                println("Thử lại tải bình luận...")
            },
            onLoadMore = {
                println("Tải thêm bình luận...")
            },
            onExpandReplies = { commentId ->
                // Giả lập tải replies
                updateReplies(commentId, true)
                // Giả lập độ trễ mạng
                // coroutineScope.launch { delay(1000); updateReplies(...) }
            },
            onCollapseReplies = { commentId ->
                val index = commentState.items.indexOfFirst { it.id == commentId }
                if (index != -1) {
                    val updatedList = commentState.items.toMutableList()
                    updatedList[index] = updatedList[index].copy(isExpanded = false)
                    commentState = commentState.copy(items = updatedList)
                }
            },
            onLoadMoreReplies = { commentId ->
                println("Tải thêm replies cho: $commentId")
            },
            onReply = {},
            onUserClick = {},
            onReact = { _, _ -> },
            onShowOptions = {}*/
    }
}

@Composable
fun ModalBottomSheetLayoutPreviewWrapper(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        content()
    }
}