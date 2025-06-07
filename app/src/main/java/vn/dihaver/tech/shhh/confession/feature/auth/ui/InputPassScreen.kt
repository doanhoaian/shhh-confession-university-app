package vn.dihaver.tech.shhh.confession.feature.auth.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import vn.dihaver.tech.shhh.confession.R
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhButton
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhLoadingDialog
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhTextField
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhTopAppBar
import vn.dihaver.tech.shhh.confession.core.ui.component.fadeClick
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhDimens
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhTheme
import vn.dihaver.tech.shhh.confession.feature.auth.viewmodel.InputPassViewModel

@Preview
@Composable
private fun InputPassScreenPreview() {
    ShhhTheme {
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputPassScreen(
    viewModel: InputPassViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val email = viewModel.email
    val password = viewModel.password
    val isLoading = viewModel.isLoading
    val isError = viewModel.isError
    val errorMessage = viewModel.errorMessage
    val isValidPassword = viewModel.isValidPassword

    val keyboardController = LocalSoftwareKeyboardController.current

    BackHandler(enabled = true, onBack = onBack)

    ShhhLoadingDialog(visible = isLoading, showBackground = true, message = "Đang thực hiện... sẽ nhanh thôi")

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
                text = "Nhập mật khẩu",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(ShhhDimens.SpacerSmall))
            Text(
                text = "Nhập mật khẩu để đăng nhập vào tài khoản $email",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(ShhhDimens.SpacerLarge))
            ShhhTextField(
                value = password,
                onValueChange = { viewModel.onPasswordChange(it) },
                hint = "Mật khẩu",
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
                        viewModel.signIn(onNext)
                    }
                )
            )
            Spacer(Modifier.height(ShhhDimens.SpacerSmall))
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(Modifier.weight(1f, true))
                Text(
                    text = "Quên mật khẩu?",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fadeClick {
                            viewModel.onForgetPassword(onNext)
                        }
                        .padding(ShhhDimens.PaddingSmall, ShhhDimens.PaddingExtraSmall)
                )
            }
            Spacer(Modifier.height(ShhhDimens.SpacerLarge))
            ShhhButton(
                label = stringResource(R.string.action_continue),
                enabled = isValidPassword,
                isLoading = isLoading
            ) {
                viewModel.signIn(onNext)
            }
        }
    }
}