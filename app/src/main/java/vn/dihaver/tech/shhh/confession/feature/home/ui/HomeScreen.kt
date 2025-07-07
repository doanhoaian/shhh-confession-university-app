package vn.dihaver.tech.shhh.confession.feature.home.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.flaringapp.compose.topbar.nestedcollapse.CollapsingTopBarColumn
import com.flaringapp.compose.topbar.scaffold.CollapsingTopBarScaffold
import com.flaringapp.compose.topbar.scaffold.CollapsingTopBarScaffoldScrollMode
import com.flaringapp.compose.topbar.scaffold.rememberCollapsingTopBarScaffoldState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import vn.dihaver.tech.shhh.confession.R
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhErrorInternet
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhErrorUnknown
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhPullToRefresh
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhTopAppBar
import vn.dihaver.tech.shhh.confession.core.ui.component.fadeClick
import vn.dihaver.tech.shhh.confession.core.ui.component.refreshEffect
import vn.dihaver.tech.shhh.confession.core.ui.state.PagingState
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhTheme
import vn.dihaver.tech.shhh.confession.core.util.MockDataProvider
import vn.dihaver.tech.shhh.confession.feature.comment.ui.CommentSheet
import vn.dihaver.tech.shhh.confession.feature.comment.ui.bottomSheetScrollFix
import vn.dihaver.tech.shhh.confession.feature.home.HomeViewModel
import vn.dihaver.tech.shhh.confession.feature.home.ui.state.HomeUiState
import vn.dihaver.tech.shhh.confession.feature.post.ui.PostItem
import vn.dihaver.tech.shhh.confession.feature.post.ui.PostItemSkeleton
import java.io.IOException
import java.text.DecimalFormat

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onCreate: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var uiState by remember {
        mutableStateOf(
            HomeUiState(
                userSession = MockDataProvider.getMockUserSession(),
                feedState = PagingState(
                    items = MockDataProvider.generateMockPosts(15)
                ),
                dynamiteHint = "Kéo xuống để xem điều kỳ diệu ✨"
            )
        )
    }

    val onRefresh = {
        scope.launch {
            uiState = uiState.copy(
                feedState = uiState.feedState.copy(isPullRefreshing = true)
            )

            delay(2000)

            val newPosts = MockDataProvider.generateMockPosts(15)
            uiState = uiState.copy(
                feedState = uiState.feedState.copy(
                    items = newPosts,
                    isPullRefreshing = false
                ),
                dynamiteHint = "Đã làm mới với dữ liệu mới!"
            )
        }
    }

    val onLoadMore = {
        scope.launch {
            uiState = uiState.copy(
                feedState = uiState.feedState.copy(isAppending = true)
            )
            delay(1200)

            val additionalPosts = MockDataProvider.generateAdditionalMockPosts(
                existingCount = uiState.feedState.items.size,
                count = 10
            )

            uiState = uiState.copy(
                feedState = uiState.feedState.copy(
                    items = uiState.feedState.items + additionalPosts,
                    isAppending = false
                ),
                dynamiteHint = "Đã tải thêm dữ liệu!"
            )
        }
    }

    HomeContent(
        uiState = uiState,
        onLogout = { },
        onCreate = onCreate,
        onRefresh = { onRefresh() },
        onRetry = { },
        onLoadMore = { onLoadMore() }
    )
}

private const val TAG = "AAA-CommentSheet"


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    uiState: HomeUiState,
    onLogout: () -> Unit,
    onCreate: () -> Unit,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    val userSession = uiState.userSession
    val feedState = uiState.feedState

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val collapseState = rememberCollapsingTopBarScaffoldState()
    val listState = rememberLazyListState()

    var showCommentSheet by remember { mutableStateOf(false) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { info ->
                val lastVisibleIndex = info.visibleItemsInfo.lastOrNull()?.index ?: return@collect
                val totalItems = info.totalItemsCount

                if (totalItems > 0 && lastVisibleIndex >= totalItems - 2) {
                    if (!feedState.isAppending) {
                        onLoadMore()
                    }
                }
            }
    }

    if (showCommentSheet) {
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
            confirmValueChange = { it != SheetValue.Hidden })
        val lazyState = rememberLazyListState()

//        val nestedScrollConnection = remember {
//            object : NestedScrollConnection {
//            }
//        }
        ModalBottomSheet(
            modifier = Modifier.bottomSheetScrollFix(lazyState, scope),
            sheetState = sheetState,
            onDismissRequest = { showCommentSheet = false },
            containerColor = MaterialTheme.colorScheme.surface,
            contentWindowInsets = { WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom) },
            dragHandle = {
                Column {
                    Spacer(Modifier.height(12.dp))
                    Box(
                        Modifier
                            .height(2.dp)
                            .width(28.dp)
                            .background(MaterialTheme.colorScheme.outline)
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        ) {
            CommentSheet(listState = lazyState)
        }
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface
            ) {
                Text("Menu 1")
                Text("Menu 2")
                Text(
                    "Logout",
                    modifier = Modifier
                        .fadeClick {
                            onLogout()
                            /*val intent = Intent(context, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            context.startActivity(intent)*/
                        }
                        .padding(15.dp, 10.dp)
                )
            }
        },
        drawerState = drawerState
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(Float.MAX_VALUE)
                    .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = .8f))
                    .align(Alignment.TopCenter)
            )

            /* Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(Float.MAX_VALUE)
                    .height(WindowInsets.navigationBars.asPaddingValues().calculateTopPadding())
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = .8f))
                    .align(Alignment.BottomCenter)
            )*/

            ShhhPullToRefresh(
                isRefreshing = feedState.isPullRefreshing,
                onRefresh = onRefresh,
                indicatorPaddingTop = with(density) {
                    collapseState.topBarState.layoutInfo.height.toDp()
                }
            ) { cardOffset, cardRotation ->
                CollapsingTopBarScaffold(
                    state = collapseState,
                    scrollMode = CollapsingTopBarScaffoldScrollMode.collapse(expandAlways = true),
                    topBarModifier = Modifier
                        .background(MaterialTheme.colorScheme.surface),
                    topBar = { topBarState ->
                        CollapsingTopBarColumn(topBarState) {
                            ShhhTopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                                title = {
                                    Text(
                                        text = stringResource(R.string.app_name),
                                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp)
                                    )
                                },
                                actions = {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(userSession.avatarUrl)
                                            .crossfade(true)
                                            .placeholder(R.drawable.svg_shhh_loading_square)
                                            .error(R.drawable.svg_shhh_loading_square)
                                            .build(),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(35.dp)
                                            .clip(CircleShape)
                                    )
                                    Spacer(Modifier.width(15.dp))
                                },
                                showNavigation = true,
                                navigationIcon = painterResource(R.drawable.svg_font_menu),
                                onNavigationClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }
                            )

                            /*var collapseProgress by remember {
                            mutableFloatStateOf(1f)
                        }

                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 5.dp)
                                .background(MaterialTheme.colorScheme.surface)
                                .columnProgress { _, itemProgress ->
                                    collapseProgress = itemProgress
                                }
                                .graphicsLayer {
                                    alpha = collapseProgress
                                },
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            items(topics) { topic ->
                                FilterChip(
                                    selected = topic.value == selectedTopicValue,
                                    onClick = {
                                        viewModel.onCategorySelected(topic.value)
                                    },
                                    label = { Text(topic.name) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        borderColor = Color.Transparent,
                                        selectedBorderColor = Color.Transparent,
                                        enabled = true,
                                        selected = true
                                    )
                                )
                            }
                        }*/
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outlineVariant.copy(
                                    .4f
                                )
                            )
                        }
                    }
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {

                        item {
                            Box(
                                Modifier.refreshEffect(
                                    index = 0,
                                    total = feedState.items.size + 1,
                                    cardRotation = cardRotation,
                                    cardOffset = cardOffset
                                )
                            ) {
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .shadow(elevation = 1.dp)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .clickable {
                                            onCreate()
                                        }
                                        .padding(15.dp)
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(userSession.avatarUrl)
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
                                            text = userSession.displayName + if (userSession.aliasIndex != 1) {
                                                " #${userSession.aliasIndex}"
                                            } else "",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(Modifier.height(10.dp))
                                        Text(
                                            uiState.dynamiteHint,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                                    alpha = .8f
                                                )
                                            ),
                                            maxLines = 3,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }

                        feedState.initialError?.let { error ->
                            item {
                                if (error is IOException) {
                                    ShhhErrorInternet(
                                        modifier = Modifier.wrapContentHeight(),
                                        onRetry = onRetry
                                    )
                                } else {
                                    ShhhErrorUnknown(
                                        modifier = Modifier.wrapContentHeight(),
                                        onRetry = onRetry
                                    )
                                }
                            }
                        }

                        if (feedState.isInitialLoading) {
                            items(5) {
                                PostItemSkeleton()
                            }
                        }

                        if (!feedState.isInitialLoading && feedState.initialError == null) {
                            itemsIndexed(
                                items = feedState.items
                            ) { index, item ->
                                Box(
                                    modifier = Modifier
                                        .refreshEffect(
                                            index = index + 1,
                                            total = feedState.items.size + 1,
                                            cardRotation = cardRotation,
                                            cardOffset = cardOffset
                                        ),
                                ) {
                                    PostItem(
                                        item = item,
                                        onMoreClick = {},
                                        onImageClick = { _, _ -> },
                                        onLikeClick = {},
                                        onCommentClick = { showCommentSheet = true }
                                    )
                                }
                            }
                        }

                        if (feedState.isAppending) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                                }
                            }
                        }

                        feedState.appendError?.let {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        "Đã xảy ra lỗi khi tải thêm",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }

                        item {
                            Spacer(
                                Modifier
                                    .height(40.dp)
                                    .padding(WindowInsets.navigationBars.asPaddingValues())
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Interactive Refresh Preview")
@Composable
fun HomeContentInteractivePreview() {
    val scope = rememberCoroutineScope()

    var uiState by remember {
        mutableStateOf(
            HomeUiState(
                userSession = MockDataProvider.getMockUserSession(),
                feedState = PagingState(
                    items = MockDataProvider.generateMockPosts(15)
                ),
                dynamiteHint = "Kéo xuống để xem điều kỳ diệu ✨"
            )
        )
    }

    val onRefresh = {
        scope.launch {
            uiState = uiState.copy(
                feedState = uiState.feedState.copy(isPullRefreshing = true)
            )

            delay(2000)

            val newPosts = MockDataProvider.generateMockPosts(15)
            uiState = uiState.copy(
                feedState = uiState.feedState.copy(
                    items = newPosts,
                    isPullRefreshing = false
                ),
                dynamiteHint = "Đã làm mới với dữ liệu mới!"
            )
        }
    }

    val onLoadMore = {
        scope.launch {
            uiState = uiState.copy(
                feedState = uiState.feedState.copy(isAppending = true)
            )
            delay(1200)

            val additionalPosts = MockDataProvider.generateAdditionalMockPosts(
                existingCount = uiState.feedState.items.size,
                count = 10
            )

            uiState = uiState.copy(
                feedState = uiState.feedState.copy(
                    items = uiState.feedState.items + additionalPosts,
                    isAppending = false
                ),
                dynamiteHint = "Đã tải thêm dữ liệu!"
            )
        }
    }

    ShhhTheme {
        HomeContent(
            uiState = uiState,
            onLogout = {},
            onCreate = {},
            onRefresh = { onRefresh() },
            onRetry = {},
            onLoadMore = { onLoadMore() }
        )
    }
}


/*
@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Initial Loading Skeleton Preview"
)
@Composable
fun HomeContentSkeletonPreview() {
    val scope = rememberCoroutineScope()

    var uiState by remember {
        mutableStateOf(
            HomeUiState(
                userSession = MockDataProvider.getMockUserSession(),
                feedState = PagingState(
                    items = emptyList(),
                    isInitialLoading = true
                ),
                dynamiteHint = "Đang tải dữ liệu..."
            )
        )
    }

    LaunchedEffect(Unit) {
        scope.launch {
            delay(1500)
            val loadedPosts = MockDataProvider.generateMockPosts(15)
            uiState = uiState.copy(
                feedState = uiState.feedState.copy(
                    items = loadedPosts,
                    isInitialLoading = false,
                    initialError = Exception()
                ),
                dynamiteHint = "Dữ liệu đã sẵn sàng!"
            )
        }
    }

    ShhhTheme {
        HomeContent(
            uiState = uiState,
            onLogout = {},
            onCreate = {},
            onRefresh = {},
            onRetry = {},
            onLoadMore = {}
        )
    }
}*/
