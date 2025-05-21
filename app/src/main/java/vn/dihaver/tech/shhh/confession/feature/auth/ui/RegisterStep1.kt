package vn.dihaver.tech.shhh.confession.feature.auth.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import vn.dihaver.tech.shhh.confession.R
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhTextField
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhButton
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun RegisterStep1Screen(
    onNextStep: (String, String, String) -> Unit = { _, _, _ -> }
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    var confirmPasswordText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val backgroundColor = Color(0xFF1E1E2E)
    val progress = remember { Animatable(0.2f) }
    val primaryColor = MaterialTheme.colorScheme.primary

    LaunchedEffect(Unit) {
        progress.animateTo(0.2f, animationSpec = tween(durationMillis = 500))
    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(top = 32.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {},
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.svg_back_svgrepo),
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Register",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = "Step 1/5",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .padding(horizontal = 16.dp)
        ) {
            drawLine(
                color = Color.Gray,
                start = Offset(0f, size.height / 2),
                end = Offset(size.width, size.height / 2),
                strokeWidth = size.height
            )
            drawLine(
                color = primaryColor,
                start = Offset(0f, size.height / 2),
                end = Offset(size.width * progress.value, size.height / 2),
                strokeWidth = size.height
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = "Create your account",
                style = MaterialTheme.typography.headlineSmall,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Enter your personal information to get started.",
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Username",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            )
            Spacer(modifier = Modifier.height(6.dp))
            ShhhTextField(
                value = username,
                onValueChange = { newValue ->
                    username = newValue
                    usernameError = if (newValue.isBlank()) "Username cannot be empty" else null
                },
                hint = "Enter your username",
                modifier = Modifier.fillMaxWidth()
            )
            usernameError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Email",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            )
            Spacer(modifier = Modifier.height(6.dp))
            ShhhTextField(
                value = email,
                onValueChange = { newValue ->
                    email = newValue
                    emailError = when {
                        newValue.isBlank() -> "Email cannot be empty"
                        !isValidEmail(newValue) -> "Invalid email format"
                        else -> null
                    }
                },
                hint = "Enter your email",
                modifier = Modifier.fillMaxWidth()
            )
            emailError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Password",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            )
            Spacer(modifier = Modifier.height(6.dp))
            ShhhTextField(
                value = passwordText,
                onValueChange = { passwordText = it },
                hint = "Enter your password",
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = painterResource(id = if (isPasswordVisible) R.drawable.svg_vector_hidden else R.drawable.svg_vector_presently),
                trailingIconContentDescription = if (isPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu",
                onTrailingIconClick = { isPasswordVisible = !isPasswordVisible }
            )
            passwordError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))



            Spacer(modifier = Modifier.height(6.dp))
            ShhhTextField(
                value = confirmPasswordText,
                onValueChange = { confirmPasswordText = it },
                hint = "Confirm your password",
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = painterResource(id = if (isConfirmPasswordVisible) R.drawable.svg_vector_hidden else R.drawable.svg_vector_presently),
                trailingIconContentDescription = if (isConfirmPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu",
                onTrailingIconClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }
            )
            confirmPasswordError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(95.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(end = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                ShhhButton(
                    label = "Next Step  >",
                    isLoading = isLoading,
                    enabled = !isLoading,
                    modifier = Modifier.widthIn(min = 120.dp, max = 160.dp),
                    onClick = {
                        usernameError = if (username.isBlank()) "Username cannot be empty" else null
                        emailError = when {
                            email.isBlank() -> "Email cannot be empty"
                            !isValidEmail(email) -> "Invalid email format"
                            else -> null
                        }
                        passwordError = if (passwordText.isBlank()) "Password cannot be empty" else null
                        confirmPasswordError = when {
                            confirmPasswordText.isBlank() -> "Please confirm your password"
                            confirmPasswordText != passwordText -> "Passwords do not match"
                            else -> null
                        }

                        if (usernameError == null && emailError == null && passwordError == null && confirmPasswordError == null) {
                            isLoading = true
                            scope.launch {
                                try {
                                    onNextStep(username, email, passwordText)
                                    snackbarHostState.showSnackbar("Next step succeeded")
                                } catch (e: Exception) {
                                    snackbarHostState.showSnackbar("Error: ${e.message}")
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    }
                )
            }
        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.CenterHorizontally))
    }

}
