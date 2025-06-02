package vn.dihaver.tech.shhh.confession.feature.auth.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composeuisuite.ohteepee.OhTeePeeDefaults
import com.composeuisuite.ohteepee.OhTeePeeInput
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhDialog
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhLoadingDialog
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhTopAppBar
import vn.dihaver.tech.shhh.confession.core.ui.component.fadeClick
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhDimens
import vn.dihaver.tech.shhh.confession.feature.auth.model.AuthContext
import vn.dihaver.tech.shhh.confession.feature.auth.viewmodel.InputOtpViewModel


@Preview
@Composable
private fun InputOtpScreenPreview() {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputOtpScreen(
    viewModel: InputOtpViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val authContext = viewModel.authContext.collectAsState()
    val email = viewModel.email
    val otpValue = viewModel.otpValue
    val isLoading = viewModel.isLoading
    val cooldown = viewModel.cooldown
    val isError = viewModel.isError
    val errorMessage = viewModel.errorMessage
    val showReenterEmail = viewModel.showReenterEmail

    val textTitle = when (authContext.value) {
        is AuthContext.Email.ForgetPassword -> {
            "Đổi mật khẩu"
        }
        else -> {
            "Xác mình Email"
        }
    }

    val defaultCellConfig = OhTeePeeDefaults.cellConfiguration(
        backgroundColor = MaterialTheme.colorScheme.surface,
        borderWidth = 5.dp,
        textStyle = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onSurface)
    )

    BackHandler(enabled = true, onBack = onBack)


    Scaffold(
        topBar = {
            ShhhTopAppBar(showBack = true) {
                onBack()
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
                textTitle,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(ShhhDimens.SpacerSmall))
            Text(
                text = buildAnnotatedString {
                    append("Nhập mã OTP được gửi đến ")

                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append(email)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(ShhhDimens.SpacerLarge))

            OhTeePeeInput(
                value = otpValue,
                onValueChange = { newValue, _ ->
                    viewModel.onOtpValueChange(newValue)

                    val digitCount = newValue.count { it.isDigit() }
                    if (digitCount >= 4) {
                        keyboardController?.hide()
                        viewModel.verifyOtp(onNext)
                    }
                },
                enabled = !isLoading,
                configurations = OhTeePeeDefaults.inputConfiguration(
                    cellsCount = 4,
                    emptyCellConfig = defaultCellConfig.copy(
                        borderColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    filledCellConfig = defaultCellConfig.copy(
                        borderColor = MaterialTheme.colorScheme.onSurface
                    ),
                    activeCellConfig = defaultCellConfig.copy(
                        borderColor = MaterialTheme.colorScheme.primary
                    ),
                    errorCellConfig = defaultCellConfig.copy(
                        borderColor = MaterialTheme.colorScheme.error
                    ),
                    cellModifier = Modifier
                        .weight(1f, true)
                        .padding(horizontal = 8.dp),
                    enableBottomLine = true,
                ),
            )
            AnimatedVisibility(
                visible = isError && !errorMessage.isNullOrBlank(),
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Text(
                    text = errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, start = 12.dp)
                )
            }
            Spacer(Modifier.height(30.dp))
            ResendCodeRow(
                isLoading = isLoading,
                cooldown = cooldown,
                showReenterEmail = showReenterEmail,
                onBack = onBack
            ) { viewModel.sendOtp() }
        }
    }
}

@Composable
private fun ResendCodeRow(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    cooldown: Int,
    showReenterEmail: Boolean,
    onBack: () -> Unit,
    onResend: () -> Unit
) {

    val canResend = cooldown == 0 && !isLoading

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showReenterEmail) {
            Text(
                text = "Nhập lại email",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fadeClick {
                        onBack()
                    }
                    .padding(8.dp)
            )
        } else if (canResend) {
            Text(
                text = "Gửi lại mã",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fadeClick {
                        onResend()
                    }
                    .padding(8.dp)
            )
        } else {
            Text(
                text = if (isLoading) "Đang gửi..." else "Gửi lại sau ${cooldown}s",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(16.dp).weight(1f, true))
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(16.dp),
                strokeWidth = 2.dp
            )
        }
    }
}
