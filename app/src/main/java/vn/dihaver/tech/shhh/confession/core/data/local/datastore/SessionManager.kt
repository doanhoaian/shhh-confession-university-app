package vn.dihaver.tech.shhh.confession.core.data.local.datastore

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import vn.dihaver.tech.shhh.confession.core.domain.model.UserSession
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.LoginMethod
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "user_session")

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "AAA-SessionManager"

        private val UID = stringPreferencesKey("uid")
        private val EMAIL = stringPreferencesKey("email")
        private val STATUS = stringPreferencesKey("status")
        private val LOGIN_METHOD = stringPreferencesKey("login_method")
        private val ALIAS_ID = stringPreferencesKey("alias_id")
        private val ALIAS_INDEX = intPreferencesKey("alias_index")
        private val DISPLAY_NAME = stringPreferencesKey("display_name")
        private val AVATAR_URL = stringPreferencesKey("avatar_url")
        private val SCHOOL_ID = intPreferencesKey("school_id")
        private val SCHOOL_NAME = stringPreferencesKey("school_name")
        private val SCHOOL_SHORT_NAME = stringPreferencesKey("school_short_name")
        private val SCHOOL_LOGO_URL = stringPreferencesKey("school_logo_url")
        private val BANNED_REASON = stringPreferencesKey("banned_reason")
        private val DELETED_REASON = stringPreferencesKey("deleted_reason")
    }

    val userSession: Flow<UserSession?> = context.dataStore.data.map { prefs ->
        if (!prefs.contains(UID)) return@map null

        UserSession(
            uid = prefs[UID] ?: "",
            email = prefs[EMAIL] ?: "",
            status = prefs[STATUS] ?: "",
            loginMethod = LoginMethod.valueOf(prefs[LOGIN_METHOD] ?: LoginMethod.Email.name),
            aliasId = prefs[ALIAS_ID],
            aliasIndex = prefs[ALIAS_INDEX],
            displayName = prefs[DISPLAY_NAME],
            avatarUrl = prefs[AVATAR_URL],
            schoolId = prefs[SCHOOL_ID],
            schoolName = prefs[SCHOOL_NAME],
            schoolShortName = prefs[SCHOOL_SHORT_NAME],
            schoolLogoUrl = prefs[SCHOOL_LOGO_URL],
            bannedReason = prefs[BANNED_REASON],
            deletedReason = prefs[DELETED_REASON]
        )
    }

    suspend fun saveUserSession(session: UserSession) {
        Log.d(TAG, "saveUserSession: Bắt đầu lưu session cho email ${session.email}")
        try {
            context.dataStore.edit { prefs ->
                Log.d(TAG, "saveUserSession: Đang ghi dữ liệu vào DataStore")
                prefs[UID] = session.uid
                prefs[EMAIL] = session.email
                prefs[STATUS] = session.status
                prefs[LOGIN_METHOD] = session.loginMethod.name
                prefs[ALIAS_ID] = session.aliasId ?: ""
                session.aliasIndex?.let { prefs[ALIAS_INDEX] = it }
                prefs[DISPLAY_NAME] = session.displayName ?: ""
                prefs[AVATAR_URL] = session.avatarUrl ?: ""
                session.schoolId?.let { prefs[SCHOOL_ID] = it }
                prefs[SCHOOL_NAME] = session.schoolName ?: ""
                prefs[SCHOOL_SHORT_NAME] = session.schoolShortName ?: ""
                prefs[SCHOOL_LOGO_URL] = session.schoolLogoUrl ?: ""
                prefs[BANNED_REASON] = session.bannedReason ?: ""
                prefs[DELETED_REASON] = session.deletedReason ?: ""
                Log.d(TAG, "saveUserSession: Hoàn tất ghi dữ liệu vào DataStore")
            }
            Log.d(TAG, "saveUserSession: Lưu session thành công cho email ${session.email}")
        } catch (e: Exception) {
            Log.e(TAG, "saveUserSession: Lỗi khi lưu session: ${e.message}", e)
            throw e // Ném lại ngoại lệ để xử lý ở tầng gọi
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun isLoggedIn(): Boolean {
        return try {
            val snapshot = context.dataStore.data.first()
                    !snapshot[UID].isNullOrBlank() &&
                    !snapshot[ALIAS_ID].isNullOrBlank() &&
                    snapshot[SCHOOL_ID] != null
        } catch (e: Exception) {
            false
        }
    }

    suspend fun saveLoginInfo(
        uid: String,
        email: String,
        status: String,
        loginMethod: LoginMethod
    ) {
        context.dataStore.edit { prefs ->
            prefs[UID] = uid
            prefs[EMAIL] = email
            prefs[STATUS] = status
            prefs[LOGIN_METHOD] = loginMethod.name
        }
    }

    suspend fun saveAliasInfo(
        aliasId: String,
        aliasIndex: Int?,
        displayName: String?,
        avatarUrl: String?
    ) {
        context.dataStore.edit { prefs ->
            prefs[ALIAS_ID] = aliasId
            prefs[ALIAS_INDEX] = aliasIndex ?: 1
            prefs[DISPLAY_NAME] = displayName ?: ""
            prefs[AVATAR_URL] = avatarUrl ?: ""
        }
    }

    suspend fun saveSchoolInfo(
        schoolId: Int,
        schoolName: String,
        schoolShortName: String,
        schoolLogoUrl: String?
    ) {
        context.dataStore.edit { prefs ->
            prefs[SCHOOL_ID] = schoolId
            prefs[SCHOOL_NAME] = schoolName
            prefs[SCHOOL_SHORT_NAME] = schoolShortName
            prefs[SCHOOL_LOGO_URL] = schoolLogoUrl ?: ""
        }
    }
}