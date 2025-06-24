package vn.dihaver.tech.shhh.confession.core.data.repository

import android.util.Log
import okio.Buffer
import retrofit2.Response
import vn.dihaver.tech.shhh.confession.core.data.remote.BaseResponse
import vn.dihaver.tech.shhh.confession.core.util.ApiException
import java.io.IOException

open class BaseRepository {

    companion object {
        private const val TAG = "AAA-BaseRepository"
    }

    /**
     * Gọi API và trả về kết quả có thể nullable.
     * Dành cho các API không yêu cầu data bắt buộc (vd: 201, 204).
     */
    suspend fun <T : Any> safeApiCallNullable(
        successCodes: Set<Int> = setOf(200, 201, 204),
        apiCall: suspend () -> Response<BaseResponse<T>>
    ): T? {
        return try {
            val response = apiCall()
            logResponse(response)

            val body = response.body() ?: throw ApiException(response.code(), "Response body is null")

            if (body.code !in successCodes) {
                throw ApiException(
                    body.code,
                    body.message
                )
            }

            body.data
        } catch (e: Exception) {
            logError(e)
            throw e
        }
    }

    /**
     * Gọi API và đảm bảo luôn trả về non-null data.
     * Dành cho các API bắt buộc có dữ liệu (vd: 200 OK).
     */
    suspend fun <T : Any> safeApiCall(
        successCodes: Set<Int> = setOf(200, 201),
        apiCall: suspend () -> Response<BaseResponse<T>>
    ): T {
        return try {
            val response = apiCall()
            logResponse(response)

            val body = response.body() ?: throw ApiException(response.code(), "Response body is null")

            if (body.code !in successCodes) {
                throw ApiException(
                    body.code,
                    body.message
                )
            }

            body.data ?: throw ApiException(
                body.code,
                body.message
            )
        } catch (e: Exception) {
            logError(e)
            throw e
        }
    }

    /**
     * Log lại thông tin response từ API dưới dạng JSON thô.
     */
    private fun <T : Any> logResponse(response: Response<BaseResponse<T>>) {
        Log.d(TAG, "URL: ${response.raw().request.url}")
        Log.d(TAG, "HTTP Code: ${response.code()}")

        try {
            // Lấy JSON thô từ raw response body
            val rawBody = response.raw().body
            val jsonString = rawBody?.let {
                // Clone body để tránh đóng stream
                val buffer = Buffer()
                it.source().readAll(buffer)
                buffer.readUtf8()
            } ?: "No response body"

            Log.d(TAG, "Raw JSON Body: $jsonString")
        } catch (e: Exception) {
            Log.e(TAG, "Error logging raw JSON: ${e.message}")
            // Fallback: Log body đã parse nếu có
            Log.d(TAG, "Parsed Body: ${response.body()?.toString()}")
        }
    }

    /**
     * Log lỗi để debug.
     */
    private fun logError(e: Exception) {
        when (e) {
            is ApiException -> Log.e(TAG, "ApiException: ${e.code} - ${e.message}")
            is IOException -> Log.e(TAG, "IOException: ${e.message}")
            else -> Log.e(TAG, "Unexpected error: ${e.message}")
        }
    }
}