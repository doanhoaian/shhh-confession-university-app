package vn.dihaver.tech.shhh.confession.feature.auth.ui.state

import vn.dihaver.tech.shhh.confession.feature.auth.model.AuthContext

data class CreatePassUiState(
    val authContext: AuthContext = AuthContext.Email.Register,
    val email: String = "",
    val password: String = "",
    val error: String? = null,
    val isLoading: Boolean = false,
    val isValidPassword: Boolean = false,
    val hasMinLength: Boolean = false,
    val hasRequiredChars: Boolean = false,
    val navigateTo: Boolean = false
)
