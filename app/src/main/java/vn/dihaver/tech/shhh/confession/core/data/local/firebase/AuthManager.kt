package vn.dihaver.tech.shhh.confession.core.data.local.firebase

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    private val currentUser get() = firebaseAuth.currentUser

    val uid: String? get() = currentUser?.uid

    val email: String? get() = currentUser?.email

    val displayName: String? get() = currentUser?.displayName

    val avatarUrl: String? get() = currentUser?.photoUrl?.toString()

    fun isLoggedIn(): Boolean = currentUser != null

    suspend fun getIdToken(forceRefresh: Boolean = false): String? {
        return currentUser?.getIdToken(forceRefresh)?.await()?.token
    }

    suspend fun getBearerToken(forceRefresh: Boolean = false): String? {
        return getIdToken(forceRefresh)?.let { "Bearer $it" }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    suspend fun refreshToken(): String? {
        return getIdToken(true)
    }

    fun getUserDebugInfo(): String {
        return "UID: ${uid ?: "N/A"}, Email: ${email ?: "N/A"}"
    }
}
