package vn.dihaver.tech.shhh.confession.feature.home.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import vn.dihaver.tech.shhh.confession.feature.home.data.local.entity.PostEntity

@Dao
interface PostDao {

    /**
     * Chèn hoặc cập nhật một danh sách các bài viết.
     * Nếu post đã tồn tại, nó sẽ được thay thế bằng thông tin mới.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPosts(posts: List<PostEntity>)

    /**
     * Lấy thông tin một bài viết cụ thể bằng ID.
     * Hữu ích cho màn hình chi tiết bài viết.
     */
    @Query("SELECT * FROM posts WHERE id = :postId LIMIT 1")
    suspend fun getPostById(postId: String): PostEntity?

    /**
     * Xóa tất cả các bài viết.
     * Có thể cần cho việc bảo trì hoặc reset toàn bộ cache.
     */
    @Query("DELETE FROM posts")
    suspend fun clearAllPosts()
}