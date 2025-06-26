package vn.dihaver.tech.shhh.confession.core.domain.post.usecase

import android.net.Uri
import vn.dihaver.tech.shhh.confession.core.domain.post.PostRepository
import vn.dihaver.tech.shhh.confession.feature.post.data.remote.dto.CreatePostDto
import vn.dihaver.tech.shhh.confession.feature.post.data.remote.dto.CreatePostRequest
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(request: CreatePostRequest, imageUris: List<Uri>): CreatePostDto {
        return postRepository.createPost(request, imageUris)
    }
}