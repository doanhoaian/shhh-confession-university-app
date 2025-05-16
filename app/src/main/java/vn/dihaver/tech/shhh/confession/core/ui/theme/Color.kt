package vn.dihaver.tech.shhh.confession.core.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class ExtraColors(
    val success: Color,
    val onSuccess: Color,
    val warning: Color,
    val onWarning: Color,
    val info: Color,
    val onInfo: Color,
)

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6366F1),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF4F46A5),
    onPrimaryContainer = Color(0xFFE0E7FF),
    secondary = Color(0xFF818CF8),
    onSecondary = Color.White,
    surface = Color(0xFF1E1E2E),
    onSurface = Color(0xFFE5E7EB),
    surfaceVariant = Color(0xFF2A2A3C),
    onSurfaceVariant = Color(0xFFA1A1AA),
    background = Color(0xFF161619),
    error = Color(0xFFEF4444),
    onError = Color.White

)

val DarkExtraColorScheme = ExtraColors(
    success = Color(0xFF22C55E),
    onSuccess = Color.White,
    warning = Color(0xFFFACC15),
    onWarning = Color.Black,
    info = Color(0xFF38BDF8),
    onInfo = Color.White
)

val LocalExtraColors = staticCompositionLocalOf { DarkExtraColorScheme }