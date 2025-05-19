package vn.dihaver.tech.shhh.confession.feature.auth.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onBackClick: () -> Unit = {},
    onNextStep: (String, String, String) -> Unit = { _, _, _ -> },
    currentStep: Int = 1
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val roundedShape = RoundedCornerShape(8.dp)
    val backgroundColor = Color(0xFF1E1E2E)
    val progress = remember { Animatable(0f) }

    LaunchedEffect(currentStep) {
        scope.launch {
            progress.animateTo(
                targetValue = currentStep.toFloat() / 5,
                animationSpec = tween(durationMillis = 500)
            )
        }
    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Text("Register")
                            Spacer(modifier = Modifier.weight(1f))
                            Text("Step $currentStep/5", style = MaterialTheme.typography.bodySmall)
                        }
                        Spacer(modifier = Modifier.height(8.dp)) // Thêm khoảng cách từ "Register"
                        // Thanh tiến trình với nền xám và phần tiến trình xanh dương
                        Canvas(modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)) { // Tăng chiều cao lên 6dp
                            // Vẽ nền xám
                            drawLine(
                                color = Color.Gray.copy(alpha = 0.5f),
                                start = androidx.compose.ui.geometry.Offset(0f, 0f),
                                end = androidx.compose.ui.geometry.Offset(size.width, 0f),
                                strokeWidth = 6f
                            )
                            // Vẽ phần tiến trình xanh dương
                            drawLine(
                                color = Color.Blue,
                                start = androidx.compose.ui.geometry.Offset(0f, 0f),
                                end = androidx.compose.ui.geometry.Offset(size.width * progress.value, 0f),
                                strokeWidth = 6f
                            )
                        }
                    }
                },
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text(
                            text = "<-",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {},
                modifier = Modifier.background(backgroundColor)
            )
        },
        modifier = Modifier.background(backgroundColor)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(backgroundColor)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
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

            // Username
            Text(
                text = "Username",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    usernameError = if (it.isBlank()) "Username cannot be empty" else null
                },
                placeholder = { Text("Enter your username") },
                isError = usernameError != null,
                supportingText = { usernameError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                shape = roundedShape
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Email
            Text(
                text = "Email",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = when {
                        it.isBlank() -> "Email cannot be empty"
                        !isValidEmail(it) -> "Invalid email format"
                        else -> null
                    }
                },
                placeholder = { Text("Enter your email") },
                isError = emailError != null,
                supportingText = { emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                shape = roundedShape
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Password
            Text(
                text = "Password",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = if (it.isBlank()) "Password cannot be empty" else null
                },
                placeholder = { Text("Enter your password") },
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordError != null,
                supportingText = { passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                shape = roundedShape
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Confirm Password
            Text(
                text = "Confirm your password",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = when {
                        it.isBlank() -> "Please confirm your password"
                        it != password -> "Passwords do not match"
                        else -> null
                    }
                },
                placeholder = { Text("Confirm your password") },
                visualTransformation = PasswordVisualTransformation(),
                isError = confirmPasswordError != null,
                supportingText = { confirmPasswordError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                shape = roundedShape
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        usernameError = if (username.isBlank()) "Username cannot be empty" else null
                        emailError = when {
                            email.isBlank() -> "Email cannot be empty"
                            !isValidEmail(email) -> "Invalid email format"
                            else -> null
                        }
                        passwordError = if (password.isBlank()) "Password cannot be empty" else null
                        confirmPasswordError = when {
                            confirmPassword.isBlank() -> "Please confirm your password"
                            confirmPassword != password -> "Passwords do not match"
                            else -> null
                        }

                        if (usernameError == null && emailError == null && passwordError == null && confirmPasswordError == null) {
                            isLoading = true
                            scope.launch {
                                try {
                                    onNextStep(username, email, password)
                                    snackbarHostState.showSnackbar("Registered successfully!")
                                } catch (e: Exception) {
                                    snackbarHostState.showSnackbar("Registration failed: ${e.message}")
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    },
                    enabled = !isLoading,
                    shape = roundedShape
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .height(24.dp)
                                .padding(end = 8.dp),
                            strokeWidth = 2.dp
                        )
                    }
                    Text("Next step  >")
                }
            }
        }
    }

    LaunchedEffect(usernameError, emailError, passwordError, confirmPasswordError) {
        val errorMessage = listOfNotNull(
            usernameError,
            emailError,
            passwordError,
            confirmPasswordError
        ).firstOrNull()
        errorMessage?.let {
            scope.launch {
                snackbarHostState.showSnackbar(it)
            }
        }
    }
}