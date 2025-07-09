package vn.dihaver.tech.shhh.confession.feature.home.ui.state

import vn.dihaver.tech.shhh.confession.core.domain.model.UserSession
import vn.dihaver.tech.shhh.confession.core.ui.state.PagingState
import vn.dihaver.tech.shhh.confession.feature.post.ui.state.PostUiModel

data class HomeUiState(
    val userSession: UserSession,
    val feedState: PagingState<PostUiModel>,
    val dynamiteHint: String
)
