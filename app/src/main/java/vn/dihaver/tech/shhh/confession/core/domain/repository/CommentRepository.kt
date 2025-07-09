package vn.dihaver.tech.shhh.confession.core.domain.repository

import vn.dihaver.tech.shhh.confession.core.domain.model.Comment

interface CommentRepository {
    /**
     * Tạo một bình luận mới cho một bài đăng.
     *
     * @param postId ID của bài đăng cần bình luận.
     * @param userId ID của người dùng tạo bình luận.
     * @param content Nội dung của bình luận.
     * @param parentCommentId (Tùy chọn) ID của bình luận cha nếu đây là một bình luận trả lời.
     * @return Đối tượng Comment vừa được tạo hoặc null nếu có lỗi.
     */
    suspend fun createComment(postId: String, userId: String, content: String, parentCommentId: String? = null): Comment?

    /**
     * Lấy danh sách các bình luận cho một bài đăng cụ thể.
     * Hỗ trợ phân trang.
     *
     * @param postId ID của bài đăng.
     * @param page Số trang hiện tại (mặc định là 1).
     * @param pageSize Số lượng bình luận trên mỗi trang (mặc định là 10).
     * @return Danh sách các Comment hoặc danh sách trống nếu không có bình luận nào hoặc có lỗi.
     */
    suspend fun getCommentsByPostId(postId: String, page: Int = 1, pageSize: Int = 10): List<Comment>

    /**
     * Cập nhật nội dung của một bình luận đã tồn tại.
     *
     * @param commentId ID của bình luận cần cập nhật.
     * @param userId ID của người dùng thực hiện cập nhật (để kiểm tra quyền).
     * @param newContent Nội dung mới của bình luận.
     * @return Đối tượng Comment đã được cập nhật hoặc null nếu không tìm thấy bình luận hoặc không có quyền.
     */
    suspend fun updateComment(commentId: String, userId: String, newContent: String): Comment?

    /**
     * Xóa một bình luận.
     *
     * @param commentId ID của bình luận cần xóa.
     * @param userId ID của người dùng thực hiện xóa (để kiểm tra quyền).
     * @return True nếu xóa thành công, False nếu ngược lại.
     */
    suspend fun deleteComment(commentId: String, userId: String): Boolean

    /**
     * Lấy thông tin chi tiết của một bình luận dựa trên ID.
     *
     * @param commentId ID của bình luận.
     * @return Đối tượng Comment hoặc null nếu không tìm thấy.
     */
    suspend fun getCommentById(commentId: String): Comment?

    /**
     * Đếm số lượng bình luận cho một bài đăng.
     *
     * @param postId ID của bài đăng.
     * @return Số lượng bình luận.
     */
    suspend fun countCommentsByPostId(postId: String): Long

    /**
     * Lấy danh sách các bình luận trả lời cho một bình luận cha.
     * Hỗ trợ phân trang.
     *
     * @param parentCommentId ID của bình luận cha.
     * @param page Số trang hiện tại (mặc định là 1).
     * @param pageSize Số lượng bình luận trên mỗi trang (mặc định là 10).
     * @return Danh sách các Comment trả lời hoặc danh sách trống nếu không có.
     */
    suspend fun getRepliesByCommentId(parentCommentId: String, page: Int = 1, pageSize: Int = 10): List<Comment>
}