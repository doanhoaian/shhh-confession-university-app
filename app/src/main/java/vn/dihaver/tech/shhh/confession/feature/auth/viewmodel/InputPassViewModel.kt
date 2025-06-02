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
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import vn.dihaver.tech.shhh.confession.feature.auth.AuthViewModel
import vn.dihaver.tech.shhh.confession.feature.auth.model.AuthContext
import vn.dihaver.tech.shhh.confession.feature.auth.model.MsgError.ERROR_INVALID_CREDENTIAL
import vn.dihaver.tech.shhh.confession.feature.auth.model.MsgError.ERROR_NETWORK
import vn.dihaver.tech.shhh.confession.feature.auth.model.MsgError.ERROR_USER_DISABLED
import vn.dihaver.tech.shhh.confession.feature.auth.model.MsgError.ERROR_USER_NOT_FOUND
import vn.dihaver.tech.shhh.confession.feature.auth.model.ResultLogin
import vn.dihaver.tech.shhh.confession.feature.auth.model.TypeLogin

@HiltViewModel(assistedFactory = InputPassViewModel.Factory::class)
class InputPassViewModel @AssistedInject constructor(
    @Assisted private val authViewModel: AuthViewModel
) : ViewModel() {

    companion object {
        private const val TAG = "AAA-InputPassViewModel"
    }

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

    init {
        email = authViewModel.email
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        isValidPassword = newPassword.length in 8..20
        isError = newPassword.isNotEmpty() && !isValidPassword
        errorMessage = if (isError) {
            if (newPassword.length < 8) "Mật khẩu phải có ít nhất 8 ký tự"
            else "Mật khẩu không được quá 20 ký tự"
        } else null
    }

    fun signIn(onNext: () -> Unit) {
        if (!isValidPassword) {
            isError = true
            errorMessage = if (password.length < 8) {
                "Mật khẩu phải có ít nhất 8 ký tự"
            } else {
                "Mật khẩu không được quá 20 ký tự"
            }
            return
        }

        viewModelScope.launch {
            isLoading = true
            isError = false
            errorMessage = null

            try {
                authViewModel.signInWithEmail(email, password)

                authViewModel.authState
                    .filter { state ->
                        state is ResultLogin.Idle ||
                                (state is ResultLogin.Loading && state.typeLogin == TypeLogin.SIGN_IN_WITH_MAIL) ||
                                (state is ResultLogin.Success && state.typeLogin == TypeLogin.SIGN_IN_WITH_MAIL) ||
                                (state is ResultLogin.Error && state.typeLogin == TypeLogin.SIGN_IN_WITH_MAIL)
                    }
                    .collect { state ->
                        when (state) {
                            is ResultLogin.Success -> {
                                isLoading = false
                                authViewModel.updateAuthContext(AuthContext.Email.Login)
                                onNext()
                            }

                            is ResultLogin.Error -> {
                                isLoading = false
                                isError = true
                                errorMessage = when (state.msgError) {
                                    ERROR_INVALID_CREDENTIAL -> "Mật khẩu không đúng"
                                    ERROR_USER_NOT_FOUND -> "Tài khoản không tồn tại"
                                    ERROR_USER_DISABLED -> "Tài khoản đã bị vô hiệu hóa"
                                    ERROR_NETWORK -> "Lỗi kết nối mạng. Vui lòng kiểm tra lại"
                                    else -> "Có lỗi xảy ra. Vui lòng thử lại"
                                }
                                Log.e(TAG, "Lỗi đăng nhập: ${state.msgError} - ${state.message}")
                            }

                            else -> {}
                        }
                    }
            } catch (e: Exception) {
                isLoading = false
                isError = true
                errorMessage = "Có lỗi xảy ra. Vui lòng thử lại"
                Log.e(TAG, "Lỗi không xác định: ${e.message}")
            }
        }
    }

    fun onForgetPassword(onNext: () -> Unit) {
        authViewModel.updateAuthContext(AuthContext.Email.ForgetPassword)
        onNext()
    }

    @AssistedFactory
    interface Factory {
        fun create(authViewModel: AuthViewModel): InputPassViewModel
    }
}