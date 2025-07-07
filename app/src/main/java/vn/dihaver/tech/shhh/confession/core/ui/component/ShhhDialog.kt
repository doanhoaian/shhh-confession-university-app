package vn.dihaver.tech.shhh.confession.core.ui.component

import android.annotation.SuppressLint
import android.graphics.drawable.shapes.Shape
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhTheme

@Composable
fun ShhhDialog(
    onDismiss: () -> Unit,
    confirmText: String,
    onConfirm: () -> Unit,
    dismissText: String? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(24.dp),
    tonalElevation: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = modifier,
            shape = shape,
            tonalElevation = tonalElevation
        ) {
            Column(
                modifier = Modifier.padding(
                    top = 24.dp,
                    bottom = 12.dp,
                    start = 24.dp,
                    end = 24.dp
                )
            ) {
                content()
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (dismissText != null) {
                        TextButton(onClick = onDismiss) {
                            Text(dismissText)
                        }
                        Spacer(Modifier.width(8.dp))
                    }
                    TextButton(onClick = onConfirm) {
                        Text(confirmText)
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun ShhhLoadingDialogPreview() {
    ShhhTheme(useDarkTheme = true) {
        ShhhLoadingDialog(visible = true, message = "Loading...")
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun ShhhLoadingDialogWithBackgroundPreview() {
    ShhhTheme(useDarkTheme = true) {
        ShhhLoadingDialog(
            visible = true,
            message = "Loading with background...",
            showBackground = true
        )
    }
}


@Composable
fun ShhhLoadingDialog(
    visible: Boolean,
    onDismiss: (() -> Unit)? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    progress: Float? = null,
    message: String? = null,
    showBackground: Boolean = false
) {
    if (!visible) return

    Dialog(
        onDismissRequest = { onDismiss?.invoke() },
        properties = DialogProperties(
            dismissOnBackPress = onDismiss != null,
            dismissOnClickOutside = onDismiss != null,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(
                    if (showBackground) MaterialTheme.colorScheme.background.copy(alpha = 0.75f)
                    else Color.Transparent
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .wrapContentSize()
                    .background(
                        if (showBackground) MaterialTheme.colorScheme.surface else Color.Transparent,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(24.dp)
            ) {
                if (progress == null) {
                    CircularProgressIndicator(color = if (showBackground) MaterialTheme.colorScheme.inverseSurface else Color.White)
                } else {
                    CircularProgressIndicator(
                        color = if (showBackground) MaterialTheme.colorScheme.inverseSurface else Color.White,
                        progress = { progress },
                        trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
                    )
                }

                if (!message.isNullOrBlank()) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium.copy(color = if (showBackground) MaterialTheme.colorScheme.onSurface else Color.White.copy(alpha = .8f)),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

