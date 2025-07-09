package vn.dihaver.tech.shhh.confession.feature.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vn.dihaver.tech.shhh.confession.feature.auth.AuthViewModel
import vn.dihaver.tech.shhh.confession.feature.auth.model.AuthContext
import vn.dihaver.tech.shhh.confession.feature.auth.model.MsgError
import vn.dihaver.tech.shhh.confession.feature.auth.model.MsgError.ERROR_INVALID_CREDENTIAL
import vn.dihaver.tech.shhh.confession.feature.auth.model.MsgError.ERROR_NETWORK
import vn.dihaver.tech.shhh.confession.feature.auth.model.MsgError.ERROR_USER_DISABLED
import vn.dihaver.tech.shhh.confession.feature.auth.model.MsgError.ERROR_USER_NOT_FOUND
import vn.dihaver.tech.shhh.confession.feature.auth.model.ResultLogin
import vn.dihaver.tech.shhh.confession.feature.auth.model.TypeLogin
import vn.dihaver.tech.shhh.confession.feature.auth.ui.state.InputPassUiState

@HiltViewModel(assistedFactory = InputPassViewModel.Factory::class)
class InputPassViewModel @AssistedInject constructor(
    @Assisted private val authViewModel: AuthViewModel
) : ViewModel() {

    companion object {
        private const val TAG = "AAA-InputPassViewModel"
    }

    private val _uiState = MutableStateFlow(InputPassUiState())
    val uiState: StateFlow<InputPassUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(email = authViewModel.email) }
        listenToAuthChange()
    }

    fun onPasswordChange(newPassword: String) {
        val isValid = newPassword.length in 8..20
        _uiState.update {
            it.copy(
                password = newPassword,
                isValidPassword = isValid,
                error = if (newPassword.isNotEmpty() && !isValid) {
                    if (newPassword.length < 8) "Mật khẩu phải có ít nhất 8 ký tự"
                    else "Mật khẩu không được quá 20 ký tự"
                } else null
            )
        }
    }

    fun onSignIn() {
        val currentState = _uiState.value
        if (!currentState.isValidPassword) {
            _uiState.update { it.copy(error = "Mật khẩu không hợp lệ") }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            authViewModel.signInWithEmail(currentState.email, currentState.password)
        }
    }

    fun onForgetPassword() {
        authViewModel.updateAuthContext(AuthContext.Email.ForgetPassword)
        _uiState.update { it.copy(navigateTo = true) }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigateTo = false) }
        authViewModel.onAuthEventHandled()
    }

    private fun listenToAuthChange() {
        viewModelScope.launch {
            authViewModel.authState.collect { result ->
                when (result) {
                    is ResultLogin.Error -> if (result.typeLogin == TypeLogin.SIGN_IN_WITH_MAIL) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = mapFirebaseErrorToString(result.msgError)
                            )
                        }
                        Log.e(TAG, "Lỗi đăng nhập: ${result.msgError} - ${result.message}")
                    }

                    is ResultLogin.Success -> if (result.typeLogin == TypeLogin.SIGN_IN_WITH_MAIL) {
                        authViewModel.updateAuthContext(AuthContext.Email.Login)
                        _uiState.update { it.copy(isLoading = false, navigateTo = true) }
                    }

                    else -> {}
                }
            }
        }
    }

    private fun mapFirebaseErrorToString(errorCode: MsgError?): String {
        return when (errorCode) {
            ERROR_INVALID_CREDENTIAL -> "Mật khẩu không đúng"
            ERROR_USER_NOT_FOUND -> "Tài khoản không tồn tại"
            ERROR_USER_DISABLED -> "Tài khoản đã bị vô hiệu hóa"
            ERROR_NETWORK -> "Lỗi kết nối mạng. Vui lòng kiểm tra lại"
            else -> "Có lỗi xảy ra. Vui lòng thử lại"
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(authViewModel: AuthViewModel): InputPassViewModel
    }
}