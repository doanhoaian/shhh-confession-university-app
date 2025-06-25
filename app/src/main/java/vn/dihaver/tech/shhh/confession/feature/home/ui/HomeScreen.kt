package vn.dihaver.tech.shhh.confession.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.flaringapp.compose.topbar.nestedcollapse.CollapsingTopBarColumn
import com.flaringapp.compose.topbar.scaffold.CollapsingTopBarScaffold
import com.flaringapp.compose.topbar.scaffold.CollapsingTopBarScaffoldScrollMode
import com.flaringapp.compose.topbar.scaffold.rememberCollapsingTopBarScaffoldState
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import kotlinx.coroutines.launch
import vn.dihaver.tech.shhh.confession.R
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhErrorInternet
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhErrorUnknown
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhTopAppBar
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhTheme
import vn.dihaver.tech.shhh.confession.feature.home.HomeViewModel
import java.io.IOException
import java.util.UUID

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun HomeScreenPreview() {
    ShhhTheme {
        HomeScreen(onCreate = {})
    }
}

data class Topic(
    val value: String,
    val name: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onCreate: () -> Unit
) {
    val lazyFeedItems = viewModel.feedPagingFlow.collectAsLazyPagingItems()
    val selectedTopicValue = viewModel.selectedTopic.collectAsState().value
    val userSession = viewModel.userSession.collectAsState().value

    val topics = remember {
        listOf(
            Topic("all", "Tất cả"),
            Topic("tam-su", "Tâm sự"),
            Topic("tinh-yeu", "Tình yêu"),
            Topic("hai-huoc-meme", "Hài hước & memes"),
            Topic("chuyen-truong-lop", "Chuyện trường lớp"),
            Topic("goc-hoc-tap", "Góc học tập"),
            Topic("review-mon-hoc", "Review môn học"),
            Topic("thuc-tap-viec-lam", "Thực tập & việc làm"),
            Topic("nha-tro-pass-do", "Nhà trọ & pass đồ"),
            Topic("tim-do-hoi-dap", "Tìm đồ & hỏi đáp"),
            Topic("su-kien", "Sự kiện"),
            Topic("chuyen-linh-tinh", "Chuyện linh tinh")
        )
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val collapseState = rememberCollapsingTopBarScaffoldState()
    val listState = rememberLazyListState()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface
            ) {
                Text("Menu 1")
                Text("Menu 2")
            }
        },
        gesturesEnabled = true,
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

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(Float.MAX_VALUE)
                    .height(WindowInsets.navigationBars.asPaddingValues().calculateTopPadding())
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = .8f))
                    .align(Alignment.BottomCenter)
            )

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
                                        .data(userSession?.avatarUrl)
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

                        var collapseProgress by remember {
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
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(.4f))
                    }
                }
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {

                    item {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface)
                                .clickable {
                                    onCreate()
                                }
                                .padding(15.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(userSession?.avatarUrl)
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
                                    text = userSession?.displayName + if (userSession?.aliasIndex != 1) {
                                        " #${userSession?.aliasIndex}"
                                    } else "",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(Modifier.height(10.dp))
                                Text(
                                    viewModel.dynamiteHint,
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

                    val refreshState = lazyFeedItems.loadState.refresh
                    if (refreshState is LoadState.Error && lazyFeedItems.itemCount == 0) {
                        item {
                            val error = refreshState.error
                            if (error is IOException) {
                                ShhhErrorInternet(
                                    modifier = Modifier.fillParentMaxSize(),
                                    onRetry = { lazyFeedItems.retry() }
                                )
                            } else {
                                ShhhErrorUnknown(
                                    modifier = Modifier.fillParentMaxSize(),
                                    onRetry = { lazyFeedItems.retry() }
                                )
                            }
                        }
                    }

                    if (refreshState is LoadState.Loading && lazyFeedItems.itemCount == 0) {
                        items(5) {
                            PostItemSkeleton()
                        }
                    }

                    if (refreshState is LoadState.NotLoading || lazyFeedItems.itemCount > 0) {
                        items(
                            count = lazyFeedItems.itemCount,
                            key = { index ->
                                lazyFeedItems.peek(index)?.id ?: UUID.randomUUID()
                            }
                        ) { index ->
                            val feedItem = lazyFeedItems[index]
                            if (feedItem != null) {
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

                    item {
                        when (lazyFeedItems.loadState.append) {
                            is LoadState.Loading -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                                }
                            }

                            is LoadState.Error -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        "Đã xảy ra lỗi",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                            else -> {}
                        }
                    }

                    item {
                        Spacer(
                            Modifier
                                .height(120.dp)
                                .padding(WindowInsets.navigationBars.asPaddingValues())
                        )
                    }
                }
            }
        }
    }
}