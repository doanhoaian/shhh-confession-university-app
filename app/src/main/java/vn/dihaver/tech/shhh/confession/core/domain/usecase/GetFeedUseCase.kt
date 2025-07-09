package vn.dihaver.tech.shhh.confession.core.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import vn.dihaver.tech.shhh.confession.core.domain.model.FeedItem
import vn.dihaver.tech.shhh.confession.core.domain.repository.FeedRepository
import javax.inject.Inject

class GetFeedUseCase @Inject constructor(
    private val feedRepository: FeedRepository
) {
    operator fun invoke(
        topicValue: String?
    ): Flow<PagingData<FeedItem>> {
        return feedRepository.getFeed(topicValue)
    }
}