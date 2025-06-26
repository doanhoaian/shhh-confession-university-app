package vn.dihaver.tech.shhh.confession.feature.post.data.repository

import android.app.Application
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import vn.dihaver.tech.shhh.confession.core.data.local.firebase.AuthManager
import vn.dihaver.tech.shhh.confession.core.data.repository.BaseRepository
import vn.dihaver.tech.shhh.confession.core.domain.post.PostRepository
import vn.dihaver.tech.shhh.confession.core.util.UnauthorizedException
import vn.dihaver.tech.shhh.confession.feature.post.data.remote.PostService
import vn.dihaver.tech.shhh.confession.feature.post.data.remote.dto.CreatePostDto
import vn.dihaver.tech.shhh.confession.feature.post.data.remote.dto.CreatePostRequest
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val application: Application, // Inject Application Context
    private val postService: PostService,
    private val authManager: AuthManager
) : PostRepository, BaseRepository() {
    override suspend fun createPost(request: CreatePostRequest, imageUris: List<Uri>): CreatePostDto {
        val token = authManager.getBearerToken() ?: throw UnauthorizedException("User not logged in")

        // Chuyển đổi các trường text thành RequestBody
        val userId = request.userId.toRequestBody("text/plain".toMediaTypeOrNull())
        val schoolId = request.schoolId.toRequestBody("text/plain".toMediaTypeOrNull())
        val content = request.content.toRequestBody("text/plain".toMediaTypeOrNull())
        val commentPermission = request.commentPermission.toRequestBody("text/plain".toMediaTypeOrNull())
        val viewPermission = request.viewPermission.toRequestBody("text/plain".toMediaTypeOrNull())

        // Chuyển đổi danh sách Uri thành List<MultipartBody.Part>
        val imageParts = imageUris.mapNotNull { uri ->
            try {
                application.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val fileName = getFileName(uri)
                    val file = File(application.cacheDir, fileName)
                    FileOutputStream(file).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                    val requestFile = file.asRequestBody(application.contentResolver.getType(uri)?.toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("images", file.name, requestFile)
                }
            } catch (e: Exception) {
                // Xử lý lỗi nếu không thể đọc file từ Uri
                null
            }
        }

        return safeApiCall(successCodes = setOf(201)) {
            postService.createPost(
                authHeader = token,
                userId = userId,
                schoolId = schoolId,
                content = content,
                commentPermission = commentPermission,
                viewPermission = viewPermission,
                images = imageParts.ifEmpty { null } // Gửi null nếu không có ảnh
            )
        }
    }

    // Hàm tiện ích để lấy tên file từ Uri
    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursors = application.contentResolver.query(uri, null, null, null, null)
            cursors.use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (columnIndex != -1) {
                        result = cursor.getString(columnIndex)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != null && cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result ?: "unknown_file_${System.currentTimeMillis()}"
    }
}