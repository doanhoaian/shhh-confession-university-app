package vn.dihaver.tech.shhh.confession.core.domain.home.model

data class FeedItem(
    val post: Post,
    val userInteraction: UserInteraction?
)