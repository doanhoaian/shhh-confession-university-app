package vn.dihaver.tech.shhh.confession.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhDimens
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhTheme
import vn.dihaver.tech.shhh.confession.core.ui.util.spToDp

@Preview
@Composable
private fun ButtonPreview() {
    var text by remember { mutableStateOf("") }

    ShhhTheme {
        Column(
            Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(20.dp), verticalArrangement = Arrangement.Center
        ) {
            ShhhTextField(
                value = text,
                onValueChange = { text = it },
                leadingIcon = Icons.Filled.Email,
                hint = "Nhập email của bạn"
            )

            Spacer(modifier = Modifier.height(ShhhDimens.SpacerLarge))

            ShhhTextField(
                value = text,
                onValueChange = { text = it },
                lines = 6,
                hint = "Hôm nay bạn cảm thấy thế nào?"
            )

            Spacer(modifier = Modifier.height(ShhhDimens.SpacerLarge))

            ShhhTextField(value = text, onValueChange = { text = it }, hint = "Nhập tin nhắn...", lines = 4, autoGrow = true)
        }
    }
}

@Composable
fun ShhhTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    lines: Int = 1,
    autoGrow: Boolean = false,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val shape = MaterialTheme.shapes.large
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    val textColor = MaterialTheme.colorScheme.onSurface
    val hintColor = MaterialTheme.colorScheme.onSurfaceVariant

    val minHeight = ShhhDimens.TextFieldHeight
    val lineHeightSp = 24.sp

    val maxHeightDp = ((lineHeightSp * lines)).spToDp() + 30.dp

    val baseModifier = modifier
        .fillMaxWidth()
        .clip(shape)
        .then(
            when {
                lines == 1 -> Modifier.height(minHeight)
                autoGrow -> Modifier
                    .defaultMinSize(minHeight = minHeight)
                    .heightIn(max = maxHeightDp)
                else -> Modifier
                    .height(maxHeightDp)
            }
        )

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = baseModifier,
        shape = shape,
        singleLine = lines == 1,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = textColor,
            lineHeight = lineHeightSp
        ),
        placeholder = {
            Text(
                text = hint,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = hintColor,
                    lineHeight = lineHeightSp
                )
            )
        },
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = hintColor
                )
            }
        },
        trailingIcon = trailingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = textColor
                )
            }
        },
        enabled = enabled,
        maxLines = if (autoGrow) lines else Int.MAX_VALUE,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = backgroundColor,
            unfocusedContainerColor = backgroundColor,
            disabledContainerColor = backgroundColor.copy(alpha = 0.4f),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            disabledTextColor = textColor.copy(alpha = 0.4f),
            focusedLeadingIconColor = textColor,
            unfocusedLeadingIconColor = textColor,
            focusedTrailingIconColor = textColor,
            unfocusedTrailingIconColor = textColor
        )
    )
}
