package vn.dihaver.tech.shhh.confession.feature.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vn.dihaver.tech.shhh.confession.R
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhButton
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhDialog
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhLoadingDialog
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhOutlinedButton
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhDimens
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhTheme
import vn.dihaver.tech.shhh.confession.feature.auth.model.TypeLogin
import vn.dihaver.tech.shhh.confession.feature.auth.viewmodel.StartViewModel

@Preview(
    showSystemUi = true, showBackground = true,
    device = "spec:width=411dp,height=891dp"
)
@Composable
private fun StartScreenPreview() {
    ShhhTheme {

    }
}

@Composable
fun StartScreen(
    viewModel: StartViewModel,
    onNext: () -> Unit
) {

    val context = LocalContext.current

    val isLoading by viewModel.isLoading.collectAsState()
    val resultSuccess by viewModel.resultSuccess.collectAsState()
    val resultError by viewModel.resultError.collectAsState()

    ShhhLoadingDialog(visible = isLoading, showBackground = true, message = "Đang thực hiện...")

    resultSuccess?.let { type ->
        if (type == TypeLogin.SIGN_IN_WITH_GOOGLE) {
            onNext()
        }
    }

    resultError?.let { error ->
        ShhhDialog(
            onDismiss = { viewModel.stateIdle() },
            onConfirm = { viewModel.stateIdle() },
            confirmText = "Ok"
        ) {
            Text(
                "Đã xảy ra lỗi",
                style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.error)
            )
            Spacer(Modifier.height(12.dp))
            Text(error, style = MaterialTheme.typography.bodyMedium)
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        Image(
            painter = painterResource(R.drawable.svg_decore_start),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.surface
                        ),
                        startY = 0f,
                        endY = 500f
                    )
                )
                .padding(40.dp)
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Chia sẻ Nhịp đập của bạn",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(ShhhDimens.SpacerMedium))

            Text(
                text = "Một câu hỏi, một khoảnh khắc vui, hay một ý tưởng chợt loé lên... Hãy góp phần tạo nên sức sống cho cộng đồng C.A.M.P.U.S",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(ShhhDimens.SpacerExtraLarge))

            ShhhButton(label = "Sử dụng Email", leadingIcon = R.drawable.svg_font_fill_mail) {
                viewModel.onEmailMethod { onNext() }
            }

            Spacer(Modifier.height(ShhhDimens.SpacerMedium))

            ShhhOutlinedButton(
                label = "Tiếp tục với Google",
                isTintLeading = false,
                leadingIcon = com.google.android.gms.auth.api.R.drawable.googleg_standard_color_18
            ) {
                viewModel.onGoogleMethod(context)
            }

            Spacer(Modifier.height(ShhhDimens.SpacerMedium))
        }
    }
}