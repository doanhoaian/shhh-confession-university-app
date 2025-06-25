package vn.dihaver.tech.shhh.confession.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider


/*@Composable
fun ShhhTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) DarkColorScheme else LightColorScheme
    *//*val extraColors = if (useDarkTheme) DarkExtraColorScheme else LightExtraColorScheme*//*

    CompositionLocalProvider(LocalExtraColors provides DarkExtraColorScheme) {
        MaterialTheme(
            colorScheme = colors,
            typography = ShhhTypography,
            shapes = ShhhShapes,
            content = content
        )
    }
}*/

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
