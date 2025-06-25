package vn.dihaver.tech.shhh.confession.feature.post.ui.model

import android.net.Uri

data class CreatePostUiState(
    val content: String = "",
    val selectedMedia: List<Uri> = emptyList(),
    val dynamicHint: String = "Bạn đang nghĩ gì thế?",
    val isPostButtonEnabled: Boolean = false
)

sealed class CreatePostNavEvent {
    data object NavigateBack : CreatePostNavEvent()
}