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
import vn.dihaver.tech.shhh.confession.core.domain.auth.usecase.ResetPasswordUseCase
import vn.dihaver.tech.shhh.confession.core.util.ApiException
import vn.dihaver.tech.shhh.confession.core.util.FormatUtils.hashSHA256
import vn.dihaver.tech.shhh.confession.feature.auth.AuthViewModel
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.ResetPasswordRequest
import vn.dihaver.tech.shhh.confession.feature.auth.model.AuthContext
import vn.dihaver.tech.shhh.confession.feature.auth.model.MsgError
import vn.dihaver.tech.shhh.confession.feature.auth.model.MsgError.ERROR_INVALID_CREDENTIAL
import vn.dihaver.tech.shhh.confession.feature.auth.model.MsgError.ERROR_NETWORK
import vn.dihaver.tech.shhh.confession.feature.auth.model.MsgError.ERROR_USER_DISABLED
import vn.dihaver.tech.shhh.confession.feature.auth.model.MsgError.ERROR_USER_NOT_FOUND
import vn.dihaver.tech.shhh.confession.feature.auth.model.ResultLogin
import vn.dihaver.tech.shhh.confession.feature.auth.model.TypeLogin
import vn.dihaver.tech.shhh.confession.feature.auth.ui.state.CreatePassUiState

@HiltViewModel(assistedFactory = CreatePassViewModel.Factory::class)
class CreatePassViewModel @AssistedInject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase,
    @Assisted private val authViewModel: AuthViewModel
) : ViewModel() {

    companion object {
        private const val TAG = "AAA-CreatePassViewModel"
        private const val ALLOWED_SPECIAL_CHARS = "!@#$%^&*()-_=+[]{};:'\",.<>?/\\|`~"
        private val ALLOWED_CHARS_REGEX = Regex("[A-Za-z0-9${Regex.escape(ALLOWED_SPECIAL_CHARS)}]")
    }

    private val _uiState = MutableStateFlow(CreatePassUiState())
    val uiState: StateFlow<CreatePassUiState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                authContext = authViewModel.authContext.value,
                email = authViewModel.email
            )
        }
        listenToAuthChange()
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
        validatePassword()
    }

    private fun validatePassword() {
        val password = _uiState.value.password

        val invalidChars = password.filter { !it.toString().matches(ALLOWED_CHARS_REGEX) }.toSet()
        val hasMinLength = password.length in 8..20
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { ALLOWED_SPECIAL_CHARS.contains(it) }
        val hasRequiredChars = hasUpperCase && hasDigit && hasSpecialChar
        val isValidPassword = hasMinLength && hasRequiredChars && invalidChars.isEmpty()

        val errorMessage = when {
            invalidChars.isNotEmpty() -> {
                "Không được dùng \"${
                    invalidChars.take(3).joinToString(" ")
                }\" trong mật khẩu"
            }

            password.isNotEmpty() && !hasMinLength -> {
                if (password.length < 8) "Mật khẩu phải có ít nhất 8 ký tự"
                else "Mật khẩu không được quá 20 ký tự"
            }

            password.isNotEmpty() && !hasRequiredChars -> {
                "Mật khẩu phải chứa chữ hoa, số và ký tự đặc biệt"
            }

            else -> null
        }

        _uiState.update {
            it.copy(
                hasMinLength = hasMinLength,
                hasRequiredChars = hasRequiredChars,
                isValidPassword = isValidPassword,
                error = errorMessage
            )
        }
    }


    fun onSubmitPassword(onSuccessForgetPassword: () -> Unit) {
        validatePassword()

        val currentState = _uiState.value
        if (!currentState.isValidPassword) {
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            when (currentState.authContext) {
                is AuthContext.Email.Register -> authViewModel.signUpWithEmail(
                    email = currentState.email,
                    password = currentState.password
                )

                is AuthContext.Email.ForgetPassword -> {
                    try {
                        val request = ResetPasswordRequest(
                            email = currentState.email,
                            password = currentState.password,
                            passwordHash = currentState.password.hashSHA256()
                        )
                        resetPasswordUseCase.invoke(request)
                        onSuccessForgetPassword()
                    } catch (e: ApiException) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = when (e.code) {
                                    400 -> "Yêu cầu không hợp lệ"
                                    409 -> "Email đã được sử dụng"
                                    429 -> "Quá nhiều yêu cầu. Vui lòng thử lại sau ít phút"
                                    in 500..599 -> "Lỗi máy chủ. Vui lòng thử lại sau ít phút"
                                    else -> "Có lỗi xảy ra. Vui lòng thử lại"
                                }
                            )
                        }
                        Log.e(TAG, "ApiException: ${e.code} - ${e.message}")
                    } catch (e: Exception) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = "Có lỗi xảy ra. Vui lòng thử lại"
                            )
                        }
                        Log.e(TAG, "Lỗi không xác định: ${e.message}")
                    }
                }

                else -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Tình huống không hợp lệ :(("
                        )
                    }
                    Log.e(TAG, "Tình huống không hợp lệ: ${currentState.authContext}")
                }
            }
        }
    }

    fun onNavigationHandled() {
        _uiState.update { it.copy(navigateTo = false) }
        authViewModel.onAuthEventHandled()
    }

    private fun listenToAuthChange() {
        viewModelScope.launch {
            authViewModel.authState.collect { result ->
                when (result) {
                    is ResultLogin.Error -> if (result.typeLogin == TypeLogin.SIGN_UP_WITH_MAIL) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = mapFirebaseErrorToString(result.msgError)
                            )
                        }
                        Log.e(TAG, "Lỗi đăng ký: ${result.msgError} - ${result.message}")
                    }

                    is ResultLogin.Success -> if (result.typeLogin == TypeLogin.SIGN_UP_WITH_MAIL) {
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
            ERROR_INVALID_CREDENTIAL -> "Thông tin đăng nhập không hợp lệ"
            ERROR_USER_NOT_FOUND -> "Tài khoản không tồn tại"
            ERROR_USER_DISABLED -> "Tài khoản đã bị vô hiệu hóa"
            ERROR_NETWORK -> "Lỗi kết nối mạng. Vui lòng kiểm tra lại"
            else -> "Có lỗi xảy ra. Vui lòng thử lại"
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(authViewModel: AuthViewModel): CreatePassViewModel
    }
}