package vn.dihaver.tech.shhh.confession.feature.auth.ui.state

data class InputPassUiState(
    val email: String = "",
    val password: String = "",
    val error: String? = null,
    val isLoading: Boolean = false,
    val isValidPassword: Boolean = false,
    val navigateTo: Boolean = false
)