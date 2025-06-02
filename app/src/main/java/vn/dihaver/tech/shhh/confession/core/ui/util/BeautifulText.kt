package vn.dihaver.tech.shhh.confession.core.ui.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
