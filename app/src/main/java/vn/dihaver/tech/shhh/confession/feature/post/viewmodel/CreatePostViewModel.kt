package vn.dihaver.tech.shhh.confession.feature.post.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import vn.dihaver.tech.shhh.confession.feature.post.ui.model.CreatePostNavEvent
import vn.dihaver.tech.shhh.confession.feature.post.ui.model.CreatePostUiState
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CreatePostUiState())
    val uiState = _uiState.asStateFlow()

    private val _navEvent = Channel<CreatePostNavEvent>()
    val navEvent = _navEvent.receiveAsFlow()

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
            val updatedMedia = currentState.selectedMedia.toMutableList().also { it.removeAt(index) }
            currentState.copy(selectedMedia = updatedMedia)
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _navEvent.send(CreatePostNavEvent.NavigateBack)
        }
    }

    fun onPostClicked() {
        // TODO: Xử lý logic đăng bài ở đây (sẽ làm sau)
        // Sau khi đăng bài thành công, chúng ta cũng sẽ điều hướng về
        println("Đăng bài với nội dung: ${_uiState.value.content} và ${uiState.value.selectedMedia.size} ảnh")

        viewModelScope.launch {
            _navEvent.send(CreatePostNavEvent.NavigateBack)
        }
    }

    private fun getDynamicHint(): String {
        val currentHour = LocalDateTime.now().hour
        return when (currentHour) {
            in 5..10 -> "Chào buổi sáng, campus có gì mới không? ☀️"
            in 11..13 -> "Trưa nay ăn gì, ở đâu ngon nhỉ? 🍜"
            in 14..17 -> "Chiều nay học hành có căng thẳng không? Chia sẻ vài dòng nhé."
            in 18..21 -> "Tối rồi, có hoạt động gì hay ho không? 🌙"
            else -> "Cú đêm còn ai thức không? Kể chuyện đêm khuya nào... 🦉"
        }
    }
}