package vn.dihaver.tech.shhh.confession.core.ui.util

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import vn.dihaver.tech.shhh.confession.core.util.FormatUtils.removeVietnameseDiacritics

@Composable
fun buildHighlightedText(text: String, query: String): AnnotatedString {
    val annotatedString = buildAnnotatedString {
        val queryLower = query.lowercase().trim().removeVietnameseDiacritics()
        if (queryLower.isEmpty()) {
            append(text)
            return@buildAnnotatedString
        }

        val textLower = text.lowercase().removeVietnameseDiacritics()
        var startIndex = 0

        while (startIndex < text.length) {
            val matchIndex = textLower.indexOf(queryLower, startIndex)
            if (matchIndex == -1) {
                append(text.substring(startIndex))
                break
            }
            append(text.substring(startIndex, matchIndex))
            withStyle(
                style = SpanStyle(
                    background = MaterialTheme.colorScheme.secondaryContainer,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(text.substring(matchIndex, matchIndex + queryLower.length))
            }
            startIndex = matchIndex + queryLower.length
        }
    }
    return annotatedString
}

@Composable
fun KeyValueText(
    key: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.W400,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = MaterialTheme.typography.bodySmall.fontFamily
            )) {
                append(key)
                append(": ")
            }
            withStyle(style = SpanStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.W600,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
            )) {
                append(value)
            }
        }
    )
}

@Composable
fun OdometerText(
    count: Long,
    modifier: Modifier = Modifier,
    style: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyLarge
) {
    // Dùng LaunchedEffect và key là `count` để đảm bảo `oldCount` được cập nhật đúng.
    // Chúng ta cần một biến để lưu giá trị cũ của count.
    var oldCount by remember {
        mutableLongStateOf(count)
    }
    LaunchedEffect(count) {
        oldCount = count
    }

    Row(modifier = modifier) {
        val countString = count.toString()
        val oldCountString = oldCount.toString()

        // Để xử lý trường hợp 99 -> 100, chúng ta cần đảm bảo 2 chuỗi có độ dài bằng nhau
        // bằng cách thêm khoảng trắng vào đầu chuỗi ngắn hơn.
        val maxLength = maxOf(countString.length, oldCountString.length)
        val paddedCountString = countString.padStart(maxLength, ' ')
        val paddedOldCountString = oldCountString.padStart(maxLength, ' ')

        paddedCountString.forEachIndexed { index, newChar ->
            val oldChar = paddedOldCountString.getOrNull(index) ?: ' '

            // AnimatedContent cho từng chữ số
            AnimatedContent(
                targetState = newChar,
                transitionSpec = {
                    // Nếu không có gì thay đổi, không animation
                    if (initialState == targetState) {
                        fadeIn(animationSpec = tween(0)) togetherWith fadeOut(animationSpec = tween(0))
                    } else {
                        // So sánh giá trị số để quyết định trượt lên hay xuống
                        val isIncreasing = (targetState.digitToIntOrNull() ?: 0) > (initialState.digitToIntOrNull() ?: 0)

                        if (isIncreasing) {
                            // Trượt lên
                            slideInVertically { height -> height } + fadeIn() togetherWith
                                    slideOutVertically { height -> -height } + fadeOut()
                        } else {
                            // Trượt xuống
                            slideInVertically { height -> -height } + fadeIn() togetherWith
                                    slideOutVertically { height -> height } + fadeOut()
                        }.using(
                            // Giữ kích thước không đổi trong lúc chuyển đổi
                            SizeTransform(clip = false, sizeAnimationSpec = { _, _ -> tween(300) })
                        )
                    }
                },
                label = "OdometerDigitAnimation"
            ) { charToDisplay ->
                Text(
                    text = charToDisplay.toString(),
                    style = style,
                    softWrap = false
                )
            }
        }
    }
}
