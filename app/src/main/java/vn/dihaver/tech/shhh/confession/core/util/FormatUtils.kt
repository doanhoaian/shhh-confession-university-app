package vn.dihaver.tech.shhh.confession.core.util

import java.text.Normalizer

object FormatUtils {
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