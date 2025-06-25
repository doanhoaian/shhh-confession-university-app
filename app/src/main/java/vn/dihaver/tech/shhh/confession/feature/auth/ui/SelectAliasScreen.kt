package vn.dihaver.tech.shhh.confession.feature.auth.ui

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import vn.dihaver.tech.shhh.confession.R
import vn.dihaver.tech.shhh.confession.core.domain.auth.model.Alias
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhButton
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhDialog
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhErrorInternet
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhErrorUnknown
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhTopAppBar
import vn.dihaver.tech.shhh.confession.core.ui.component.fadeClick
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhDimens
import vn.dihaver.tech.shhh.confession.feature.auth.viewmodel.SelectAliasViewModel

@Preview(
    showBackground = true, device = "spec:parent=pixel_5",
    showSystemUi = true
)
@Composable
private fun SelectAliasScreenPreview() {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectAliasScreen(
    viewModel: SelectAliasViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {

    val context = LocalContext.current

    val aliases = viewModel.aliases
    val isLoading = viewModel.isLoading
    val dataLoading = viewModel.dataLoading
    val fetchError = viewModel.fetchError
    val updateError = viewModel.updateError

    var selectedIndex by remember { mutableIntStateOf(-1) }

    LaunchedEffect(Unit) {
        viewModel.fetchAliases(context)
    }

    BackHandler(enabled = true, onBack = {
        onBack()
    })

    updateError?.let { errorMessage ->
        ShhhDialog(
            onDismiss = { viewModel.clearUpdateError() },
            onConfirm = { viewModel.clearUpdateError() },
            confirmText = "Hiểu rồi"
        ) {
            Text(
                text = "Đã xảy ra lỗi",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

    Scaffold(
        topBar = {
            ShhhTopAppBar(showNavigation = true) {
                onBack()
            }
        },
        bottomBar = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.navigationBars.asPaddingValues()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier.padding(
                        horizontal = ShhhDimens.PaddingLarge,
                        vertical = ShhhDimens.PaddingSmall
                    ),
                ) {
                    ShhhButton(
                        label = stringResource(R.string.action_continue),
                        enabled = selectedIndex != -1 && !dataLoading,
                        isLoading = isLoading
                    ) {
                        if (selectedIndex != -1) {
                            viewModel.updateAlias(aliases[selectedIndex]) { onNext() }
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = ShhhDimens.PaddingLarge)
        ) {
            Column {
                Spacer(Modifier.height(ShhhDimens.SpacerLarge))
                Text(
                    text = "Chọn danh tính ẩn danh của bạn",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(ShhhDimens.SpacerSmall))
                Text(
                    text = "Chọn ảnh đại diện và tên hiển thị. Đây là cách người khác sẽ nhìn thấy bạn.",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(ShhhDimens.SpacerMedium))
            }
            when {
                fetchError != null -> {
                    when (fetchError) {
                        SelectAliasViewModel.FetchErrorType.INTERNET -> {
                            ShhhErrorInternet(
                                onRetry = { viewModel.retryFetch(context) }
                            )
                        }

                        SelectAliasViewModel.FetchErrorType.UNKNOWN -> {
                            ShhhErrorUnknown(
                                onRetry = { viewModel.retryFetch(context) }
                            )
                        }
                    }
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(ShhhDimens.SpacerMedium)
                    ) {
                        if (dataLoading) {
                            item(span = { GridItemSpan(3) }) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        } else {
                            items(aliases.size, key = { aliases[it].id }) { index ->
                                AliasItem(
                                    alias = aliases[index],
                                    isSelected = selectedIndex == index,
                                    onClick = { selectedIndex = index }
                                )
                            }
                        }
                        item(span = { GridItemSpan(3) }) {
                            Spacer(Modifier.height(ShhhDimens.SpacerLarge))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AliasItem(
    alias: Alias,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .fadeClick { onClick() }
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent,
                shape = MaterialTheme.shapes.medium
            )
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = ShhhDimens.StrokeMedium,
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.medium
                    )
                } else {
                    Modifier
                }
            )
            .padding(10.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(alias.imageUrl)
                .crossfade(true)
                .placeholder(R.drawable.svg_shhh_loading_square)
                .error(R.drawable.svg_shhh_loading_square)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = alias.displayName,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            minLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}