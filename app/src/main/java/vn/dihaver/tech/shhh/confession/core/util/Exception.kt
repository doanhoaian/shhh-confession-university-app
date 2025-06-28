package vn.dihaver.tech.shhh.confession.core.util

class ApiException(val code: Int?, message: String?) : Exception("$message")
class NetworkException(message: String) : Exception(message)
class UnauthorizedException(message: String) : Exception(message)
class UnexpectedNullException(message: String? = "Unexpected null value encountered") : Exception(message)