package vn.dihaver.tech.shhh.confession.core.domain.model

import vn.dihaver.tech.shhh.confession.feature.post.model.UserInteraction

data class FeedItem(
    val post: Post,
    val userInteraction: UserInteraction?
)