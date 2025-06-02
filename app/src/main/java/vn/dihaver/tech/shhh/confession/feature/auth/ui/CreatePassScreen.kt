package vn.dihaver.tech.shhh.confession.feature.auth.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vn.dihaver.tech.shhh.confession.R
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhButton
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhDialog
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhLoadingDialog
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhTextField
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhTopAppBar
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhDimens
import vn.dihaver.tech.shhh.confession.core.ui.util.ShhhThemeExtended
import vn.dihaver.tech.shhh.confession.feature.auth.model.AuthContext
import vn.dihaver.tech.shhh.confession.feature.auth.viewmodel.CreatePassViewModel

@Preview
@Composable
private fun CreatePassScreenPreview() {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePassScreen(
    viewModel: CreatePassViewModel,
    onRestPass: () -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val authContext by viewModel.authContext.collectAsState()
    val email = viewModel.email
    val password = viewModel.password
    val isLoading = viewModel.isLoading
    val isError = viewModel.isError
    val errorMessage = viewModel.errorMessage
    val isValidPassword = viewModel.isValidPassword
    val hasMinLength = viewModel.hasMinLength
    val hasRequiredChars = viewModel.hasRequiredChars

    val keyboardController = LocalSoftwareKeyboardController.current

    var isShowConfirmReset by remember { mutableStateOf(false) }

    val textTitle = when (authContext) {
        is AuthContext.Email.ForgetPassword -> "Đặt lại mật khẩu"
        else -> "Đặt mật khẩu"
    }

    BackHandler(enabled = true, onBack = onBack)

    if (isShowConfirmReset) {
        ShhhDialog(
            onDismiss = onRestPass,
            onConfirm = onRestPass,
            confirmText = "Đăng nhập"
        ) {
            Text(
                text = "Đổi mật khẩu thành công",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Mật khẩu của bạn đã được cập nhật. Vui lòng đăng nhập lại",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

    ShhhLoadingDialog(
        visible = isLoading,
        showBackground = true,
        message = "Đang thực hiện... sẽ nhanh thôi"
    )

    Scaffold(
        topBar = {
            ShhhTopAppBar(showBack = true) { onBack() }
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
                text = textTitle,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(ShhhDimens.SpacerSmall))
            Text(
                text = "Hãy tạo một mật khẩu mạnh cho tài khoản $email",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(ShhhDimens.SpacerLarge))

            ShhhTextField(
                value = password,
                onValueChange = { viewModel.onPasswordChange(it) },
                hint = "Mật khẩu mới",
                isError = isError,
                enabled = !isLoading,
                errorMessage = errorMessage,
                isPassword = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        viewModel.submitPassword(
                            onNext = onNext,
                            onSuccessForgetPassword = { isShowConfirmReset = true })
                    }
                )
            )

            Spacer(Modifier.height(ShhhDimens.SpacerSmall))

            Column(modifier = Modifier.fillMaxWidth()) {
                PasswordRequirementItem("Độ dài 8-20 ký tự", hasMinLength)
                PasswordRequirementItem(
                    "Chứa 1 chữ hoa, 1 số, 1 ký tự (ví dụ: @#&*)",
                    hasRequiredChars
                )
            }

            Spacer(Modifier.height(ShhhDimens.SpacerLarge))

            ShhhButton(
                label = stringResource(R.string.action_continue),
                enabled = isValidPassword,
                isLoading = isLoading
            ) {
                viewModel.submitPassword(
                    onNext = onNext,
                    onSuccessForgetPassword = {
                        isShowConfirmReset = true
                    })
            }
        }
    }
}

@Composable
fun PasswordRequirementItem(text: String, met: Boolean) {
    val iconRes = if (met) R.drawable.svg_font_check_w500 else R.drawable.svg_sanity_dot
    val colorTint =
        if (met) ShhhThemeExtended.extraColors.success else MaterialTheme.colorScheme.onSurfaceVariant
    val colorText =
        if (met) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Icon(
            painter = painterResource(iconRes),
            modifier = Modifier.size(ShhhDimens.IconSizeSmall),
            tint = colorTint,
            contentDescription = null
        )
        Spacer(Modifier.width(2.dp))
        Text(
            text = text,
            color = colorText,
            style = MaterialTheme.typography.bodySmall
        )
    }
}