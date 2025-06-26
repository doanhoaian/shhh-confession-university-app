package vn.dihaver.tech.shhh.confession.feature.post.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.withTransaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import vn.dihaver.tech.shhh.confession.core.data.local.database.AppDatabase
import vn.dihaver.tech.shhh.confession.core.data.local.datastore.SessionManager
import vn.dihaver.tech.shhh.confession.core.domain.post.usecase.CreatePostUseCase
import vn.dihaver.tech.shhh.confession.feature.home.data.local.entity.FeedRemoteKeyEntity
import vn.dihaver.tech.shhh.confession.feature.home.data.local.entity.PostEntity
import vn.dihaver.tech.shhh.confession.feature.post.data.remote.dto.CreatePostRequest
import vn.dihaver.tech.shhh.confession.feature.post.ui.model.CreatePostNavEvent
import vn.dihaver.tech.shhh.confession.feature.post.ui.model.CreatePostUiState
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val createPostUseCase: CreatePostUseCase,
    private val sessionManager: SessionManager,
    private val appDatabase: AppDatabase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreatePostUiState())
    val uiState = _uiState.asStateFlow()

    private val _navEvent = Channel<CreatePostNavEvent>()
    val navEvent = _navEvent.receiveAsFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        _uiState.update { it.copy(dynamicHint = getDynamicHint()) }
    }

    fun onContentChange(newContent: String) {
        _uiState.update {
            it.copy(
                content = newContent,
                isPostButtonEnabled = newContent.isNotBlank()
            )
        }
    }

    fun onMediaSelected(uris: List<Uri>) {
        _uiState.update { currentState ->
            val updatedMedia = (currentState.selectedMedia + uris).distinct().take(8)
            currentState.copy(selectedMedia = updatedMedia)
        }
    }

    fun onRemoveMedia(index: Int) {
        _uiState.update { currentState ->
            val updatedMedia =
                currentState.selectedMedia.toMutableList().also { it.removeAt(index) }
            currentState.copy(selectedMedia = updatedMedia)
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navEvent.send(CreatePostNavEvent.NavigateBack)
        }
    }

    fun onPostClicked() {
        viewModelScope.launch {
            val session = sessionManager.userSession.first()
            if (session?.schoolId == null) {
                _errorMessage.value = "Không thể xác thực người dùng"
                return@launch
            }

            _isLoading.value = true
            try {
                val request = CreatePostRequest(
                    userId = session.uid,
                    schoolId = session.schoolId.toString(),
                    content = _uiState.value.content,
                    commentPermission = "all", // Default values
                    viewPermission = "all"   // Default values
                )
                val result = createPostUseCase(request, _uiState.value.selectedMedia)

                // Logic chèn vào DB vẫn giữ nguyên, hình ảnh sẽ được cập nhật khi feed tải lại
                val newPostEntity = PostEntity(
                    id = result.postId,
                    userId = session.uid,
                    schoolId = session.schoolId,
                    postType = "CONFESSION",
                    avatarUrl = session.avatarUrl ?: "",
                    displayName = session.displayName ?: "Anonymous",
                    schoolShortName = session.schoolShortName ?: "",
                    content = _uiState.value.content,
                    images = emptyList(), // API không trả về URL ảnh ngay, nên tạm để trống
                    status = result.status,
                    commentPermission = "allow",
                    viewPermission = "public",
                    totalLike = 0,
                    totalDislike = 0,
                    totalComment = 0,
                    createdAt = Instant.now().toString(),
                    updatedAt = Instant.now().toString(),
                    lastUpdated = System.currentTimeMillis()
                )

                val newRemoteKey = listOf(
                    FeedRemoteKeyEntity(
                        postId = result.postId,
                        cachedForUserId = session.uid,
                        version = "schoolId=${session.schoolId}&topic=",
                        orderInFeed = -1,
                        createdAt = System.currentTimeMillis()
                    )
                )

                appDatabase.withTransaction {
                    appDatabase.postDao().upsertPosts(listOf(newPostEntity))
                    appDatabase.feedRemoteKeyDao().insertAll(newRemoteKey)
                }

                _isLoading.value = false
                _navEvent.send(CreatePostNavEvent.NavigateBack)

            } catch (e: IOException) {
                _isLoading.value = false
                _errorMessage.value = "Lỗi mạng, vui lòng thử lại."
            } catch (e: Exception) {
                _isLoading.value = false
                _errorMessage.value = "Đã có lỗi xảy ra: ${e.message}"
            }
        }
    }

    fun dismissErrorDialog() {
        _errorMessage.value = null
    }

    private fun getDynamicHint(): String {
        val currentHour = LocalDateTime.now().hour
        return when (currentHour) {
            in 5..10 -> "Chào buổi sáng, campus có gì mới không? ☀️"
            in 11..13 -> "Trưa nay ăn gì, ở đâu ngon nhỉ? 🍜"
            in 14..17 -> "Chiều nay học hành có căng thẳng không? Chia sẻ vài dòng nhé."
            else -> "Cú đêm còn ai thức không? Kể chuyện đêm khuya nào... 🦉"
        }
    }
}