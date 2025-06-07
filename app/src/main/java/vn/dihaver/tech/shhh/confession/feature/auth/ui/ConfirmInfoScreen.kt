package vn.dihaver.tech.shhh.confession.feature.auth.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import vn.dihaver.tech.shhh.confession.R
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhButton
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhTopAppBar
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhDimens
import vn.dihaver.tech.shhh.confession.core.ui.util.KeyValueText
import vn.dihaver.tech.shhh.confession.feature.auth.viewmodel.ConfirmInfoViewModel


@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun ConfirmInfoScreenPreview() {
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmInfoScreen(
    viewModel: ConfirmInfoViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val user = viewModel.userSession

    val displayName = "${user?.displayName} ${user?.aliasIndex}"
    val email = "${user?.email}"
    val schoolName = "${user?.schoolName}"

    BackHandler(enabled = true, onBack = onBack)

    Scaffold(
        topBar = {
            ShhhTopAppBar(showBack = true) {
                onBack()
            }
        },
        bottomBar = {
            Row(Modifier.padding(WindowInsets.navigationBars.asPaddingValues()).padding(ShhhDimens.PaddingLarge, ShhhDimens.PaddingMedium)) {
                ShhhButton(
                    label = "Bắt đầu khám phá"
                ) {
                    onNext()
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(ShhhDimens.PaddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Chào mừng, $displayName 👋",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(ShhhDimens.SpacerSmall))
            Text(
                "Bây giờ bạn đã sẵn sàng để khám phá và chia sẻ lời confession đầu tiên của mình.",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(ShhhDimens.SpacerExtraLarge))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.medium
                    )
                    .border(
                        width = ShhhDimens.StrokeMedium,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.primary,
                                Color.Transparent
                            )
                        ),
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(5.dp)
            ) {
                Text("Thẻ Sinh Viên Ẩn Danh", modifier = Modifier.fillMaxWidth().padding(10.dp), textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.W500)
                HorizontalDivider(thickness = ShhhDimens.StrokeExtraSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(Modifier.padding(10.dp)) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(user?.avatarUrl)
                            .decoderFactory(SvgDecoder.Factory())
                            .crossfade(true)
                            .placeholder(R.drawable.svg_shhh_loading_square)
                            .error(R.drawable.svg_shhh_loading_square)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                    )
                    Spacer(Modifier.width(15.dp))
                    Column {
                        KeyValueText(key = "Tên", value = displayName)
                        Spacer(Modifier.height(5.dp))
                        KeyValueText(key = "Email", value = email)
                        Spacer(Modifier.height(5.dp))
                        KeyValueText(key = "Trường", value = schoolName)
                    }
                }
            }
        }
    }
}