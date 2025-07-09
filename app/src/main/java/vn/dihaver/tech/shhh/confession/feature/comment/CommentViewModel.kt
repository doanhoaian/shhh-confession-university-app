package vn.dihaver.tech.shhh.confession.feature.comment

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import vn.dihaver.tech.shhh.confession.core.ui.state.PagingState
import vn.dihaver.tech.shhh.confession.core.util.MockDataProvider
import vn.dihaver.tech.shhh.confession.feature.comment.ui.state.CommentSheetUiState

class CommentViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(CommentSheetUiState())
    val uiState: StateFlow<CommentSheetUiState> = _uiState.asStateFlow()

    init {

        _uiState.value = CommentSheetUiState(commentState = PagingState(
            items = MockDataProvider.generateMockComments(count = 99).map {
                if (it.totalReply > 0) it.copy(canLoadMoreReplies = true) else it
            }
        ))
    }
}