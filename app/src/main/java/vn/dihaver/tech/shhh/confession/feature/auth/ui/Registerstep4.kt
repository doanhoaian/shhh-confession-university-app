package vn.dihaver.tech.shhh.confession.feature.auth.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import vn.dihaver.tech.shhh.confession.R
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhButton
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhTextField
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhDimens
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search

@Composable
fun UniversityScreen(
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onUniversitySelected: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val universities = listOf(
        University("UTH", "ĐH Giao thông Vận tải", R.drawable.ic_uth_logo)
    )

    // Progress animation
    val progress = remember { Animatable(0f) }
    val backgroundColor = MaterialTheme.colorScheme.surface
    val primaryColor = MaterialTheme.colorScheme.primary

    LaunchedEffect(Unit) {
        progress.animateTo(0.8f, animationSpec = tween(durationMillis = 500)) // Step 4/5 = 80%
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with Back button, Register text, Step, and Progress Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onPreviousClick,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.svg_back),
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
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Step 4/5",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            // Progress Bar using Canvas
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
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

            // Title and Description
            Text(
                text = "Which university are you from?",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = "Select your school to connect with the right confession feed.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(ShhhDimens.SpacerLarge))

            // Search Bar using ShhhTextField with custom icon
            ShhhTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ShhhDimens.TextFieldHeight),
                hint = "Tìm kiếm trường của bạn?",
                leadingIcon = Icons.Default.Search // Thay bằng icon custom
            )

            Spacer(modifier = Modifier.height(ShhhDimens.SpacerMedium))

            // University List
            LazyColumn {
                items(universities) { university ->
                    UniversityItem(
                        university = university,
                        onClick = { onUniversitySelected(university.name) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Navigation Buttons using ShhhButton
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ShhhButton(
                    label = "Previous",
                    onClick = onPreviousClick,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(ShhhDimens.SpacerMedium))
                ShhhButton(
                    label = "Next step",
                    onClick = onNextClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

data class University(val name: String, val fullName: String, val logoRes: Int)

@Composable
fun UniversityItem(university: University, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = university.logoRes),
                contentDescription = university.name,
                modifier = Modifier.size(40.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = university.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = university.fullName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

