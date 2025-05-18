package vn.dihaver.tech.shhh.confession.feature.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhButton
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhDimens
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhTheme

@Preview(
    showSystemUi = true, showBackground = true,
    device = "id:pixel_9_pro_xl"
)
@Composable
private fun LoginScreenPreview() {
    ShhhTheme {
       LoginScreen()
    }
}

@Composable
fun LoginScreen() {
    var userName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
        )
        {
            // Box Toolbar
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .height(62.dp)
                    .padding(top = 80.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),

                ) {
                    // Box btn
                    Box (
                        modifier = Modifier
                            .weight(2f)
                    ){
                        Button(
                            onClick = { /* điều hướng */ },
                            modifier = Modifier
                                .padding(10.dp)
                        ) {
                            Text("<-")
                        }
                    }

                    // Box text
                    Box (
                        modifier = Modifier
                            .weight(8f)
                            .padding(top = 20.dp)
                    ){
                        Text(
                            text = "Login",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            // Box Content
            Box(
                modifier = Modifier
                    .weight(4f)
                    .fillMaxWidth()
                    .padding(top = 5.dp)
                    .background(MaterialTheme.colorScheme.surface),

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.Start
                    )
                {
                    Text(
                        text = "Welcome to back \uD83D\uDC4B",
                        textAlign = TextAlign.Start,
                        fontSize = 24.sp,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Log in to your anonymous space.",
                        textAlign = TextAlign.Start,
                        fontSize = 15.sp,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(ShhhDimens.SpacerExtraLarge))
                    Text(
                        text = "Username or Email",
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    OutlinedTextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = { Text("Enter your username or email") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(Modifier.height(ShhhDimens.SpacerExtraLarge))
                    Text(
                        text = "Password",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp)),
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(onClick = {
                        //Xu ly khi bam nut

                    }) {
                        Text(
                            text = "Forgot password?",
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }

                    Spacer(Modifier.height(ShhhDimens.SpacerExtraLarge))

                    ShhhButton(label = "Login") {
                        //onLoginClick()
                    }
                }
            }

            // Box bottom
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Text(
                        text = "Don't have an account?",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 2.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    TextButton(onClick = {
                        //Xu ly khi bam nut
                    }) {
                        Text(
                            text = "Register",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }