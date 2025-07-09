package vn.dihaver.tech.shhh.confession.core.ui.state

data class PagingState<T>(
    val items: List<T> = emptyList(),

    val isInitialLoading: Boolean = false,
    val isPullRefreshing: Boolean = false,
    val isAppending: Boolean = false,

    val initialError: Throwable? = null,
    val appendError: Throwable? = null,

    val isPrepending: Boolean = false,
    val prependError: Throwable? = null,
)
