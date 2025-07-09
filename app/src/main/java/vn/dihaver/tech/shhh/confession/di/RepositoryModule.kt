package vn.dihaver.tech.shhh.confession.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.dihaver.tech.shhh.confession.core.domain.repository.AuthRepository
import vn.dihaver.tech.shhh.confession.core.domain.repository.FeedRepository
import vn.dihaver.tech.shhh.confession.core.data.repository.AuthRepositoryImpl
import vn.dihaver.tech.shhh.confession.feature.home.data.repository.FeedRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindFeedRepository(
        impl: FeedRepositoryImpl
    ): FeedRepository

}
