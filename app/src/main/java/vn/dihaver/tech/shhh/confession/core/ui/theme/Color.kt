package vn.dihaver.tech.shhh.confession.core.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
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


/*val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6366F1),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF4F46A5),
    onPrimaryContainer = Color(0xFFE0E7FF),
    secondary = Color(0xFF818CF8),
    onSecondary = Color(0xFFFFFFFF),
    surface = Color(0xFF1E1E2E),
    onSurface = Color(0xFFE5E7EB),
    surfaceVariant = Color(0xFF2A2A3C),
    onSurfaceVariant = Color(0xFFA1A1AA),
    background = Color(0xFF161619),
    error = Color(0xFFEF4444),
    onError = Color(0xFFFFFFFF)

)*/

val LightColorScheme = lightColorScheme(
    // === MÀU CHỦ ĐẠO (PRIMARY) ===
    // Dùng phiên bản đậm hơn để đảm bảo độ tương phản tốt với onPrimary (màu trắng).
    primary = Color(0xFF4447E0),
    // Chữ/icon trên nền màu primary.
    onPrimary = Color(0xFFFFFFFF),
    // Phiên bản màu pastel rất nhạt của primary.
    primaryContainer = Color(0xFFDEE0FF),
    // Chữ/icon trên nền primaryContainer, cần rất đậm để đọc được.
    onPrimaryContainer = Color(0xFF00038F),

    // === MÀU THỨ CẤP (SECONDARY) ===
    // Cũng làm đậm hơn một chút so với màu gốc.
    secondary = Color(0xFF008797),
    // Chữ/icon trên nền secondary.
    onSecondary = Color(0xFFFFFFFF),
    // Phiên bản màu pastel của secondary.
    secondaryContainer = Color(0xFFD8F6FA),
    // Chữ/icon trên nền secondaryContainer.
    onSecondaryContainer = Color(0xFF00383F),

    // === MÀU BỔ TRỢ (TERTIARY) ===
    tertiary = Color(0xFFA57D00),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFBEAAB),
    onTertiaryContainer = Color(0xFF3E3200),

    // === MÀU BÁO LỖI (ERROR) ===
    // Màu đỏ đậm hơn một chút cho theme sáng.
    error = Color(0xFFE52E2E),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD7),
    onErrorContainer = Color(0xFF5F0000),

    // === MÀU NỀN & BỀ MẶT (BACKGROUND & SURFACE) ===
    // Nền chính của app, màu trắng ngà có ánh xanh rất nhẹ.
    background = Color(0xFFF7F8FF),
    // Chữ/icon chính trên nền background.
    onBackground = Color(0xFF1D1C24),
    // Nền cho Card, BottomSheet... thường là màu trắng tinh để tạo độ tương phản sạch sẽ.
    surface = Color(0xFFFFFFFF),
    // Chữ/icon chính trên nền surface.
    onSurface = Color(0xFF1D1C24),

    // === CÁC BIẾN THỂ CỦA BỀ MẶT & ĐƯỜNG VIỀN (VARIANTS) ===
    // Nền xám nhạt có ánh xanh cho TextField, banner...
    surfaceVariant = Color(0xFFE9E9F7),
    // Chữ phụ, hint text, màu xám đậm vừa phải.
    onSurfaceVariant = Color(0xFF52505C),
    // Đường viền chính, màu xám có độ tương phản vừa phải.
    outline = Color(0xFF878592),
    // Đường viền trang trí, màu xám rất nhạt.
    outlineVariant = Color(0xFFD4D2E0),

    // === CÁC MÀU ĐẶC BIỆT KHÁC ===
    scrim = Color(0xFF000000),
    inversePrimary = Color(0xFF6366F1), // Là màu primary của theme tối
    // Nền tối trong theme sáng, dùng cho các thông báo đặc biệt.
    inverseSurface = Color(0xFF2B2A33),
    // Chữ/icon trên nền inverseSurface.
    inverseOnSurface = Color(0xFFE2E1E8),
)

val DarkColorScheme = darkColorScheme(
    // === MÀU CHỦ ĐẠO (PRIMARY) ===
    // Dành cho các nút chính, thành phần nổi bật nhất, trạng thái active.
    primary = Color(0xFF6366F1),
    // Chữ/icon trên nền màu primary.
    onPrimary = Color(0xFFFFFFFF),
    // Nền cho các thành phần cần nhấn mạnh nhẹ, nhưng vẫn liên quan đến primary (VD: Card được chọn).
    primaryContainer = Color(0xFF35327A),
    // Chữ/icon trên nền primaryContainer.
    onPrimaryContainer = Color(0xFFE2E1E8),

    // === MÀU THỨ CẤP (SECONDARY) ===
    // Dành cho các hành động phụ, bộ lọc (filter chips), hoặc các thành phần ít quan trọng hơn.
    secondary = Color(0xFF0096A8), // Màu xanh mòng két (Teal)
    // Chữ/icon trên nền secondary.
    onSecondary = Color(0xFF000000), // Dùng màu đen để có độ tương phản tốt nhất
    // Nền cho các thành phần liên quan đến secondary.
    secondaryContainer = Color(0xFF005059),
    // Chữ/icon trên nền secondaryContainer.
    onSecondaryContainer = Color(0xFFD1F3F8),

    // === MÀU BỔ TRỢ (TERTIARY) ===
    // Dành cho các điểm nhấn tương phản, không liên quan trực tiếp đến hành động chính (VD: tag "New", màu trên biểu đồ).
    tertiary = Color(0xFFB88B00), // Màu vàng gold/hổ phách
    // Chữ/icon trên nền tertiary.
    onTertiary = Color(0xFF000000),
    // Nền cho các thành phần liên quan đến tertiary.
    tertiaryContainer = Color(0xFF5C4D00),
    // Chữ/icon trên nền tertiaryContainer.
    onTertiaryContainer = Color(0xFFF0EDC8),

    // === MÀU BÁO LỖI (ERROR) ===
    // Dành cho các thông báo lỗi, hành động hủy bỏ nguy hiểm (destructive actions).
    error = Color(0xFFFF5B5B),
    // Chữ/icon trên nền error.
    onError = Color(0xFF000000),
    // Nền cho các thành phần liên quan đến error (VD: nền của TextField báo lỗi).
    errorContainer = Color(0xFF8B2121),
    // Chữ/icon trên nền errorContainer.
    onErrorContainer = Color(0xFFFFDAD4),

    // === MÀU NỀN & BỀ MẶT (BACKGROUND & SURFACE) ===
    // Nền chính của toàn bộ ứng dụng.
    background = Color(0xFF1D1C24),
    // Chữ/icon chính trên nền background.
    onBackground = Color(0xFFE2E1E8),
    // Nền cho các thành phần "nổi" lên trên background (Card, BottomSheet, Dialog...).
    surface = Color(0xFF2B2A33),
    // Chữ/icon chính trên nền surface.
    onSurface = Color(0xFFE2E1E8),

    // === CÁC BIẾN THỂ CỦA BỀ MẶT & ĐƯỜNG VIỀN (VARIANTS) ===
    // Một biến thể của màu surface, dùng cho các thành phần cần tách biệt nhẹ (thanh tìm kiếm, banner).
    surfaceVariant = Color(0xFF393842),
    // Chữ/icon trên nền surfaceVariant, thường là chữ phụ, hint text.
    onSurfaceVariant = Color(0xFFBAB9C2),
    // Màu cho các đường viền chính, đường kẻ phân chia (divider) có độ tương phản rõ.
    outline = Color(0xFF787682),
    // Màu cho các đường viền trang trí, ít quan trọng, độ tương phản thấp.
    outlineVariant = Color(0xFF484650),

    // === CÁC MÀU ĐẶC BIỆT KHÁC ===
    // Lớp phủ mờ để che nội dung bên dưới (khi mở modal drawer).
    scrim = Color(0xFF000000),
    // Màu primary sẽ dùng trên nền `inverseSurface`. Thường là màu primary của theme sáng.
    inversePrimary = Color(0xFF4447E0),
    // Một bề mặt có màu đối nghịch với theme hiện tại (nền sáng trong theme tối), dùng để làm nổi bật thông báo (Snackbar).
    inverseSurface = Color(0xFFF0EFF7),
    // Chữ/icon trên nền `inverseSurface`.
    inverseOnSurface = Color(0xFF24232B)
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