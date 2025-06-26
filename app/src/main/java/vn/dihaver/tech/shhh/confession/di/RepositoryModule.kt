package vn.dihaver.tech.shhh.confession.di

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.dihaver.tech.shhh.confession.core.data.local.firebase.AuthManager
import vn.dihaver.tech.shhh.confession.core.domain.auth.AuthRepository
import vn.dihaver.tech.shhh.confession.core.domain.home.repository.FeedRepository
import vn.dihaver.tech.shhh.confession.core.domain.post.PostRepository
import vn.dihaver.tech.shhh.confession.feature.auth.data.repository.AuthRepositoryImpl
import vn.dihaver.tech.shhh.confession.feature.home.data.repository.FeedRepositoryImpl
import vn.dihaver.tech.shhh.confession.feature.post.data.remote.PostService
import vn.dihaver.tech.shhh.confession.feature.post.data.repository.PostRepositoryImpl
import javax.inject.Singleton

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
