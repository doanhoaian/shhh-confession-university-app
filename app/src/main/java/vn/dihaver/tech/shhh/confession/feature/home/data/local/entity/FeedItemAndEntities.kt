package vn.dihaver.tech.shhh.confession.feature.home.data.local.entity

import androidx.room.Embedded
import vn.dihaver.tech.shhh.confession.core.domain.model.FeedItem
import vn.dihaver.tech.shhh.confession.feature.home.data.mapper.toDomain

/**
 * Lớp dữ liệu trung gian để giữ kết quả của câu lệnh JOIN
 * giữa PostEntity và InteractionEntity.
 *
 * @param post Dữ liệu từ bảng 'posts', được nhúng trực tiếp.
 * @param interaction Dữ liệu từ bảng 'user_interactions'.
 * Phải là nullable (?) vì chúng ta dùng LEFT JOIN.
 * Nó sẽ là null nếu người dùng hiện tại chưa có tương tác với bài viết này.
 */
data class FeedItemAndEntities(
    @Embedded(prefix = "post_")
    val post: PostEntity,

    @Embedded(prefix = "interaction_")
    val interaction: InteractionEntity?
) {
    /**
     * Hàm ánh xạ để chuyển đổi từ lớp dữ liệu thô của database
     * sang Domain Model sạch sẽ mà các lớp trên sẽ sử dụng.
     */
    fun toDomain(): FeedItem {
        return FeedItem(
            post = post.toDomain(), // Gọi mapper PostEntity -> Post
            userInteraction = interaction?.toDomain() // Gọi mapper InteractionEntity -> UserInteraction
        )
    }
}