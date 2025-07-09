package vn.dihaver.tech.shhh.confession.core.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput

enum class ClickEffect {
    Ripple,
    Bounce,
    Fade
}

enum class ButtonState {
    Pressed, Idle
}

fun Modifier.bounceClick(onClick: () -> Unit) = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val scale by animateFloatAsState(
        targetValue = if (buttonState == ButtonState.Pressed) 0.7f else 1f,
        label = "bounceClick"
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
        .pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    awaitFirstDown(false)
                    buttonState = ButtonState.Pressed
                    waitForUpOrCancellation()
                    buttonState = ButtonState.Idle
                }
            }
        }
}

fun Modifier.fadeClick(onClick: () -> Unit): Modifier = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val alpha by animateFloatAsState(if (buttonState == ButtonState.Pressed) 0.5f else 1f)

    this
        .alpha(alpha)
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
        .pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    awaitFirstDown(false)
                    buttonState = ButtonState.Pressed
                    waitForUpOrCancellation()
                    buttonState = ButtonState.Idle
                }
            }
        }
}