package vn.dihaver.tech.shhh.confession.core.domain.home.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import vn.dihaver.tech.shhh.confession.core.domain.home.model.FeedItem
import vn.dihaver.tech.shhh.confession.core.domain.home.repository.FeedRepository
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