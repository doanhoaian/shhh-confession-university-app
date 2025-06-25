package vn.dihaver.tech.shhh.confession.core.util

import androidx.paging.PagingData
import androidx.paging.filter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Một toán tử stateful để lọc các item đã xuất hiện trong PagingData.
 * Nó duy trì một tập hợp các ID đã thấy và chỉ cho phép các item có ID mới đi qua.
 *
 * @param idSelector một lambda để trích xuất ID định danh từ mỗi item.
 * @return một Flow<PagingData> mới đã được lọc bỏ các item trùng lặp.
 */
fun <T : Any> Flow<PagingData<T>>.distinctUntilChangedBy(idSelector: (T) -> Any): Flow<PagingData<T>> {
    // Set này sẽ lưu lại các ID đã được hiển thị.
    val seenIds = mutableSetOf<Any>()

    // Dùng toán tử map trên Flow
    return this.map { pagingData ->
        // và toán tử filter trên PagingData bên trong
        pagingData.filter { item ->
            val id = idSelector(item)
            // .add() trả về true nếu ID chưa có trong set (và được thêm vào thành công)
            // và trả về false nếu ID đã tồn tại.
            seenIds.add(id)
        }
    }
}