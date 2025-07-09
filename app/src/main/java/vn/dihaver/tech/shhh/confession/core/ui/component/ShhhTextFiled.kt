package vn.dihaver.tech.shhh.confession.core.ui.component

import android.util.Patterns
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vn.dihaver.tech.shhh.confession.R
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
                hint = "Nhập email của bạn",
                isError = !Patterns.EMAIL_ADDRESS.matcher(text).matches(),
                errorMessage = if (!Patterns.EMAIL_ADDRESS.matcher(text)
                        .matches()
                ) "Email không hợp lệ" else null
            )

            Spacer(modifier = Modifier.height(ShhhDimens.SpacerLarge))

            ShhhTextField(
                value = text,
                onValueChange = { text = it },
                lines = 6,
                hint = "Hôm nay bạn cảm thấy thế nào?"
            )

            Spacer(modifier = Modifier.height(ShhhDimens.SpacerLarge))

            ShhhTextField(
                value = text,
                onValueChange = { text = it },
                style = ShhhTextFieldStyle.Outlined,
                hint = "Nhập tin nhắn...",
                lines = 4,
                autoGrow = true
            )
        }
    }
}

sealed interface ShhhTextFieldStyle {
    data object Filled : ShhhTextFieldStyle
    data object Transparent : ShhhTextFieldStyle
    data object Outlined : ShhhTextFieldStyle
}

@Composable
fun ShhhTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    style: ShhhTextFieldStyle = ShhhTextFieldStyle.Filled,
    hint: String = "",
    lines: Int = 1,
    maxLength: Int = Int.MAX_VALUE,
    autoGrow: Boolean = false,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val shape = MaterialTheme.shapes.large
    val textColor = MaterialTheme.colorScheme.onSurface
    val hintColor = MaterialTheme.colorScheme.onSurfaceVariant

    val minHeight = ShhhDimens.TextFieldHeight
    val lineHeightSp = 24.sp
    val maxHeightDp = ((lineHeightSp * lines)).spToDp() + 30.dp

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isErrorVisible = isError && value.isNotEmpty()

    val backgroundColor = when (style) {
        is ShhhTextFieldStyle.Filled -> MaterialTheme.colorScheme.surfaceVariant
        is ShhhTextFieldStyle.Transparent -> Color.Transparent
        is ShhhTextFieldStyle.Outlined -> Color.Transparent
    }

    val animatedBorderColor by animateColorAsState(
        targetValue = when (style) {
            is ShhhTextFieldStyle.Transparent -> Color.Transparent
            is ShhhTextFieldStyle.Outlined -> when {
                isErrorVisible -> MaterialTheme.colorScheme.error
                isFocused -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.outline
            }
            is ShhhTextFieldStyle.Filled -> when {
                isErrorVisible -> MaterialTheme.colorScheme.error
                isFocused -> MaterialTheme.colorScheme.primary
                else -> Color.Transparent
            }
        },
        label = "borderColor"
    )

    val baseModifier = modifier
        .fillMaxWidth()
        .padding(0.dp)
        .clip(shape)
        .border(ShhhDimens.StrokeSmall, animatedBorderColor, shape)
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

    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val trailingIconSlot: (@Composable (() -> Unit))? = when {
        trailingIcon != null -> {
            {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    tint = textColor
                )
            }
        }

        isPassword -> {
            {
                val visibilityIcon =
                    if (passwordVisible) R.drawable.svg_font_fill_visibility_off else R.drawable.svg_font_fill_visibility
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(visibilityIcon),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                    )
                }
            }
        }

        else -> null
    }

    TextField(
        value = value,
        onValueChange = {
            if (it.length <= maxLength) {
                onValueChange(it)
            }
        },
        modifier = baseModifier,
        interactionSource = interactionSource,
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
        trailingIcon = trailingIconSlot,
        enabled = enabled,
        isError = isErrorVisible,
        visualTransformation = when {
            isPassword && !passwordVisible -> PasswordVisualTransformation()
            else -> VisualTransformation.None
        },
        maxLines = if (autoGrow) lines else Int.MAX_VALUE,
        keyboardOptions = if (isPassword)
            keyboardOptions.copy(keyboardType = KeyboardType.Password)
        else
            keyboardOptions,
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
    AnimatedVisibility(
        visible = isError && value.isNotEmpty() && !errorMessage.isNullOrBlank(),
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Text(
            text = errorMessage ?: "",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, start = 12.dp)
        )
    }
}