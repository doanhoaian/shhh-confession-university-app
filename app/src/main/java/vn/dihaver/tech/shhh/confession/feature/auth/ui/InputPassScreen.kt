package vn.dihaver.tech.shhh.confession.feature.auth.ui

import android.content.res.Configuration
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import vn.dihaver.tech.shhh.confession.feature.auth.ui.state.InputPassUiState
import vn.dihaver.tech.shhh.confession.feature.auth.viewmodel.InputPassViewModel

@Composable
fun InputPassScreen(
    viewModel: InputPassViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value

    LaunchedEffect(uiState.navigateTo) {
        if (uiState.navigateTo) {
            onNext()
            viewModel.onNavigationHandled()
        }
    }

    BackHandler(enabled = true, onBack = onBack)

    ShhhLoadingDialog(
        visible = uiState.isLoading,
        showBackground = true,
        message = "Đang thực hiện..."
    )

    InputPassContent(
        uiState = uiState,
        onPasswordChange = viewModel::onPasswordChange,
        onSignIn = viewModel::onSignIn,
        onForgetPassword = viewModel::onForgetPassword,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InputPassContent(
    uiState: InputPassUiState,
    onPasswordChange: (newPass: String) -> Unit,
    onSignIn: () -> Unit,
    onForgetPassword: () -> Unit,
    onBack: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            ShhhTopAppBar(showNavigation = true) { onBack() }
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
                text = "Nhập mật khẩu để đăng nhập vào tài khoản ${uiState.email}",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(ShhhDimens.SpacerLarge))
            ShhhTextField(
                value = uiState.password,
                onValueChange = { onPasswordChange(it) },
                hint = "Mật khẩu",
                isError = uiState.error != null,
                enabled = !uiState.isLoading,
                errorMessage = uiState.error,
                isPassword = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        onSignIn()
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
                            onForgetPassword()
                        }
                        .padding(ShhhDimens.PaddingSmall, ShhhDimens.PaddingExtraSmall)
                )
            }
            Spacer(Modifier.height(ShhhDimens.SpacerLarge))
            ShhhButton(
                label = stringResource(R.string.action_continue),
                enabled = uiState.isValidPassword,
                isLoading = uiState.isLoading
            ) {
                onSignIn()
            }
        }
    }
}

@Preview(showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun InputPassContentPreview() {
    InputPassContent(
        uiState = InputPassUiState(email = "example@example.com"),
        onPasswordChange = {},
        onSignIn = {},
        onForgetPassword = {},
        onBack = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun InputPassContentLoadingPreview() {
    InputPassContent(
        uiState = InputPassUiState(
            email = "example@example.com",
            isLoading = true
        ),
        onPasswordChange = {},
        onSignIn = {},
        onForgetPassword = {},
        onBack = {}
    )
}

