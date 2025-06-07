package vn.dihaver.tech.shhh.confession.core.domain.auth.model

data class CheckEmail(
    val isExists: Boolean,
    val providers: List<String>
)
