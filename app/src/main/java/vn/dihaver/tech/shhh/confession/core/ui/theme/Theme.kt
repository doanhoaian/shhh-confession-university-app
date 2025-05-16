package vn.dihaver.tech.shhh.confession.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider


@Composable
fun ShhhTheme(
    content: @Composable () -> Unit
) {

    CompositionLocalProvider(LocalExtraColors provides DarkExtraColorScheme) {
        MaterialTheme(
            colorScheme = DarkColorScheme,
            typography = ShhhTypography,
            shapes = ShhhShapes,
            content = content
        )
    }
}