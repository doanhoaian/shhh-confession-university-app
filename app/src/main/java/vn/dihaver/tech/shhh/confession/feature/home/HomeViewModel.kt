package vn.dihaver.tech.shhh.confession.feature.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import vn.dihaver.tech.shhh.confession.core.data.local.datastore.SessionManager
import vn.dihaver.tech.shhh.confession.core.domain.auth.model.UserSession
import vn.dihaver.tech.shhh.confession.core.domain.home.model.FeedItem
import vn.dihaver.tech.shhh.confession.core.domain.home.usecase.GetFeedUseCase
import vn.dihaver.tech.shhh.confession.core.util.distinctUntilChangedBy
import vn.dihaver.tech.shhh.confession.feature.home.data.mapper.toUiModel
import vn.dihaver.tech.shhh.confession.feature.home.ui.model.PostUiModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFeedUseCase: GetFeedUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _selectedTopic = MutableStateFlow<String?>("all")
    val selectedTopic = _selectedTopic.asStateFlow()

    private val _userSession = MutableStateFlow<UserSession?>(null)
    val userSession: StateFlow<UserSession?> = _userSession.asStateFlow()

    var dynamiteHint by mutableStateOf("")
        private set

    init {
        dynamiteHint = getDynamicHint()

        viewModelScope.launch {
            sessionManager.userSession.collect { session ->
                _userSession.value = session
            }
        }
    }

    /**
     * Một [Flow] của [PagingData] biểu diễn nguồn cấp dữ liệu được phân trang của các bài đăng.
     *
     * Luồng này quan sát `_selectedTopic` và lấy nguồn cấp dữ liệu mới bất cứ khi nào chủ đề thay đổi.
     * Nó sử dụng `getFeedUseCase` để lấy dữ liệu nguồn cấp dữ liệu, sau đó được chuyển đổi thành
     * các phiên bản `PostUiModel` phù hợp để hiển thị trong UI.
     *
     * Toán tử `cachedIn(viewModelScope)` đảm bảo rằng PagingData được lưu trong bộ nhớ đệm trong
     * phạm vi của ViewModel, ngăn ngừa mất dữ liệu trong quá trình thay đổi cấu hình (ví dụ: xray màn hình)
     * và cho phép tải và hiển thị dữ liệu hiệu quả.
     *
     * Khi `_selectedTopic` phát ra giá trị mới:
     * 1. `flatMapLatest` hủy mọi hoạt động lấy nguồn cấp dữ liệu trước đó.
     * 2. `getFeedUseCase` được gọi với `schoolId` hiện tại và `topicValue`.
     * - Nếu `topicValue` là "all", nó được coi là null (có nghĩa là không có bộ lọc chủ đề cụ thể).
     * 3. `PagingData<FeedItem>` kết quả được chuyển đổi:
     * - Mỗi `FeedItem` được ánh xạ tới `PostUiModel` bằng hàm mở rộng `toUiModel()`.
     * 4. `PagingData<PostUiModel>` đã chuyển đổi được phát ra bởi luồng này.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val feedPagingFlow: Flow<PagingData<PostUiModel>> = _selectedTopic
        .flatMapLatest { topicValue ->
            getFeedUseCase(
                topicValue = if (topicValue == "all") null else topicValue
            ).map { pagingData: PagingData<FeedItem> ->
                pagingData.map { feedItem ->
                    feedItem.toUiModel()
                }
            }
        }
        .distinctUntilChangedBy { postUiModel -> postUiModel.id }
        .cachedIn(viewModelScope)

    /**
     * Xử lý việc lựa chọn một danh mục.
     *
     * Nếu chủ đề mới được chọn khác với chủ đề đang được chọn,
     * nó cập nhật LiveData `_selectedTopic` với giá trị chủ đề mới.
     *
     * @param topicValue Giá trị của danh mục đã được chọn.
     */
    fun onCategorySelected(topicValue: String) {
        if (_selectedTopic.value != topicValue) {
            _selectedTopic.value = topicValue
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