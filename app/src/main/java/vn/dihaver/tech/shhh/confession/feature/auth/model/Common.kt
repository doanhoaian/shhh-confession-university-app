package vn.dihaver.tech.shhh.confession.feature.auth.model


enum class OtpPurpose {
    VERIFY_EMAIL,
    RESET_PASSWORD
}

sealed class ResultLogin {
    data object Idle : ResultLogin()
    data class Loading(val typeLogin: TypeLogin) : ResultLogin()
    data class Success(val typeLogin: TypeLogin, val message: String = "") : ResultLogin()
    data class Error(val typeLogin: TypeLogin, val msgError: MsgError, val message: String = "") : ResultLogin()
}

enum class TypeLogin {
    SIGN_UP_WITH_MAIL,
    SIGN_IN_WITH_MAIL,
    SIGN_IN_WITH_GOOGLE
}

enum class MsgError {
    ERROR_EMAIL_ALREADY_IN_USE,
    ERROR_EMAIL_NOT_VERIFY,
    ERROR_INVALID_CREDENTIAL,
    ERROR_USER_NOT_FOUND,
    ERROR_USER_DISABLED,
    ERROR_NETWORK,
    ERROR_UNCERTAIN
}

enum class AuthStep {
    START, INPUT_EMAIL, INPUT_PASSWORD, INPUT_OTP, CREATE_PASSWORD, SELECT_ALIAS, SELECT_SCHOOL, CONFIRM_INFO, COMPLETED
}

sealed interface AuthContext {
    data object None : AuthContext
    data object Google : AuthContext
    sealed class Email : AuthContext {
        data object None : Email()
        data object Login : Email()
        data object Register : Email()
        data object ForgetPassword : Email()
    }
}

fun String.parseAuthContext(): AuthContext {
    return when (this) {
        "Email.None" -> AuthContext.Email.None
        "Email.Login" -> AuthContext.Email.Login
        "Email.Register" -> AuthContext.Email.Register
        "Email.ForgetPassword" -> AuthContext.Email.ForgetPassword
        "Google" -> AuthContext.Google
        "None" -> AuthContext.None
        else -> AuthContext.None
    }
}

fun AuthContext.serializeAuthContext(): String {
    return when (this) {
        is AuthContext.Email.None -> "Email.None"
        is AuthContext.Email.Login -> "Email.Login"
        is AuthContext.Email.Register -> "Email.Register"
        is AuthContext.Email.ForgetPassword -> "Email.ForgetPassword"
        is AuthContext.Google -> "Google"
        is AuthContext.None -> "None"
    }
}

