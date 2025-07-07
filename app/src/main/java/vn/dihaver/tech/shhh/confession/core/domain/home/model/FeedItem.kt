package vn.dihaver.tech.shhh.confession.core.domain.home.model

import vn.dihaver.tech.shhh.confession.core.domain.post.model.Post
import vn.dihaver.tech.shhh.confession.core.domain.post.model.UserInteraction

data class FeedItem(
    val post: Post,
    val userInteraction: UserInteraction?
)