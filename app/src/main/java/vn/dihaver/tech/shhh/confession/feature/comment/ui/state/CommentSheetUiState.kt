package vn.dihaver.tech.shhh.confession.feature.comment.ui.state

import vn.dihaver.tech.shhh.confession.core.domain.model.UserSession
import vn.dihaver.tech.shhh.confession.core.ui.state.PagingState

data class CommentSheetUiState(
    val userSession: UserSession = UserSession.GUEST,
    val commentState: PagingState<CommentUiModel> = PagingState()
)
