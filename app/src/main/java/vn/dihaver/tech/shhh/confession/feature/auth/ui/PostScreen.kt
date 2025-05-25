@file:OptIn(ExperimentalMaterial3Api::class)

package vn.dihaver.tech.shhh.confession.feature.create

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vn.dihaver.tech.shhh.confession.R
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhTextField

@Composable
fun CreatePostScreen(
    modifier: Modifier = Modifier,
    onSubmit: (String) -> Unit = {},
    isSubmitting: Boolean = false,
    onBack: () -> Unit = {}
) {
    var text by remember { mutableStateOf("") }
    var selectedVisibility by remember { mutableStateOf("Only my school") }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create Post",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Button(
                        onClick = { if (text.isNotBlank()) onSubmit(text) },
                        enabled = text.isNotBlank() && !isSubmitting,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5E5CE6),
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFF5E5CE6).copy(alpha = 0.4f),
                            disabledContentColor = Color.White.copy(alpha = 0.4f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.defaultMinSize(minHeight = 36.dp)
                    ) {
                        Text("Post", fontSize = 13.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = " \uD83D\uDCAD What’s on your mind?",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
            )

            Spacer(modifier = Modifier.height(8.dp))

            ShhhTextField(
                value = text,
                onValueChange = { text = it },
                hint = "Có chuyện gì bạn muốn chia sẻ hôm nay?",
                lines = 6,
                autoGrow = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = " \uD83D\uDDBC\uFE0F Add Image",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )

            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF2C2C2E)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Image",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Settings, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(" \uFE0F Setup your post", fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Căn trái ngay dưới "Setup your post"
                Column {
                    Button(
                        onClick = { expanded = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5E5CE6),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.defaultMinSize(minHeight = 36.dp)
                    ) {
                        Text(text = selectedVisibility, fontSize = 13.sp)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Only my school") },
                            onClick = {
                                selectedVisibility = "Only my school"
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Public") },
                            onClick = {
                                selectedVisibility = "Public"
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(" \uD83C\uDFEB Posting as", fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.logo_uth1),
                    contentDescription = "UTH Logo",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("UTH", fontWeight = FontWeight.Bold)
                    Text(
                        "ĐH Giao Thông Vận Tải Tp. HCM",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
