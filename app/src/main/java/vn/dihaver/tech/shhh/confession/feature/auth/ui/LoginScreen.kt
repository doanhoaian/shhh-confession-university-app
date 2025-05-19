package vn.dihaver.tech.shhh.confession.feature.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vn.dihaver.tech.shhh.confession.R
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhButton
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhOutlinedButton
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhTextField
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhDimens
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhTheme

@Preview(
    showSystemUi = true, showBackground = true,
    device = "id:pixel_9_pro_xl"
)
@Composable
private fun LoginScreenPreview() {
    ShhhTheme {
       LoginScreen(onBackClick = {}, onLoginClick = {})
    }
}

@Composable
fun LoginScreen(onBackClick: () -> Unit, onLoginClick: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
        Column(
            modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
        )
        {
            Spacer(Modifier.height(ShhhDimens.SpacerExtraLarge))
            // Box Toolbar
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .height(62.dp)
                    .padding(WindowInsets.systemBars.asPaddingValues()),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),

                ) {
                    // Box btn
                    Box (
                        modifier = Modifier
                            .weight(2.5f)
                            .padding(10.dp)
                            .align(Alignment.CenterVertically)
                    ){
                        ShhhOutlinedButton(
                            label = "",
                            leadingIcon = ImageVector.vectorResource(id = R.drawable.svg_arrow_back),
                            onClick = {
                                onBackClick()
                            }
                        )
                    }
                    // Box text
                    Box (
                        modifier = Modifier
                            .weight(7.5f)
                            .padding(20.dp)
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically)
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
                    .padding(5.dp)
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
                    Spacer(modifier = Modifier.height(16.dp))
                    ShhhTextField(
                        value = username,
                        onValueChange = { username = it },
                        hint = "Username or Email"
                    )
                    Spacer(Modifier.height(ShhhDimens.SpacerExtraLarge))
                    Text(
                        text = "Password",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ShhhTextField(
                        value = password,
                        onValueChange = { password = it },
                        hint = "Username or Email"
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
                        onLoginClick()
                    }
                }
            }

            // Box bottom
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .height(59.dp)
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 10.dp),
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