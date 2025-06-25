package vn.dihaver.tech.shhh.confession.feature.post.ui

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import vn.dihaver.tech.shhh.confession.R
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhTextField
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhTopAppBar
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhTheme
import vn.dihaver.tech.shhh.confession.core.util.rememberImagePickerLauncher
import vn.dihaver.tech.shhh.confession.feature.home.ui.MediaPreviewSection
import vn.dihaver.tech.shhh.confession.feature.post.ui.model.CreatePostNavEvent
import vn.dihaver.tech.shhh.confession.feature.post.ui.model.CreatePostUiState
import vn.dihaver.tech.shhh.confession.feature.post.viewmodel.CreatePostViewModel

@Composable
fun CreatePostScreen(
    viewModel: CreatePostViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = viewModel.navEvent) {
        viewModel.navEvent.collect { event ->
            when (event) {
                is CreatePostNavEvent.NavigateBack -> {
                    onBack()
                }
            }
        }
    }

    CreatePostContent(
        uiState = uiState,
        onContentChange = viewModel::onContentChange,
        onMediaSelected = viewModel::onMediaSelected,
        onRemoveMedia = viewModel::onRemoveMedia,
        onPostClicked = viewModel::onPostClicked,
        onBackClicked = viewModel::onBackClicked
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostContent(
    uiState: CreatePostUiState,
    onContentChange: (String) -> Unit,
    onMediaSelected: (List<Uri>) -> Unit,
    onRemoveMedia: (Int) -> Unit,
    onPostClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    val imagePickerLauncher = rememberImagePickerLauncher(maxSelection = 8) { uris ->
        onMediaSelected(uris)
    }

    Scaffold(
        topBar = {
            ShhhTopAppBar(
                navigationIcon = painterResource(R.drawable.svg_phosphor_x),
                title = {
                    Text(
                        "Tạo Nhịp mới",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp)
                    )
                },
                showNavigation = true,
                onNavigationClick = onBackClicked,
                actions = {
                    IconButton(
                        onClick = onPostClicked,
                        enabled = uiState.isPostButtonEnabled,
                        colors = IconButtonDefaults.iconButtonColors()
                            .copy(contentColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.svg_phosphor_fill_paper_plane),
                            contentDescription = "Đăng",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                Modifier.imePadding(),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                    IconButton(
                        onClick = { imagePickerLauncher() },
                        enabled = uiState.selectedMedia.isEmpty()
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.svg_phosphor_image),
                            contentDescription = "Thêm ảnh",
                            tint = if (uiState.selectedMedia.size < 8) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.38f
                            )
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->

        Column(
            Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            ShhhTextField(
                value = uiState.content,
                onValueChange = onContentChange,
                lines = 6,
                hint = uiState.dynamicHint
            )

            if (uiState.selectedMedia.isNotEmpty()) {
                Spacer(modifier = Modifier.height(26.dp))
                MediaPreviewSection(
                    mediaItems = uiState.selectedMedia,
                    onAddMore = { imagePickerLauncher() },
                    onRemoveItem = onRemoveMedia
                )
            }
        }
    }
}

@Preview(name = "Light Mode - Empty", showBackground = true)
@Preview(
    name = "Dark Mode - Empty",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
private fun CreatePostContentEmptyPreview() {
    val previewState = CreatePostUiState(
        dynamicHint = "Bạn đang nghĩ gì thế? 🦉"
    )

    ShhhTheme {
        CreatePostContent(
            uiState = previewState,
            onContentChange = {},
            onMediaSelected = {},
            onRemoveMedia = {},
            onPostClicked = {},
            onBackClicked = {}
        )
    }
}