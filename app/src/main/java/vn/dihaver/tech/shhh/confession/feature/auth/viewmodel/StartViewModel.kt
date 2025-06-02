package vn.dihaver.tech.shhh.confession.feature.auth.viewmodel

import android.content.Context
import android.os.CancellationSignal
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialManagerCallback
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import vn.dihaver.tech.shhh.confession.R
import vn.dihaver.tech.shhh.confession.feature.auth.AuthViewModel
import vn.dihaver.tech.shhh.confession.feature.auth.model.AuthContext
import vn.dihaver.tech.shhh.confession.feature.auth.model.MsgError
import vn.dihaver.tech.shhh.confession.feature.auth.model.MsgError.*
import vn.dihaver.tech.shhh.confession.feature.auth.model.ResultLogin
import vn.dihaver.tech.shhh.confession.feature.auth.model.TypeLogin


@HiltViewModel(assistedFactory = StartViewModel.Factory::class)
class StartViewModel @AssistedInject constructor(
    @Assisted private val authViewModel: AuthViewModel
) : ViewModel() {

    companion object {
        private const val TAG = "AAA-StartViewModel"
    }

    val authContext: StateFlow<AuthContext> = authViewModel.authContext

    var isLoading = MutableStateFlow(false)
        private set

    var resultSuccess = MutableStateFlow<TypeLogin?>(null)
        private set

    var resultError = MutableStateFlow<String?>(null)
        private set

    init {
        viewModelScope.launch {
            authViewModel.authState
                .filter { state ->
                    state is ResultLogin.Idle ||
                            (state is ResultLogin.Loading && state.typeLogin == TypeLogin.SIGN_IN_WITH_GOOGLE) ||
                            (state is ResultLogin.Success && state.typeLogin == TypeLogin.SIGN_IN_WITH_GOOGLE) ||
                            (state is ResultLogin.Error && state.typeLogin == TypeLogin.SIGN_IN_WITH_GOOGLE)
                }
                .collect { state ->
                    when (state) {
                        ResultLogin.Idle -> {
                            stateIdle()
                        }

                        is ResultLogin.Loading -> {
                            updateState(loading = true)
                        }

                        is ResultLogin.Error -> {
                            updateState(
                                error = handleError(
                                    state.typeLogin,
                                    state.msgError,
                                    state.message
                                )
                            )
                        }

                        is ResultLogin.Success -> {
                            authViewModel.updateAuthContext(AuthContext.Google)
                            updateState(success = TypeLogin.SIGN_IN_WITH_GOOGLE)
                        }
                    }
                }
        }
    }

    fun onEmailMethod(onNext: () -> Unit) {
        authViewModel.updateAuthContext(AuthContext.Email.None)
        onNext()
    }

    fun onGoogleMethod(context: Context) {
        updateState(loading = true)

        viewModelScope.launch {
            val webClientId = context.getString(R.string.app_web_client_id)
            val googleIdOption = GetGoogleIdOption.Builder()
                .setServerClientId(webClientId)
                .setFilterByAuthorizedAccounts(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val cancellationSignal = CancellationSignal()
            CredentialManager.create(context).getCredentialAsync(
                context = context,
                request = request,
                cancellationSignal = cancellationSignal,
                executor = ContextCompat.getMainExecutor(context),
                callback = object :
                    CredentialManagerCallback<GetCredentialResponse, GetCredentialException> {
                    override fun onResult(result: GetCredentialResponse) {
                        val credential = result.credential
                        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                            val googleIdTokenCredential =
                                GoogleIdTokenCredential.createFrom(credential.data)
                            authViewModel.signInWithGoogle(googleIdTokenCredential.idToken)
                        } else {
                            updateState(error = "Không thể khởi tạo xác thực Google")
                        }
                    }

                    override fun onError(e: GetCredentialException) {
                        if (e is GetCredentialCancellationException) {
                            stateIdle()
                        } else {
                            updateState(error = "Không thể khởi tạo xác thực Google")
                        }
                    }
                }
            )
        }
    }

    fun stateIdle() {
        updateState()
    }

    private fun updateState(
        loading: Boolean = false,
        success: TypeLogin? = null,
        error: String? = null
    ) {
        isLoading.value = loading
        resultSuccess.value = success
        resultError.value = error
    }

    private fun handleError(
        type: TypeLogin,
        msgError: MsgError,
        message: String
    ): String? {
        return when (type) {
            TypeLogin.SIGN_IN_WITH_GOOGLE -> when (msgError) {
                ERROR_EMAIL_ALREADY_IN_USE, ERROR_EMAIL_NOT_VERIFY, ERROR_INVALID_CREDENTIAL, ERROR_USER_NOT_FOUND -> null
                ERROR_USER_DISABLED -> "Tài khoản này đã bị vô hiệu hóa. Vui lòng liên hệ với nhà phát hành"
                ERROR_NETWORK -> "Không có kết nối Internet. Vui lòng kiểm tra và thử lại"
                ERROR_UNCERTAIN -> {
                    Log.e(TAG, message)
                    "Có gì đó không ổn. Vui lòng thử đăng nhập lại"
                }
            }

            else -> {
                "Có gì đó không ổn. Vui lòng thử đăng nhập lại"
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(authViewModel: AuthViewModel): StartViewModel
    }
}