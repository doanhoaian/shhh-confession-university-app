package vn.dihaver.tech.shhh.confession.feature.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import vn.dihaver.tech.shhh.confession.core.data.local.datastore.SessionManager
import vn.dihaver.tech.shhh.confession.core.domain.auth.model.Alias
import vn.dihaver.tech.shhh.confession.core.domain.auth.model.School
import vn.dihaver.tech.shhh.confession.core.domain.auth.model.UserSession
import vn.dihaver.tech.shhh.confession.core.domain.auth.usecase.LoginOrRegisterUseCase
import vn.dihaver.tech.shhh.confession.core.util.FormatUtils.hashSHA256
import vn.dihaver.tech.shhh.confession.core.util.SystemUtils
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.LoginMethod
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.LoginRequest
import vn.dihaver.tech.shhh.confession.feature.auth.model.AuthContext
import vn.dihaver.tech.shhh.confession.feature.auth.model.AuthStep
import vn.dihaver.tech.shhh.confession.feature.auth.model.MsgError
import vn.dihaver.tech.shhh.confession.feature.auth.model.ResultLogin
import vn.dihaver.tech.shhh.confession.feature.auth.model.TypeLogin
import vn.dihaver.tech.shhh.confession.feature.auth.model.parseAuthContext
import vn.dihaver.tech.shhh.confession.feature.auth.model.serializeAuthContext
import vn.dihaver.tech.shhh.confession.navigation.NavRoutes
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val sessionManager: SessionManager,
    private val firebaseAuth: FirebaseAuth,
    private val loginOrRegisterUseCase: LoginOrRegisterUseCase
) : ViewModel() {

    companion object {
        private const val TAG = "AAA-AuthViewModel"
    }

    private val _authState = MutableStateFlow<ResultLogin>(ResultLogin.Idle)
    val authState: StateFlow<ResultLogin> = _authState.asStateFlow()

    private val _stepStack = mutableListOf<AuthStep>()

    private val _currentStep = MutableStateFlow(
        savedStateHandle.get<String>("currentStep")?.let { AuthStep.valueOf(it) } ?: AuthStep.START
    )
    val currentStep: StateFlow<AuthStep> = _currentStep

    private val _authContext = MutableStateFlow(
        savedStateHandle.get<String>("authContext")?.parseAuthContext() ?: AuthContext.None
    )
    val authContext: StateFlow<AuthContext> = _authContext

    var email by mutableStateOf("")
        private set

    private val _userSession = MutableStateFlow<UserSession?>(null)
    val userSession: StateFlow<UserSession?> = _userSession.asStateFlow()

    init {
        viewModelScope.launch {
            sessionManager.userSession.collect { session ->
                _userSession.value = session
            }
        }
    }


    fun updateAuthContext(authContext: AuthContext) {
        _authContext.value = authContext
        savedStateHandle["authContext"] = authContext.serializeAuthContext()
    }

    fun updateCurrentStep(step: AuthStep) {
        val current = _currentStep.value
        Log.d(
            TAG,
            "Cập nhật bước: Trước - Bước hiện tại=${current.name}, Stack=${_stepStack.joinToString()}"
        )

        if (current != step) {
            _stepStack.add(current)
            Log.d(TAG, "Cập nhật bước: Đã thêm ${current.name} vào Stack")
        }
        _currentStep.value = step
        savedStateHandle["currentStep"] = step.name
        Log.d(
            TAG,
            "Cập nhật bước: Sau - Bước hiện tại=${step.name}, Stack=${_stepStack.joinToString()}"
        )
    }

    fun popBackStep() {
        Log.d(
            TAG,
            "Quay lại bước trước: Trước - Bước hiện tại=${_currentStep.value.name}, Stack=${_stepStack.joinToString()}"
        )

        if (_stepStack.isNotEmpty()) {
            val previousStep = _stepStack.removeAt(_stepStack.size - 1)
            _currentStep.value = previousStep
            savedStateHandle["currentStep"] = previousStep.name
            Log.d(
                TAG,
                "Quay lại bước trước: Đã lấy ${previousStep.name}, Sau - Bước hiện tại=${_currentStep.value?.name}, Stack=${_stepStack.joinToString()}"
            )
        } else {
            Log.w(TAG, "Quay lại bước trước: Stack rỗng, không thực hiện hành động")
        }
    }

    fun removeStepsUpTo(step: AuthStep, inclusive: Boolean = true, updateCurrent: Boolean = true) {
        Log.d(
            TAG,
            "Xóa đến bước: Trước - Bước hiện tại=${_currentStep.value.name}, Stack=${_stepStack.joinToString()}, Bước mục tiêu=${step.name}, Bao gồm=$inclusive"
        )

        val index = _stepStack.indexOfLast { it == step }
        if (index >= 0) {
            val removeUpTo = if (inclusive) index else index + 1
            Log.d(
                TAG,
                "Xóa đến bước: Tìm thấy ${step.name} tại vị trí $index, sẽ xóa đến vị trí $removeUpTo"
            )

            // Xóa từ cuối stack cho đến khi kích thước stack bằng removeUpTo
            while (_stepStack.size > removeUpTo) {
                val removedStep = _stepStack.removeAt(_stepStack.size - 1)
                Log.d(TAG, "Xóa đến bước: Đã xóa ${removedStep.name}")
            }

            if (updateCurrent && _stepStack.isNotEmpty()) {
                _currentStep.value = _stepStack.last()
                savedStateHandle["currentStep"] = _currentStep.value.name
                Log.d(TAG, "Xóa đến bước: Cập nhật Bước hiện tại thành ${_currentStep.value.name}")
            } else if (updateCurrent) {
                _currentStep.value = AuthStep.START
                savedStateHandle["currentStep"] = AuthStep.START.name
                Log.d(TAG, "Xóa đến bước: Stack rỗng, đặt Bước hiện tại thành START")
            }
        } else {
            Log.w(TAG, "Xóa đến bước: Không tìm thấy ${step.name} trong Stack")
        }

        Log.d(
            TAG,
            "Xóa đến bước: Sau - Bước hiện tại=${_currentStep.value.name}, Stack=${_stepStack.joinToString()}"
        )
    }

    fun updateEmail(newEmail: String) {
        email = newEmail
    }

    suspend fun updateSessionAlias(newAlias: Alias, aliasIndex: Int) {
        sessionManager.saveAliasInfo(
            aliasId = newAlias.id,
            aliasIndex = aliasIndex,
            displayName = newAlias.displayName,
            avatarUrl = newAlias.imageUrl
        )
    }

    suspend fun updateSessionSchool(newSchool: School) {
        sessionManager.saveSchoolInfo(newSchool.id, newSchool.name, newSchool.shortName, newSchool.imageUrl)
    }

    fun signUpWithEmail(email: String, password: String) {
        _authState.value = ResultLogin.Loading(TypeLogin.SIGN_UP_WITH_MAIL)
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userFb = firebaseAuth.currentUser
                    requestLogin(
                        userFb = userFb!!,
                        typeLogin = TypeLogin.SIGN_UP_WITH_MAIL,
                        password = password,
                        method = LoginMethod.Email
                    )
                } else {
                    _authState.value =
                        handleFirebaseAuthException(TypeLogin.SIGN_UP_WITH_MAIL, task.exception)
                }
            }
    }

    fun signInWithEmail(email: String, password: String) {
        _authState.value = ResultLogin.Loading(TypeLogin.SIGN_IN_WITH_MAIL)
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userFb = firebaseAuth.currentUser
                    requestLogin(
                        userFb = userFb!!,
                        typeLogin = TypeLogin.SIGN_IN_WITH_MAIL,
                        password = password,
                        method = LoginMethod.Email
                    )
                } else {
                    _authState.value =
                        handleFirebaseAuthException(TypeLogin.SIGN_IN_WITH_MAIL, task.exception)
                }
            }
    }

    fun signInWithGoogle(idToken: String) {
        _authState.value = ResultLogin.Loading(TypeLogin.SIGN_IN_WITH_GOOGLE)
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userFb = firebaseAuth.currentUser
                    requestLogin(
                        userFb = userFb!!,
                        typeLogin = TypeLogin.SIGN_IN_WITH_GOOGLE,
                        password = "",
                        method = LoginMethod.Google
                    )
                } else {
                    _authState.value =
                        handleFirebaseAuthException(TypeLogin.SIGN_IN_WITH_GOOGLE, task.exception)
                }
            }
    }

    private fun handleFirebaseAuthException(
        typeLogin: TypeLogin,
        exception: Exception?
    ): ResultLogin {
        return when (exception) {
            is FirebaseAuthException -> {
                when (exception.errorCode) {
                    "ERROR_EMAIL_ALREADY_IN_USE" -> ResultLogin.Error(
                        typeLogin,
                        MsgError.ERROR_EMAIL_ALREADY_IN_USE
                    )

                    "ERROR_INVALID_CREDENTIAL" -> ResultLogin.Error(
                        typeLogin,
                        MsgError.ERROR_INVALID_CREDENTIAL
                    )

                    "ERROR_USER_NOT_FOUND" -> ResultLogin.Error(
                        typeLogin,
                        MsgError.ERROR_USER_NOT_FOUND
                    )

                    "ERROR_USER_DISABLED" -> ResultLogin.Error(
                        typeLogin,
                        MsgError.ERROR_USER_DISABLED
                    )

                    else -> ResultLogin.Error(
                        typeLogin,
                        MsgError.ERROR_UNCERTAIN,
                        "Lỗi FirebaseAuth: ${exception.errorCode}"
                    )
                }
            }

            is FirebaseNetworkException -> ResultLogin.Error(typeLogin, MsgError.ERROR_NETWORK)
            else -> ResultLogin.Error(
                typeLogin,
                MsgError.ERROR_UNCERTAIN,
                "Lỗi không xác định: ${exception?.message}"
            )
        }
    }

    private fun requestLogin(
        userFb: FirebaseUser,
        typeLogin: TypeLogin,
        password: String,
        method: LoginMethod
    ) {
        viewModelScope.launch {
            try {
                val userEmail = userFb.email ?: "unknown@gmail.com"
                val loginRequest = LoginRequest(
                    uid = userFb.uid,
                    email = userEmail,
                    passwordHash = password.hashSHA256(),
                    loginMethod = method,
                    deviceInfo = SystemUtils.getDeviceInfo()
                )

                val userSession = loginOrRegisterUseCase.invoke(request = loginRequest)

                sessionManager.saveUserSession(userSession)
                _authState.value = ResultLogin.Success(typeLogin, userEmail)
            } catch (e: Exception) {
                _authState.value = ResultLogin.Error(
                    typeLogin,
                    MsgError.ERROR_UNCERTAIN,
                    "Lỗi đăng nhập: ${e.message}"
                )
            }
        }
    }

    fun getNextRoute(): String {
        val currentStep = _currentStep.value
        val authContext = _authContext.value

        val user = _userSession.value

        when (currentStep) {
            AuthStep.START -> {
                return when (authContext) {
                    is AuthContext.Google,
                    is AuthContext.Email.None -> {
                        return when {
                            user?.uid.isNullOrBlank() -> {
                                updateCurrentStep(AuthStep.INPUT_EMAIL)
                                NavRoutes.INPUT_EMAIL
                            }

                            user?.aliasId.isNullOrBlank() -> {
                                updateCurrentStep(AuthStep.SELECT_ALIAS)
                                NavRoutes.SELECT_ALIAS
                            }

                            user?.schoolId == null -> {
                                updateCurrentStep(AuthStep.SELECT_SCHOOL)
                                NavRoutes.SELECT_SCHOOL
                            }

                            else -> {
                                updateCurrentStep(AuthStep.COMPLETED)
                                NavRoutes.HOME_GRAPH
                            }
                        }
                    }

                    else -> {
                        updateCurrentStep(AuthStep.START)
                        Log.e(
                            TAG,
                            "Lỗi không xác định tại AuthStep.START với AuthContext: $authContext"
                        )
                        NavRoutes.START
                    }
                }
            }

            AuthStep.INPUT_EMAIL -> {
                when (authContext) {
                    is AuthContext.Email.ForgetPassword -> {
                        updateCurrentStep(AuthStep.INPUT_OTP)
                        return NavRoutes.INPUT_OTP
                    }

                    is AuthContext.Email.Login -> {
                        updateCurrentStep(AuthStep.INPUT_PASSWORD)
                        return NavRoutes.INPUT_PASSWORD
                    }

                    is AuthContext.Email.Register -> {
                        updateCurrentStep(AuthStep.INPUT_OTP)
                        return NavRoutes.INPUT_OTP
                    }

                    else -> {
                        updateCurrentStep(AuthStep.START)
                        Log.e(
                            TAG,
                            "Đã xảy ra lỗi ở AuthStep.INPUT_EMAIL với AuthContext lỗi là: $authContext"
                        )
                        return NavRoutes.START
                    }
                }
            }

            AuthStep.INPUT_PASSWORD -> {
                when (authContext) {
                    is AuthContext.Email.Login -> {
                        when {
                            user?.aliasId.isNullOrBlank() -> {
                                updateCurrentStep(AuthStep.SELECT_ALIAS)
                                return NavRoutes.SELECT_ALIAS
                            }

                            user?.schoolId == null -> {
                                updateCurrentStep(AuthStep.SELECT_SCHOOL)
                                return NavRoutes.SELECT_SCHOOL
                            }

                            else -> {
                                updateCurrentStep(AuthStep.COMPLETED)
                                return NavRoutes.HOME_GRAPH
                            }
                        }
                    }

                    is AuthContext.Email.ForgetPassword -> {
                        updateCurrentStep(AuthStep.INPUT_EMAIL)
                        return NavRoutes.INPUT_EMAIL
                    }

                    else -> {
                        updateCurrentStep(AuthStep.START)
                        Log.e(
                            TAG,
                            "Đã xảy ra lỗi ở AuthStep.INPUT_PASSWORD với AuthContext lỗi là: $authContext"
                        )
                        return NavRoutes.START
                    }
                }
            }

            AuthStep.INPUT_OTP -> {
                when (authContext) {
                    is AuthContext.Email.Register, is AuthContext.Email.ForgetPassword -> {
                        updateCurrentStep(AuthStep.CREATE_PASSWORD)
                        return NavRoutes.CREATE_PASSWORD
                    }

                    else -> {
                        updateCurrentStep(AuthStep.START)
                        Log.e(
                            TAG,
                            "Đã xảy ra lỗi ở AuthStep.INPUT_OTP với AuthContext lỗi là: $authContext"
                        )
                        return NavRoutes.START
                    }
                }
            }

            AuthStep.CREATE_PASSWORD -> {
                when (authContext) {
                    is AuthContext.Email.Register -> {
                        when {
                            user?.aliasId.isNullOrBlank() -> {
                                updateCurrentStep(AuthStep.SELECT_ALIAS)
                                return NavRoutes.SELECT_ALIAS
                            }

                            user?.schoolId == null -> {
                                updateCurrentStep(AuthStep.SELECT_SCHOOL)
                                return NavRoutes.SELECT_SCHOOL
                            }

                            else -> {
                                updateCurrentStep(AuthStep.COMPLETED)
                                return NavRoutes.HOME_GRAPH
                            }
                        }
                    }

                    is AuthContext.Email.ForgetPassword -> {
                        return NavRoutes.INPUT_PASSWORD
                    }

                    else -> {
                        updateCurrentStep(AuthStep.START)
                        Log.e(
                            TAG,
                            "Đã xảy ra lỗi ở AuthStep.CREATE_PASSWORD với AuthContext lỗi là: $authContext"
                        )
                        return NavRoutes.START
                    }
                }
            }

            AuthStep.SELECT_ALIAS -> {
                updateCurrentStep(AuthStep.SELECT_SCHOOL)
                return NavRoutes.SELECT_SCHOOL
            }

            AuthStep.SELECT_SCHOOL -> {
                updateCurrentStep(AuthStep.CONFIRM_INFO)
                return NavRoutes.CONFIRM_INFO
            }

            AuthStep.CONFIRM_INFO -> {
                updateCurrentStep(AuthStep.COMPLETED)
                return NavRoutes.HOME_GRAPH
            }

            AuthStep.COMPLETED -> {
                return NavRoutes.HOME_GRAPH
            }
        }
    }

    override fun onCleared() {
        Log.d(TAG, "AuthViewModel cleared")
        super.onCleared()
    }
}