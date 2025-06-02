package vn.dihaver.tech.shhh.confession.core.util

object EmailUtils {
    /**
     * Kiểm tra tính hợp lệ của email.
     * @param email Chuỗi email cần kiểm tra.
     * @return Pair<Boolean, String?> với Boolean chỉ ra email có hợp lệ không,
     *         và String? chứa thông báo lỗi nếu có.
     */
    fun isValidEmail(email: String): Pair<Boolean, String?> {
        // Kiểm tra email rỗng hoặc chỉ chứa khoảng trắng
        if (email.isBlank()) {
            return Pair(false, "Email không được để trống")
        }

        // Kiểm tra độ dài tối đa (ví dụ: 254 ký tự theo chuẩn RFC 5321)
        if (email.length > 254) {
            return Pair(false, "Email quá dài")
        }

        // Kiểm tra định dạng email bằng regex
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()
        if (!emailRegex.matches(email)) {
            return Pair(false, "Email không hợp lệ")
        }

        // Kiểm tra các ký tự không hợp lệ
        if (email.contains("[\\n\\r\\t]".toRegex())) {
            return Pair(false, "Email chứa ký tự không hợp lệ")
        }

        // Kiểm tra phần domain (phải có ít nhất một dấu chấm và phần mở rộng hợp lệ)
        val domain = email.substringAfterLast("@")
        if (!domain.contains(".") || domain.endsWith(".")) {
            return Pair(false, "Domain không hợp lệ")
        }

        return Pair(true, null)
    }
}