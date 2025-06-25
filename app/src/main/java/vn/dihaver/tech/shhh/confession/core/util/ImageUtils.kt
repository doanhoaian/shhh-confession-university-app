package vn.dihaver.tech.shhh.confession.core.util

object ImageUtils {
    fun parseImageSize(url: String): Pair<Int, Int>? {
        val regex = Regex("w_(\\d+),h_(\\d+)")
        val match = regex.find(url)
        return match?.let {
            val width = it.groups[1]?.value?.toIntOrNull() ?: return null
            val height = it.groups[2]?.value?.toIntOrNull() ?: return null
            Pair(width, height)
        }
    }
}