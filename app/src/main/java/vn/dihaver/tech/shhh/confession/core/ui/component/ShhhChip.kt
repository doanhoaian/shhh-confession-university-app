package vn.dihaver.tech.shhh.confession.core.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoActionChip(
    modifier: Modifier = Modifier,
    iconRes: Int,
    iconContentDescription: String? = null,
    text: String? = null,
    onClick: (() -> Unit)? = null,

    isActivated: Boolean = false,
    activatedIconRes: Int? = null,
    activatedColor: Color = MaterialTheme.colorScheme.primary,
    activatedBgColor: Color = Color.Transparent
) {
    val currentIconRes = if (isActivated && activatedIconRes != null) {
        activatedIconRes
    } else {
        iconRes
    }

    val tintAndTextColor by animateColorAsState(
        targetValue = if (isActivated) activatedColor else MaterialTheme.colorScheme.onSurface,
        label = "ChipTintColor"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isActivated) activatedBgColor else Color.Transparent,
        label = "ChipBackgroundColor"
    )

    val clickableModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    } else {
        Modifier
    }

    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(backgroundColor)
            .border(
                1.dp,
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                MaterialTheme.shapes.large
            )
            .then(clickableModifier)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = currentIconRes),
            contentDescription = iconContentDescription,
            modifier = Modifier.size(16.dp),
            tint = tintAndTextColor
        )

        if (text != null) {
            Spacer(Modifier.width(5.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.W700
                ),
                color = tintAndTextColor
            )
        }
    }
}