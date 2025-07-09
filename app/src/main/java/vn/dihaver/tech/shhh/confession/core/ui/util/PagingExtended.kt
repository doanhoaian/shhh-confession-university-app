package vn.dihaver.tech.shhh.confession.core.ui.util

import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import vn.dihaver.tech.shhh.confession.core.ui.state.PagingState

fun <T : Any> LazyPagingItems<T>.toPagingUiState(): PagingState<T> {
    val loadState = this.loadState
    val items = itemSnapshotList.items

    val isInitialLoading = loadState.refresh is LoadState.Loading && items.isEmpty()
    val isPullRefreshing = loadState.refresh is LoadState.Loading && items.isNotEmpty()


    return PagingState(
        items = items,

        isInitialLoading = isInitialLoading,
        isPullRefreshing = isPullRefreshing,
        isAppending = loadState.append is LoadState.Loading,

        initialError = (loadState.refresh as? LoadState.Error)?.error,
        appendError = (loadState.append as? LoadState.Error)?.error,

        isPrepending = loadState.prepend is LoadState.Loading,
        prependError = (loadState.prepend as? LoadState.Error)?.error
    )
}
