package vn.dihaver.tech.shhh.confession.core.util

import vn.dihaver.tech.shhh.confession.core.domain.model.UserSession
import vn.dihaver.tech.shhh.confession.feature.auth.data.remote.dto.LoginMethod
import vn.dihaver.tech.shhh.confession.feature.comment.ui.state.CommentUiModel
import vn.dihaver.tech.shhh.confession.feature.post.ui.state.PostUiModel
import java.util.UUID
import kotlin.random.Random

object MockDataProvider {

    fun getMockUserSession(): UserSession {
        return UserSession(
            uid = "user_12345",
            email = "jane.doe@example.com",
            status = "active",
            loginMethod = LoginMethod.Email,
            aliasId = "alias_jane",
            aliasIndex = 3,
            displayName = "Jane Doe",
            avatarUrl = "https://i.pravatar.cc/150?img=47",
            schoolId = 42,
            schoolName = "Fictional University",
            schoolShortName = "FU",
            schoolLogoUrl = "https://placehold.co/64x64.png?text=FU",
            bannedReason = null,
            deletedReason = null
        )
    }

    fun generateMockPosts(
        count: Int = 15,
        seed: Int? = null
    ): List<PostUiModel> {
        val random = if (seed != null) Random(seed) else Random.Default

        return List(count) {
            PostUiModel(
                id = "post_${UUID.randomUUID()}",
                authorName = listOf(
                    "Nguyễn Văn A", "Trần Thị B", "Lê C", "Phạm D"
                ).random(random),
                content = listOf(
                    "Hôm nay trời đẹp quá nên tôi viết vài dòng chia sẻ.",
                    "Có ai còn thức không, tâm sự chút đi.",
                    "Tips học tập cực chill cho mọi người.",
                    "Ảnh mới đi chơi nè, mọi người thả tym nhé!",
                    "Confession: Tôi thích bạn cùng lớp."
                ).random(random),
                authorAvatarUrl = "https://i.pravatar.cc/150?img=${random.nextInt(10,70)}",
                schoolName = listOf(
                    "Example University", "Học Viện Meme", "ĐH Ẩn Danh"
                ).random(random),
                images = if (random.nextBoolean()) {
                    listOf("https://picsum.photos/seed/${UUID.randomUUID()}/600/400")
                } else emptyList(),
                timeAgo = "${random.nextInt(1, 23)} giờ trước",
                totalLike = random.nextLong(10, 5000),
                totalDislike = random.nextLong(0, 100),
                totalComment = random.nextLong(0, 300),
                isLiked = random.nextBoolean(),
                isDisliked = false,
                isSaved = false
            )
        }
    }

    fun generateAdditionalMockPosts(
        existingCount: Int,
        count: Int = 5
    ): List<PostUiModel> {
        return List(count) { index ->
            PostUiModel(
                id = "post_${UUID.randomUUID()}",
                authorName = "Tác giả ${existingCount + index + 1}",
                content = "Nội dung mới được tải thêm (load more) số #${existingCount + index + 1}.",
                authorAvatarUrl = "https://i.pravatar.cc/150?img=${(20..70).random()}",
                schoolName = "LoadMore University",
                images = if ((0..1).random() == 0)
                    listOf("https://picsum.photos/seed/${UUID.randomUUID()}/600/400")
                else emptyList(),
                timeAgo = "${(1..10).random()} giờ trước",
                totalLike = (100..5000).random().toLong(),
                totalDislike = (0..50).random().toLong(),
                totalComment = (0..200).random().toLong(),
                isLiked = false,
                isDisliked = false,
                isSaved = false
            )
        }
    }

    fun generateMockComments(
        count: Int = 10,
        seed: Int? = null
    ): List<CommentUiModel> {
        val random = if (seed != null) Random(seed) else Random.Default
        val authorNames = listOf(
            "Anh Chàng Hồi Giáo",
            "Cô Gái Thầm Lặng",
            "Người Vô Danh",
            "Hotboy IT",
            "Trùm Meme",
            "Ẩn Danh"
        )

        return List(count) { index ->
            CommentUiModel(
                id = "comment_${UUID.randomUUID()}",
                authorName = "${authorNames.random(random)} #${random.nextInt(1, 20)}",
                authorAvatarUrl = "https://i.pravatar.cc/150?img=${random.nextInt(10, 70)}",
                schoolName = listOf("UTH", "UIT", "PTIT", "BK", "ĐH Mở").random(random),
                content = listOf(
                    "Tui thấy đúng đó.",
                    "Ủa rồi sao?",
                    "Đồng ý hai tay hai chân luôn.",
                    "Chủ thớt nói đúng quá trời!",
                    "Haha comment này chất!",
                    "Tâm trạng tui y chang vậy luôn á.",
                    "Cho xin cái info bạn ơi.",
                    "@@SYS::vn.dihaver.tech.campus.comments.v1.deleted::DELETED_BY_MODERATOR",
                    "@@SYS::vn.dihaver.tech.campus.comments.v1.deleted::DELETED_BY_SYSTEM",
                    "@@SYS::vn.dihaver.tech.campus.comments.v1.deleted::DELETED_BY_POST_OWNER"
                ).random(random),
                timeAgo = "${random.nextInt(1, 59)} phút",
                isEdited = random.nextBoolean(),
                totalLikes = random.nextLong(0, 50000),
                totalDislikes = random.nextLong(0, 50),
                totalReply = random.nextLong(0, 10),
                isLiked = random.nextBoolean(),
                isDisliked = false,
                isReply = false,
                isPostOwner = random.nextBoolean()
            )
        }
    }

    fun generateMockCommentReplies(
        count: Int = 2,
        seed: Int? = null
    ): List<CommentUiModel> {
        val random = if (seed != null) Random(seed) else Random.Default
        val authorNames = listOf(
            "Anh Chàng Hồi Giáo",
            "Cô Gái Thầm Lặng",
            "Người Vô Danh",
            "Hotboy IT",
            "Trùm Meme",
            "Ẩn Danh #9"
        )

        return List(count) { index ->
            CommentUiModel(
                id = "comment_${UUID.randomUUID()}",
                authorName = "${authorNames.random(random)} #${random.nextInt(1, 20)}",
                authorAvatarUrl = "https://i.pravatar.cc/150?img=${random.nextInt(10, 70)}",
                schoolName = listOf("UTH", "UIT", "PTIT", "BK", "ĐH Mở").random(random),
                content = listOf(
                    "Tui thấy đúng đó.",
                    "Ủa rồi sao?",
                    "Đồng ý hai tay hai chân luôn.",
                    "Chủ thớt nói đúng quá trời!",
                    "Haha comment này chất!",
                    "Tâm trạng tui y chang vậy luôn á.",
                    "Cho xin cái info bạn ơi.",
                ).random(random),
                timeAgo = "${random.nextInt(1, 59)} phút",
                isEdited = random.nextBoolean(),
                totalLikes = random.nextLong(0, 50),
                totalDislikes  = random.nextLong(0, 10),
                totalReply = 0,
                isLiked = random.nextBoolean(),
                isDisliked = false,
                isReply = true
            )
        }
    }

    fun generateAdditionalMockCommentReplies(
        existingCount: Int,
        newCount: Int = 5
    ): List<CommentUiModel> {
        val random = Random.Default
        return List(newCount) { index ->
            CommentUiModel(
                id = "reply_${UUID.randomUUID()}",
                authorName = "Người trả lời thêm #${existingCount + index + 1}",
                authorAvatarUrl = "https://i.pravatar.cc/150?img=${random.nextInt(10, 70)}",
                schoolName = listOf("UTH", "UIT", "PTIT", "BK", "ĐH Mở").random(random),
                content = "Đây là nội dung trả lời được tải thêm (load more) số #${existingCount + index + 1}.",
                timeAgo = "${random.nextInt(1, 59)} phút",
                isEdited = false,
                totalLikes = random.nextLong(0, 50),
                totalDislikes = random.nextLong(0, 10),
                totalReply = 0,
                isLiked = false,
                isDisliked = false,
                isReply = true,
                isExpanded = false
            )
        }
    }
}