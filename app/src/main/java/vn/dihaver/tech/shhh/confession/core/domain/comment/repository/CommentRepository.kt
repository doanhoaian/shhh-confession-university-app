package vn.dihaver.tech.shhh.confession.core.domain.comment.repository

interface CommentRepository {
    suspend fun postComment()
}