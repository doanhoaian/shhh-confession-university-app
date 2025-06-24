package vn.dihaver.tech.shhh.confession.feature.auth.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vn.dihaver.tech.shhh.confession.core.domain.auth.usecase.SendOtpUseCase
import vn.dihaver.tech.shhh.confession.core.domain.auth.usecase.VerifyOtpUseCase
import vn.dihaver.tech.shhh.confession.core.util.ApiException
import vn.dihaver.tech.shhh.confession.feature.auth.AuthViewModel
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.OtpType
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.SendOtpRequest
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.VerifyOtpRequest
import vn.dihaver.tech.shhh.confession.feature.auth.model.AuthContext
import java.io.IOException

@HiltViewModel(assistedFactory = InputOtpViewModel.Factory::class)
class InputOtpViewModel @AssistedInject constructor(
    private val sendOtpUseCase: SendOtpUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    @Assisted private val authViewModel: AuthViewModel
) : ViewModel() {

    companion object {
        private const val TAG = "AAA-InputOtpViewModel"
        private const val COOLDOWN_SECONDS = 60
    }

    val authContext: StateFlow<AuthContext> = authViewModel.authContext

    var otpType by mutableStateOf(OtpType.VERIFY_EMAIL)
        private set

    var email by mutableStateOf("")
        private set

    var otpValue by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var hasOtpSentSuccessfully by mutableStateOf(false)
        private set

    var cooldown by mutableIntStateOf(0)
        private set

    var isError by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var showReenterEmail by mutableStateOf(false)
        private set

    private var sendOtpJob: Job? = null
    private var verifyOtpJob: Job? = null
    private var cooldownJob: Job? = null

    init {
        email = authViewModel.email

        if (authContext.value == AuthContext.Email.ForgetPassword) {
            otpType = OtpType.RESET_PASSWORD
        }

        sendOtp()
    }

    private fun startCooldown() {
        cooldownJob?.cancel()
        cooldown = COOLDOWN_SECONDS
        cooldownJob = viewModelScope.launch {
            while (cooldown > 0) {
                delay(1000)
                cooldown--
            }
        }
    }

    fun onOtpValueChange(newOtp: String) {
        otpValue = newOtp.filter { it.isDigit() }.take(4)
        isError = false
        errorMessage = null
    }

    fun sendOtp() {
        if (cooldown > 0 || isLoading) return

        sendOtpJob?.cancel()
        sendOtpJob = viewModelScope.launch {
            isLoading = true
            isError = false
            errorMessage = null
            showReenterEmail = false

            val request = SendOtpRequest(
                email = email,
                type = otpType
            )

            Log.i(TAG, request.toString())

            try {
                sendOtpUseCase.invoke(request)
                hasOtpSentSuccessfully = true
                startCooldown()
            } catch (e: ApiException) {
                isError = true
                errorMessage = when (e.code) {
                    400 -> {
                        showReenterEmail = true
                        "Email bạn nhập không tồn tại"
                    }
                    429 -> "Quá nhiều yêu cầu. Vui lòng thử lại sau ít phút"
                    in 500..599 -> "Lỗi máy chủ. Vui lòng thử lại sau ít phút"
                    else -> "Có lỗi xảy ra khi gửi OTP"
                }
                Log.e(TAG, "ApiException: ${e.code} - ${e.message}")
            } catch (e: IOException) {
                isError = true
                errorMessage = "Lỗi kết nối mạng. Vui lòng kiểm tra lại"
                Log.e(TAG, "IOException: ${e.message}")
            } catch (e: Exception) {
                isError = true
                errorMessage = "Có lỗi xảy ra. Vui lòng thử lại"
                Log.e(TAG, "Lỗi không xác định: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun verifyOtp(onNext: () -> Unit) {
        if (!hasOtpSentSuccessfully) {
            isError = true
            errorMessage = "Email OTP chưa được gửi thành công. Vui lòng nhấn gửi lại mã"
            return
        }

        if (otpValue.length < 4) {
            isError = true
            errorMessage = "Vui lòng nhập đủ 4 chữ số OTP"
            return
        }

        verifyOtpJob?.cancel()
        verifyOtpJob = viewModelScope.launch {
            isLoading = true
            isError = false
            errorMessage = null

            val request = VerifyOtpRequest(
                email = email,
                otp = otpValue,
                type = otpType
            )

            try {
                val isValid = verifyOtpUseCase.invoke(request)
                if (isValid) {
                    onNext()
                } else {
                    isError = true
                    errorMessage = "Mã OTP không đúng"
                }
            } catch (e: ApiException) {
                isError = true
                errorMessage = when (e.code) {
                    400 -> "Mã OTP không đúng"
                    401 -> "Mã OTP đã hết hạn"
                    429 -> "Quá nhiều yêu cầu. Vui lòng thử lại sau ít phút"
                    in 500..599 -> "Lỗi máy chủ. Vui lòng thử lại sau ít phút"
                    else -> e.message ?: "Có lỗi xảy ra khi xác minh OTP"
                }
                Log.e(TAG, "ApiException: ${e.code} - ${e.message}")
            } catch (e: IOException) {
                isError = true
                errorMessage = "Lỗi kết nối mạng. Vui lòng kiểm tra lại"
                Log.e(TAG, "IOException: ${e.message}")
            } catch (e: Exception) {
                isError = true
                errorMessage = "Có lỗi xảy ra. Vui lòng thử lại"
                Log.e(TAG, "Lỗi không xác định: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        sendOtpJob?.cancel()
        verifyOtpJob?.cancel()
        cooldownJob?.cancel()
    }

    @AssistedFactory
    interface Factory {
        fun create(authViewModel: AuthViewModel): InputOtpViewModel
    }
}