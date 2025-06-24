package vn.dihaver.tech.shhh.confession.feature.auth.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import vn.dihaver.tech.shhh.confession.core.domain.auth.usecase.ResetPasswordUseCase
import vn.dihaver.tech.shhh.confession.core.util.ApiException
import vn.dihaver.tech.shhh.confession.core.util.FormatUtils.hashSHA256
import vn.dihaver.tech.shhh.confession.feature.auth.AuthViewModel
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.ResetPasswordRequest
import vn.dihaver.tech.shhh.confession.feature.auth.model.AuthContext
import vn.dihaver.tech.shhh.confession.feature.auth.model.MsgError
import vn.dihaver.tech.shhh.confession.feature.auth.model.ResultLogin
import vn.dihaver.tech.shhh.confession.feature.auth.model.TypeLogin
import java.io.IOException

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

    val authContext: StateFlow<AuthContext> = authViewModel.authContext

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isError by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isValidPassword by mutableStateOf(false)
        private set

    var hasMinLength by mutableStateOf(false)
        private set

    var hasRequiredChars by mutableStateOf(false)
        private set

    private var invalidChars by mutableStateOf(emptySet<Char>())

    init {
        email = authViewModel.email
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        validatePassword()
    }

    private fun validatePassword() {
        invalidChars = password.filter { !it.toString().matches(ALLOWED_CHARS_REGEX) }.toSet()
        hasMinLength = password.length in 8..20
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { ALLOWED_SPECIAL_CHARS.contains(it) }
        hasRequiredChars = hasUpperCase && hasDigit && hasSpecialChar
        isValidPassword = hasMinLength && hasRequiredChars && invalidChars.isEmpty()

        isError = password.isNotEmpty() && !isValidPassword
        errorMessage = when {
            invalidChars.isNotEmpty() -> "Không được dùng \"${
                invalidChars.take(3).joinToString(" ")
            }\" trong mật khẩu"

            password.isNotEmpty() && !hasMinLength -> {
                if (password.length < 8) "Mật khẩu phải có ít nhất 8 ký tự"
                else "Mật khẩu không được quá 20 ký tự"
            }

            password.isNotEmpty() && !hasRequiredChars -> "Mật khẩu phải chứa chữ hoa, số và ký tự đặc biệt"
            else -> null
        }
    }

    fun submitPassword(onNext: () -> Unit, onSuccessForgetPassword: () -> Unit) {
        if (!isValidPassword) {
            isError = true
            errorMessage = when {
                invalidChars.isNotEmpty() -> "Không được dùng \"${
                    invalidChars.take(3).joinToString(" ")
                }\" trong mật khẩu"

                !hasMinLength -> if (password.length < 8) "Mật khẩu phải có ít nhất 8 ký tự" else "Mật khẩu không được quá 20 ký tự"
                !hasRequiredChars -> "Mật khẩu phải chứa chữ hoa, số và ký tự đặc biệt"
                else -> "Vui lòng kiểm tra lại mật khẩu"
            }
            return
        }

        viewModelScope.launch {
            isLoading = true
            isError = false
            errorMessage = null

            try {
                when (authContext.value) {
                    is AuthContext.Email.Register -> {
                        authViewModel.signUpWithEmail(email, password)

                        val endState = authViewModel.authState.first { state ->
                            state is ResultLogin.Idle ||
                                    (state is ResultLogin.Loading && state.typeLogin == TypeLogin.SIGN_UP_WITH_MAIL) ||
                                    (state is ResultLogin.Success && state.typeLogin == TypeLogin.SIGN_UP_WITH_MAIL) ||
                                    (state is ResultLogin.Error && state.typeLogin == TypeLogin.SIGN_UP_WITH_MAIL)
                        }
                        if (endState is ResultLogin.Success) {
                            isLoading = false
                            onNext()
                        } else if (endState is ResultLogin.Error) {
                            isLoading = false
                            isError = true
                            errorMessage = when (endState.msgError) {
                                MsgError.ERROR_INVALID_CREDENTIAL -> "Thông tin đăng nhập không hợp lệ"
                                MsgError.ERROR_USER_NOT_FOUND -> "Tài khoản không tồn tại"
                                MsgError.ERROR_USER_DISABLED -> "Tài khoản đã bị vô hiệu hóa"
                                MsgError.ERROR_NETWORK -> "Lỗi kết nối mạng. Vui lòng kiểm tra lại"
                                else -> "Có lỗi xảy ra. Vui lòng thử lại"
                            }
                            Log.e(
                                TAG,
                                "Lỗi đăng nhập: ${endState.msgError} - ${endState.message}"
                            )
                        }
                    }

                    is AuthContext.Email.ForgetPassword -> {
                        val request = ResetPasswordRequest(
                            email = email,
                            password = password,
                            passwordHash = password.hashSHA256()
                        )
                        isLoading = true
                        resetPasswordUseCase.invoke(request)
                        isLoading = false

                        onSuccessForgetPassword()
                    }

                    else -> {
                        isLoading = false
                        isError = true
                        errorMessage = "Tình huống không hợp lệ"
                    }
                }
            } catch (e: ApiException) {
                isLoading = false
                isError = true
                errorMessage = when (e.code) {
                    400 -> "Yêu cầu không hợp lệ"
                    409 -> "Email đã được sử dụng"
                    429 -> "Quá nhiều yêu cầu. Vui lòng thử lại sau ít phút"
                    in 500..599 -> "Lỗi máy chủ. Vui lòng thử lại sau ít phút"
                    else -> "Có lỗi xảy ra. Vui lòng thử lại"
                }
                Log.e(TAG, "ApiException: ${e.code} - ${e.message}")
            } catch (e: IOException) {
                isLoading = false
                isError = true
                errorMessage = "Lỗi kết nối mạng. Vui lòng kiểm tra lại"
                Log.e(TAG, "IOException: ${e.message}")
            } catch (e: Exception) {
                isLoading = false
                isError = true
                errorMessage = "Có lỗi xảy ra. Vui lòng thử lại"
                Log.e(TAG, "Lỗi không xác định: ${e.message}")
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(authViewModel: AuthViewModel): CreatePassViewModel
    }
}