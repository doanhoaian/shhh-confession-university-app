package vn.dihaver.tech.shhh.confession.feature.home.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import vn.dihaver.tech.shhh.confession.feature.home.data.local.entity.InteractionEntity

@Dao
interface InteractionDao {

    /**
     * Chèn hoặc cập nhật một tương tác của người dùng.
     * Vì khóa chính là (postId, userId), nó sẽ chỉ ảnh hưởng đến tương tác của user đó.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertInteraction(interaction: InteractionEntity)

    /**
     * Lấy trạng thái tương tác của một người dùng cụ thể trên một bài viết cụ thể.
     * Đây là hàm cốt lõi, luôn cần cả postId và userId.
     */
    @Query("SELECT * FROM user_interactions WHERE postId = :postId AND userId = :userId LIMIT 1")
    suspend fun getInteraction(postId: String, userId: String): InteractionEntity?

    /**
     * Xóa TẤT CẢ tương tác của MỘT người dùng.
     * Hữu ích khi người dùng yêu cầu xóa tài khoản.
     * Lưu ý: Chúng ta sẽ không gọi hàm này khi đăng xuất.
     */
    @Query("DELETE FROM user_interactions WHERE userId = :userId")
    suspend fun clearInteractionsForUser(userId: String)
}