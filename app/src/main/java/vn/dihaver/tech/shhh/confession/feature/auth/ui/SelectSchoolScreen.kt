package vn.dihaver.tech.shhh.confession.feature.auth.ui

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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import vn.dihaver.tech.shhh.confession.R
import vn.dihaver.tech.shhh.confession.core.domain.model.School
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhButton
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhDialog
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhErrorInternet
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhErrorUnknown
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhTextField
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhTopAppBar
import vn.dihaver.tech.shhh.confession.core.ui.component.fadeClick
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhDimens
import vn.dihaver.tech.shhh.confession.core.ui.util.buildHighlightedText
import vn.dihaver.tech.shhh.confession.core.util.FormatUtils.removeVietnameseDiacritics
import vn.dihaver.tech.shhh.confession.feature.auth.viewmodel.SelectSchoolViewModel


@Preview(
    showBackground = true, device = "spec:parent=pixel_5",
    showSystemUi = true
)
@Composable
private fun SelectSchoolScreenPreview() {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectSchoolScreen(
    viewModel: SelectSchoolViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val schools = viewModel.schools
    val isLoading = viewModel.isLoading
    val dataLoading = viewModel.dataLoading
    val fetchError = viewModel.fetchError
    val updateError = viewModel.updateError

    var selectedId by remember { mutableIntStateOf(-1) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredSchools = schools.filter { item ->
        val query = searchQuery.lowercase().trim().removeVietnameseDiacritics()
        item.name.lowercase().removeVietnameseDiacritics().contains(query) ||
                item.shortName.lowercase().removeVietnameseDiacritics().contains(query)
    }

    LaunchedEffect(filteredSchools.size, searchQuery) {
        if (selectedId != -1 && filteredSchools.none { it.id == selectedId }) {
            selectedId = -1
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchSchools(context)
    }

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

    BackHandler(enabled = true, onBack = onBack)

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
                        enabled = selectedId != -1,
                        isLoading = isLoading
                    ) {
                        if (selectedId != -1) {
                            val selectedSchool = filteredSchools.find { it.id == selectedId }
                            if (selectedSchool != null) {
                                viewModel.updateSchool(selectedSchool) { onNext() }
                            }
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
                    text = "Bạn đến từ trường đại học nào?",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.height(ShhhDimens.SpacerSmall))
                Text(
                    text = "Chọn trường của bạn để kết nối với nguồn cấp tin confession phù hợp.",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(ShhhDimens.SpacerMedium))
            }
            when {
                fetchError != null -> {
                    when (fetchError) {
                        SelectSchoolViewModel.FetchErrorType.INTERNET -> {
                            ShhhErrorInternet(
                                onRetry = { viewModel.retryFetch(context) }
                            )
                        }

                        SelectSchoolViewModel.FetchErrorType.UNKNOWN -> {
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
                        item(span = { GridItemSpan(3) }) {
                            ShhhTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                hint = "Tìm kiếm trường của bạn",
                                leadingIcon = Icons.Rounded.Search,
                                enabled = !dataLoading
                            )
                        }
                        if (filteredSchools.isNotEmpty()) {
                            items(
                                items = filteredSchools,
                                key = { it.id }
                            ) { school ->
                                SchoolItem(
                                    school = school,
                                    isSelected = selectedId == school.id,
                                    onClick = { selectedId = school.id },
                                    searchQuery = searchQuery
                                )
                            }
                        } else if (dataLoading) {
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
                            item(span = { GridItemSpan(3) }) {
                                Text(
                                    text = "Không tìm thấy trường nào khớp với tìm kiếm",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(ShhhDimens.PaddingLarge),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
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
private fun SchoolItem(
    school: School,
    isSelected: Boolean,
    onClick: () -> Unit,
    searchQuery: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isSuccess by remember { mutableStateOf(false) }

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
                .data(school.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .then(
                    if (isSuccess) {
                        Modifier
                            .background(Color.White, CircleShape)
                            .padding(10.dp)
                    } else Modifier
                ),
            imageLoader = ImageLoader.Builder(LocalContext.current)
                .placeholder(R.drawable.svg_shhh_loading_square)
                .error(R.drawable.svg_shhh_loading_square)
                .build(),
            onSuccess = { isSuccess = true },
            onError = { isSuccess = false }
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = buildHighlightedText(school.shortName, searchQuery),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(5.dp))
        Text(
            text = buildHighlightedText(school.name, searchQuery),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            minLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}