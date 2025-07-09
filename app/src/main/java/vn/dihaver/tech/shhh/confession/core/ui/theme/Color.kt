package vn.dihaver.tech.shhh.confession.core.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Màu thương hiệu chính (brand color)
val BrandBlue = Color(0xFF1171A8) // OKLCH ~ (0.60, 0.16, 230°), giữ nguyên giữa 2 theme

val LightColorScheme = lightColorScheme(
    // === PRIMARY ===
    primary = BrandBlue,
    onPrimary = Color(0xFFFFFFFF),                 // Trắng để tương phản
    primaryContainer = Color(0xFFD6EFFF),          // Nhạt hơn, cùng hue
    onPrimaryContainer = Color(0xFF002E60),        // Rất đậm để nổi bật

    // === SECONDARY ===
    secondary = Color(0xFF6D4C41),                 // Nâu nhạt (tương phản nhẹ, không lấn át brand)
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF1DED9),
    onSecondaryContainer = Color(0xFF2B170D),

    // === TERTIARY ===
    tertiary = Color(0xFF00695C),                  // Xanh ngọc đậm
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFB2DFDB),
    onTertiaryContainer = Color(0xFF00201B),

    // === ERROR ===
    error = Color(0xFFB00020),                     // Đỏ cảnh báo chuẩn iOS
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD4),
    onErrorContainer = Color(0xFF410001),

    // === SURFACE & BACKGROUND ===
    background = Color(0xFFF0F2F5),
    onBackground = Color(0xFF1A1C1E),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A1C1E),

    // === VARIANTS & OUTLINE ===
    surfaceVariant = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFF4D4D4D),
    outline = Color(0xFF8A8A8A),
    outlineVariant = Color(0xFFD0D0D0),

    // === SPECIAL ===
    scrim = Color(0xFF000000),
    inversePrimary = Color(0xFF90CAF9),
    inverseSurface = Color(0xFF2D2D2D),
    inverseOnSurface = Color(0xFFF0F0F0),
)

val DarkColorScheme = darkColorScheme(
    // === PRIMARY ===
    primary = BrandBlue,                           // Giữ nguyên, giống iOS
    onPrimary = Color(0xFFFFFFFF),                 // Đen hoặc trắng tùy độ tương phản
    primaryContainer = Color(0xFF003C72),          // Tối hơn, cùng hue
    onPrimaryContainer = Color(0xFFD6EFFF),

    // === SECONDARY ===
    secondary = Color(0xFFBCAAA4),
    onSecondary = Color(0xFF3E2723),
    secondaryContainer = Color(0xFF4E342E),
    onSecondaryContainer = Color(0xFFF1DED9),

    // === TERTIARY ===
    tertiary = Color(0xFF80CBC4),
    onTertiary = Color(0xFF00332E),
    tertiaryContainer = Color(0xFF004D43),
    onTertiaryContainer = Color(0xFFB2DFDB),

    // === ERROR ===
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD4),

    // === SURFACE & BACKGROUND ===
    background = Color(0xFF121212),
    onBackground = Color(0xFFE2E2E6),
    surface = Color(0xFF1A1C1E),
    onSurface = Color(0xFFE2E2E6),

    // === VARIANTS & OUTLINE ===
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFCACACA),
    outline = Color(0xFF8C8C8C),
    outlineVariant = Color(0xFF4A4A4A),

    // === SPECIAL ===
    scrim = Color(0xFF000000),
    inversePrimary = BrandBlue,
    inverseSurface = Color(0xFFE2E2E6),
    inverseOnSurface = Color(0xFF1A1C1E),
)

data class ExtraColors(
    val success: Color,
    val onSuccess: Color,
    val warning: Color,
    val onWarning: Color,
    val info: Color,
    val onInfo: Color,
)

val DarkExtraColorScheme = ExtraColors(
    success = Color(0xFF22C55E),
    onSuccess = Color.White,

    warning = Color(0xFFFACC15),
    onWarning = Color(0xFF1E1E2E),

    info = Color(0xFF38BDF8),
    onInfo = Color.White
)


val LocalExtraColors = staticCompositionLocalOf { DarkExtraColorScheme }