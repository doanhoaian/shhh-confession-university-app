package vn.dihaver.tech.shhh.confession.feature.auth.model

data class CheckEmail(
    val isExists: Boolean,
    val providers: List<String>
)
