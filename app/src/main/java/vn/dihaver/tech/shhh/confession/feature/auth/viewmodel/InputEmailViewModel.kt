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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vn.dihaver.tech.shhh.confession.core.domain.auth.usecase.CheckEmailUseCase
import vn.dihaver.tech.shhh.confession.core.util.ApiException
import vn.dihaver.tech.shhh.confession.core.util.EmailUtils
import vn.dihaver.tech.shhh.confession.feature.auth.AuthViewModel
import vn.dihaver.tech.shhh.confession.feature.auth.model.AuthContext
import java.io.IOException

@HiltViewModel(assistedFactory = InputEmailViewModel.Factory::class)
class InputEmailViewModel @AssistedInject constructor(
    private val checkEmailUseCase: CheckEmailUseCase,
    @Assisted private val authViewModel: AuthViewModel
) : ViewModel() {

    companion object {
        private const val TAG = "AAA-InputEmailViewModel"
    }

    val authContext: StateFlow<AuthContext> = authViewModel.authContext

    var email by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isError by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isValidEmail by mutableStateOf(false)
        private set

    private var lastVerifyJob: Job? = null

    init {
        onEmailChange(authViewModel.email)
    }

    fun onEmailChange(newEmail: String) {
        val normalizedEmail = newEmail.lowercase()
        val (isValid, error) = EmailUtils.isValidEmail(normalizedEmail)
        email = normalizedEmail
        isValidEmail = isValid
        isError = !isValid
        errorMessage = error
    }

    fun verifyEmail(onNext: () -> Unit) {
        val (isValid, error) = EmailUtils.isValidEmail(email)
        if (!isValid) {
            isError = true
            errorMessage = error
            return
        }

        lastVerifyJob?.cancel()
        lastVerifyJob = viewModelScope.launch {
            isLoading = true
            isError = false
            errorMessage = null

            try {
                val result = checkEmailUseCase.invoke(email)
                val exists = result.isExists
                val providers = result.providers
                val context = authContext.value

                // ForgetPassword buộc phải tồn tại
                if (context is AuthContext.Email.ForgetPassword) {
                    if (!exists || !providers.contains("password")) {
                        throw ApiException(404, "Email chưa được đăng ký hoặc không hợp lệ để khôi phục")
                    }
                    applyEmailResult()
                    onNext()
                    return@launch
                }

                // Xác định Login / Register
                if (providers.contains("password")) {
                    applyEmailResult(true)
                    onNext()
                } else if (providers.contains("google.com")) {
                    throw ApiException(409, "Email đã được sử dụng với đăng nhập Google")
                } else {
                    // Chưa tồn tại tài khoản → sẽ đăng ký mới
                    applyEmailResult(false)
                    onNext()
                }

            } catch (e: ApiException) {
                isError = true
                errorMessage = when (e.code) {
                    400 -> "Yêu cầu không hợp lệ. Vui lòng kiểm tra lại email"
                    404 -> e.message ?: "Email chưa được đăng ký"
                    409 -> e.message ?: "Email đã được sử dụng với nhà cung cấp khác"
                    429 -> "Quá nhiều yêu cầu. Vui lòng thử lại sau vài phút"
                    in 500..599 -> "Lỗi máy chủ. Vui lòng thử lại sau"
                    else -> e.message ?: "Có gì đó không ổn. Vui lòng thử lại"
                }
                Log.e(TAG, "ApiException: ${e.code} - ${e.message}")
            } catch (e: IOException) {
                isError = true
                errorMessage = "Lỗi kết nối mạng. Vui lòng kiểm tra lại"
                Log.e(TAG, "IOException: ${e.message}")
            } catch (e: Exception) {
                isError = true
                errorMessage = "Có gì đó không ổn. Vui lòng thử lại"
                Log.e(TAG, "Unexpected error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    private fun applyEmailResult(isLogin: Boolean? = null) {
        authViewModel.updateEmail(email)
        if (isLogin != null) {
            val context = if (isLogin) AuthContext.Email.Login else AuthContext.Email.Register
            authViewModel.updateAuthContext(context)
        }
    }

    override fun onCleared() {
        super.onCleared()
        lastVerifyJob?.cancel()
    }

    @AssistedFactory
    interface Factory {
        fun create(authViewModel: AuthViewModel): InputEmailViewModel
    }
}