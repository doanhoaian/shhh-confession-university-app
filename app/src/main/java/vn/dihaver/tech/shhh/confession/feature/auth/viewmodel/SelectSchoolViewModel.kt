package vn.dihaver.tech.shhh.confession.feature.auth.viewmodel

import android.content.Context
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
import kotlinx.coroutines.launch
import vn.dihaver.tech.shhh.confession.core.domain.auth.model.School
import vn.dihaver.tech.shhh.confession.core.domain.auth.usecase.GetAllSchoolsUseCase
import vn.dihaver.tech.shhh.confession.core.domain.auth.usecase.UpdateSchoolUseCase
import vn.dihaver.tech.shhh.confession.core.util.ApiException
import vn.dihaver.tech.shhh.confession.core.util.SystemUtils.isNetworkAvailable
import vn.dihaver.tech.shhh.confession.feature.auth.AuthViewModel
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.UpdateSchoolRequest
import vn.dihaver.tech.shhh.confession.feature.auth.viewmodel.SelectAliasViewModel.FetchErrorType
import java.io.IOException

@HiltViewModel(assistedFactory = SelectSchoolViewModel.Factory::class)
class SelectSchoolViewModel @AssistedInject constructor(
    private val getAllSchoolsUseCase: GetAllSchoolsUseCase,
    private val updateSchoolUseCase: UpdateSchoolUseCase,
    @Assisted private val authViewModel: AuthViewModel
) : ViewModel() {

    companion object {
        private const val TAG = "AAA-SelectSchoolViewModel"
    }

    var schools by mutableStateOf<List<School>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var dataLoading by mutableStateOf(false)
        private set

    var fetchError by mutableStateOf<FetchErrorType?>(null)
        private set

    var updateError by mutableStateOf<String?>(null)
        private set

    enum class FetchErrorType {
        INTERNET, UNKNOWN
    }

    private var fetchJob: Job? = null
    private var updateJob: Job? = null

    fun clearUpdateError() {
        updateError = null
    }

    fun retryFetch(context: Context) {
        fetchSchools(context)
    }

    fun fetchSchools(context: Context) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            dataLoading = true
            fetchError = null
            updateError = null

            if (!context.isNetworkAvailable()) {
                fetchError = FetchErrorType.INTERNET
                dataLoading = false
                return@launch
            }

            try {
                schools = getAllSchoolsUseCase()
            } catch (e: Exception) {
                fetchError = FetchErrorType.UNKNOWN
            } finally {
                dataLoading = false
            }
        }
    }

    fun updateSchool(school: School, onSuccess: () -> Unit) {
        updateJob?.cancel()
        updateJob = viewModelScope.launch {
            isLoading = true
            fetchError = null
            updateError = null


            val request = UpdateSchoolRequest(
                userId = null, // AuthRepo tự lấy
                schoolId = school.id
            )
            try {
                updateSchoolUseCase.invoke(request)
                authViewModel.updateSessionSchool(school)

                onSuccess()
            } catch (e: ApiException) {
                updateError = when (e.code) {
                    400 -> "Yêu cầu không hợp lệ. Vui lòng thử lại"
                    404 -> "Alias không tồn tại"
                    409 -> "Alias đã được sử dụng"
                    429 -> "Quá nhiều yêu cầu. Vui lòng thử lại sau vài phút"
                    in 500..599 -> "Lỗi máy chủ. Vui lòng thử lại sau"
                    else -> e.message ?: "Có gì đó không ổn. Vui lòng thử lại"
                }
                Log.e(TAG, "ApiException: ${e.code} - ${e.message}")
            } catch (e: IOException) {
                updateError = "Lỗi kết nối mạng. Vui lòng kiểm tra lại"
                Log.e(TAG, "IOException: ${e.message}")
            } catch (e: Exception) {
                updateError = "Có gì đó không ổn. Vui lòng thử lại"
                Log.e(TAG, "Unexpected error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        fetchJob?.cancel()
        updateJob?.cancel()
    }

    @AssistedFactory
    interface Factory {
        fun create(authViewModel: AuthViewModel): SelectSchoolViewModel
    }
}