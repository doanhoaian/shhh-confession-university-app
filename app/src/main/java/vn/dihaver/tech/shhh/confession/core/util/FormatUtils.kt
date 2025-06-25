package vn.dihaver.tech.shhh.confession.core.util

import android.content.Context
import vn.dihaver.tech.shhh.confession.R
import java.text.Normalizer

object FormatUtils {
    fun Long.formatCountDetail(context: Context): String {
        return when (this) {
            in 0..999 -> this.toString()
            in 1000..9999 -> String.format(context.getString(R.string.format_count_thousand), this)
                .replace(',', '.')

            in 10000..999_999 -> String.format(
                context.getString(R.string.format_count_big_thousand),
                this / 1000.0
            )

            in 1_000_000..999_999_999 -> String.format(
                context.getString(R.string.format_count_million),
                this / 1_000_000.0
            )

            else -> String.format(
                context.getString(R.string.format_count_billion),
                this / 1_000_000_000.0
            )
        }
    }

    fun Long.formatCountCompact(context: Context): String {
        return when (this) {
            in 0..999 -> this.toString()
            in 1000..999_999 -> String.format(
                context.getString(R.string.format_count_big_thousand),
                this / 1000.0
            )

            in 1_000_000..999_999_999 -> String.format(
                context.getString(R.string.format_count_million),
                this / 1_000_000.0
            )

            else -> String.format(
                context.getString(R.string.format_count_billion),
                this / 1_000_000_000.0
            )
        }
    }

    fun String.removeVietnameseDiacritics(): String {
        val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
        return normalized.replace("[\\p{M}]".toRegex(), "").replace("đ", "d").replace("Đ", "D")
    }

    fun String.hashSHA256(): String {
        return try {
            val digest = java.security.MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(this.toByteArray())
            hash.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            ""
        }
    }
}