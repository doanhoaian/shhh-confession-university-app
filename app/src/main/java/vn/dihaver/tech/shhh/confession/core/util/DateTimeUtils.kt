package vn.dihaver.tech.shhh.confession.core.util

import org.threeten.bp.Duration
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit
import java.util.Locale

object DateTimeUtils {
    /**
     * Chuyển đổi chuỗi ngày giờ theo định dạng ISO 8601 sang chuỗi ngày tháng dễ đọc.
     *
     * Hàm này nhận vào một chuỗi ngày giờ theo định dạng ISO 8601 (ví dụ: `"2025-04-13T18:00:00.000Z"`)
     * và chuyển đổi nó thành một chuỗi ngày tháng dễ đọc theo định dạng `"d MMM, yyyy"` (ví dụ: `"13 Thg 4, 2025"`).
     *
     * Kết quả được định dạng theo múi giờ và ngôn ngữ mặc định của hệ thống.
     * Nếu chuỗi đầu vào không hợp lệ, hàm sẽ trả về chuỗi rỗng `""`.
     *
     * ### Ví dụ:
     * ```kotlin
     * val isoString = "2025-04-13T18:00:00.000Z"
     * val readableDate = isoString.isoToReadable()
     * println(readableDate) // 13 Thg 4, 2025 (tuỳ vào ngôn ngữ hệ thống)
     *
     * val invalidString = "không phải định dạng ngày"
     * val result = invalidString.isoToReadable()
     * println(result) // ""
     * ```
     *
     * @receiver Chuỗi ngày giờ theo định dạng ISO 8601.
     * @return Chuỗi ngày tháng dễ đọc hoặc chuỗi rỗng nếu có lỗi xảy ra.
     */
    fun String.isoToReadable(): String {
        return try {
            val instant = Instant.parse(this) // Xử lý dạng "2025-04-13T18:00:00.000Z"
            val zonedDateTime = instant.atZone(ZoneId.systemDefault())
            val formatter = DateTimeFormatter.ofPattern("d MMM, yyyy", Locale.getDefault())
            formatter.format(zonedDateTime)
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * Chuyển đổi chuỗi ngày giờ theo định dạng ISO 8601 sang giá trị epoch milliseconds.
     *
     * Hàm này nhận vào một chuỗi ngày giờ theo định dạng ISO 8601 (ví dụ: `"2025-04-13T18:00:00.000Z"`)
     * và chuyển đổi nó thành giá trị epoch milliseconds (số milliseconds kể từ 01/01/1970 UTC).
     *
     * Nếu chuỗi đầu vào không hợp lệ, hàm sẽ trả về giá trị `0L`.
     *
     * ### Ví dụ:
     * ```kotlin
     * val isoString = "2025-04-13T18:00:00.000Z"
     * val epochMillis = isoString.isoToEpochMillis()
     * println(epochMillis) // 1744567200000
     *
     * val invalidString = "không phải định dạng ngày"
     * val result = invalidString.isoToEpochMillis()
     * println(result) // 0
     * @receiver Chuỗi ngày giờ theo định dạng ISO 8601.
     * @return Giá trị epoch milliseconds hoặc 0L nếu có lỗi xảy ra.
     */
    fun String.isoToEpochMillis(): Long {
        return try {
            Instant.parse(this).toEpochMilli()
        } catch (e: Exception) {
            0L
        }
    }

    fun String.isoIsAfterNow(): Boolean {
        return try {
            val now = Instant.now()
            Instant.parse(this).isAfter(now)
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Chuyển đổi chuỗi ngày giờ theo định dạng ISO 8601 sang chuỗi thời gian tương đối so với hiện tại.
     *
     * Hàm này nhận vào một chuỗi ngày giờ theo định dạng ISO 8601 (ví dụ: `"2025-04-13T18:00:00.000Z"`)
     * và trả về một chuỗi mô tả khoảng thời gian so với thời điểm hiện tại.
     * - Nếu thời gian cách đây dưới 1 phút, trả về "vừa xong".
     * - Nếu thời gian cách đây dưới 1 giờ, trả về "X phút".
     * - Nếu thời gian cách đây dưới 1 ngày, trả về "X giờ".
     * - Nếu thời gian cách đây dưới 1 tuần, trả về "X ngày".
     * - Nếu thời gian cách đây từ 1 tuần trở lên, trả về định dạng ngày tháng dễ đọc (ví dụ: "13 Thg 4, 2025").
     *
     * Nếu chuỗi đầu vào không hợp lệ, hàm sẽ trả về chuỗi rỗng `""`.
     *
     * ### Ví dụ:
     */
    fun String.isoToRelativeTime(): String {
        return try {
            val instant = Instant.parse(this)
            val now = Instant.now()
            val duration = Duration.between(instant, now).abs()

            when {
                duration.toMinutes() < 1 -> "vừa xong"
                duration.toHours() < 1 -> "${duration.toMinutes()} phút"
                duration.toDays() < 1 -> "${duration.toHours()} giờ"
                duration.toDays() < 7 -> "${duration.toDays()} ngày"
                else -> this.isoToReadable()
            }
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * Chuyển đổi giá trị epoch milliseconds sang chuỗi thời gian tương đối.
     * @receiver Giá trị epoch milliseconds.
     * @return Chuỗi thời gian tương đối (ví dụ: "5 phút").
     */
    fun Long.toRelativeTime(): String {
        if (this == 0L) return "" // Xử lý trường hợp lỗi từ lúc parse
        try {
            val instant = Instant.ofEpochMilli(this)
            val now = Instant.now()
            val duration = Duration.between(instant, now).abs()

            return when {
                duration.toMinutes() < 1 -> "vừa xong"
                duration.toHours() < 1 -> "${duration.toMinutes()} phút"
                duration.toDays() < 1 -> "${duration.toHours()} giờ"
                duration.toDays() < 7 -> "${duration.toDays()} ngày"
                else -> this.toReadableDate() // Gọi hàm format khác
            }
        } catch (e: Exception) {
            return ""
        }
    }

    /**
     * Chuyển đổi giá trị epoch milliseconds sang chuỗi ngày tháng dễ đọc.
     * @receiver Giá trị epoch milliseconds.
     * @return Chuỗi ngày tháng (ví dụ: "13 Thg 4, 2025").
     */
    fun Long.toReadableDate(): String {
        if (this == 0L) return ""
        try {
            val instant = Instant.ofEpochMilli(this)
            val zonedDateTime = instant.atZone(ZoneId.systemDefault())
            val formatter = DateTimeFormatter.ofPattern("d MMM, yyyy", Locale.getDefault())
            return formatter.format(zonedDateTime)
        } catch (e: Exception) {
            return ""
        }
    }
}