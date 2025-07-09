package vn.dihaver.tech.shhh.confession.feature.home.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import vn.dihaver.tech.shhh.confession.feature.home.data.local.entity.FeedItemAndEntities
import vn.dihaver.tech.shhh.confession.feature.home.data.local.entity.FeedRemoteKeyEntity

@Dao
interface FeedRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<FeedRemoteKeyEntity>)

    @Query("SELECT * FROM feed_remote_keys WHERE postId = :postId")
    suspend fun getRemoteKeyByPostId(postId: String): FeedRemoteKeyEntity?

    @Query("SELECT * FROM feed_remote_keys WHERE version = :version ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLatestRemoteKeyForFeed(version: String): FeedRemoteKeyEntity?

    @Query("SELECT postId FROM feed_remote_keys WHERE version = :feedIdentifier")
    suspend fun getPostIdsByFeedIdentifier(feedIdentifier: String): List<String>

    @Query("UPDATE feed_remote_keys SET cachedForUserId = :newUserId, createdAt = :newTimestamp WHERE version = :feedIdentifier")
    suspend fun updateFeedUserAndTimestamp(
        feedIdentifier: String,
        newUserId: String?,
        newTimestamp: Long
    )


    @Query("DELETE FROM feed_remote_keys WHERE version = :version")
    suspend fun clearRemoteKeysByVersion(version: String)

    /**
     * Truy xuất [PagingSource] cho các mục nguồn cấp dữ liệu, được sắp xếp theo thứ tự xuất hiện của chúng trong nguồn cấp dữ liệu.
     *
     * Hàm này xây dựng một truy vấn phức tạp để truy xuất các bài đăng và tương tác của người dùng liên quan.
     * Hàm này liên kết bảng `posts` với `feed_remote_keys` để xác định thứ tự các bài đăng trong nguồn cấp dữ liệu
     * và với `user_interactions` để lấy trạng thái thích/không thích cho người dùng hiện tại.
     *
     * Truy vấn sử dụng các bí danh cho các cột (ví dụ: `p.id AS post_id`) để phân biệt rõ ràng
     * giữa các cột từ bảng `posts` và bảng `user_interactions` khi chúng
     * được ánh xạ tới lớp dữ liệu `FeedItemAndEntities`.
     *
     * Các kết quả được sắp xếp theo `rk.orderInFeed ASC`, đảm bảo rằng PagingSource
     * cung cấp các mục theo đúng trình tự nguồn cấp dữ liệu.
     *
     * @param version Phiên bản của khóa từ xa nguồn cấp dữ liệu để truy vấn. Điều này giúp quản lý
     * các phiên bản hoặc trạng thái khác nhau của nguồn cấp dữ liệu.
     * @param currentUserId ID của người dùng hiện đang đăng nhập. ID này được sử dụng để lấy
     * dữ liệu tương tác cụ thể (thích/không thích) cho người dùng này.
     * Nếu null, sẽ không có dữ liệu tương tác cụ thể nào của người dùng được kết hợp cho trạng thái thích/không thích.
     * @return A [PagingSource] của `Int` (cho khóa trang) và `FeedItemAndEntities` (các mục dữ liệu).
     * `FeedItemAndEntities` được mong đợi là một lớp dữ liệu làm phẳng
     * dữ liệu được kết hợp từ `posts` và `user_interactions`.
     */
    @Transaction
    @Query(
        """
    SELECT 
        p.id AS post_id, 
        p.userId AS post_userId,
        p.schoolId AS post_schoolId,
        p.postType AS post_postType,
        p.avatarUrl AS post_avatarUrl,
        p.displayName AS post_displayName,
        p.schoolShortName AS post_schoolShortName,
        p.content AS post_content,
        p.images AS post_images,
        p.status AS post_status,
        p.commentPermission AS post_commentPermission,
        p.viewPermission AS post_viewPermission,
        p.totalLike AS post_totalLike,
        p.totalDislike AS post_totalDislike,
        p.totalComment AS post_totalComment,
        p.createdAt AS post_createdAt,
        p.updatedAt AS post_updatedAt,
        p.lastUpdated AS post_lastUpdated,

        i.postId AS interaction_postId,
        i.userId AS interaction_userId,
        i.isLiked AS interaction_isLiked,
        i.isDisliked AS interaction_isDisliked

    FROM feed_remote_keys AS rk
    INNER JOIN posts AS p ON rk.postId = p.id
    LEFT JOIN user_interactions AS i ON p.id = i.postId AND i.userId = :currentUserId
    WHERE rk.version = :version
    ORDER BY rk.orderInFeed ASC
    """
    )
    fun pagingSource(
        version: String,
        currentUserId: String?
    ): PagingSource<Int, FeedItemAndEntities>

}