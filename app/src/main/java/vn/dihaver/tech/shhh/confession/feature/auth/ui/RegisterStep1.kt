
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import vn.dihaver.tech.shhh.confession.R

@Composable
fun RegisterStep1Screen(
    onNextStep: (String, String, String) -> Unit = { _, _, _ -> }
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
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val roundedShape = RoundedCornerShape(8.dp)
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                isError = passwordError != null,
                supportingText = { passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                shape = roundedShape,
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            painter = painterResource(
                                id = if (showPassword) R.drawable.svg_vector_presently else R.drawable.svg_vector_hidden
                            ),
                            contentDescription = if (showPassword) "Hide password" else "Show password",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Confirm Password",
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
                visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                isError = confirmPasswordError != null,
                supportingText = { confirmPasswordError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                shape = roundedShape,
                trailingIcon = {
                    IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                        Icon(
                            painter = painterResource(
                                id = if (showConfirmPassword) R.drawable.svg_vector_presently else R.drawable.svg_vector_hidden
                            ),
                            contentDescription = if (showConfirmPassword) "Hide password" else "Show password",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

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
                                    snackbarHostState.showSnackbar("Registration successful!")
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
                    Text("Next Step  >")
                }
            }
        }
    }

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier.padding(16.dp)
    )

    LaunchedEffect(usernameError, emailError, passwordError, confirmPasswordError) {
        val errorMessage = listOfNotNull(
            usernameError,
            emailError,
            passwordError,
            confirmPasswordError
        ).firstOrNull()
        if (errorMessage != null) {
            scope.launch {
                snackbarHostState.showSnackbar(errorMessage)
            }
        }
    }
}
