package vn.dihaver.tech.shhh.confession.feature.auth.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import vn.dihaver.tech.shhh.confession.R
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhButton
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhTextField
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhTopAppBar
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhDimens
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhTheme
import vn.dihaver.tech.shhh.confession.feature.auth.model.AuthContext
import vn.dihaver.tech.shhh.confession.feature.auth.viewmodel.InputEmailViewModel


@Preview(device = "id:pixel_9_pro_xl", showSystemUi = true, showBackground = true)
@Composable
private fun InputEmailScreenPreview() {
    ShhhTheme {

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputEmailScreen(
    viewModel: InputEmailViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val authContext = viewModel.authContext.collectAsState()
    val email = viewModel.email
    val isLoading = viewModel.isLoading
    val isError = viewModel.isError
    val errorMessage = viewModel.errorMessage
    val isValidEmail = viewModel.isValidEmail

    val textSubtitle = when (authContext.value) {
        is AuthContext.Email.ForgetPassword -> {
            "Để khôi phục mật khẩu cho tài khoản"
        }
        is AuthContext.Email.None, AuthContext.Email.Register, AuthContext.Email.Login -> {
            "Để đăng ký tài khoản mới hoặc đăng nhập"
        }
        else -> {
            "Để đăng ký tài khoản mới hoặc đăng nhập"
        }
    }

    BackHandler(enabled = true, onBack = onBack)

    Scaffold(
        topBar = {
            ShhhTopAppBar(showNavigation = true) {
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
                "Nhập Email của bạn",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(ShhhDimens.SpacerSmall))
            Text(
                text = textSubtitle,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(ShhhDimens.SpacerLarge))
            ShhhTextField(
                value = email,
                onValueChange = { viewModel.onEmailChange(it) },
                enabled = !isLoading,
                hint = stringResource(R.string.label_email),
                isError = isError,
                errorMessage = errorMessage
            )
            Spacer(Modifier.height(ShhhDimens.SpacerExtraLarge))
            ShhhButton(
                label = stringResource(R.string.action_continue),
                enabled = isValidEmail,
                isLoading = isLoading
            ) {
                viewModel.verifyEmail { onNext() }
            }
        }
    }
}