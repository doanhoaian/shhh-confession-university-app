package vn.dihaver.tech.shhh.confession.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.dihaver.tech.shhh.confession.core.data.local.firebase.AuthManager
import vn.dihaver.tech.shhh.confession.core.domain.repository.PostRepository
import vn.dihaver.tech.shhh.confession.feature.post.data.remote.PostService
import vn.dihaver.tech.shhh.confession.feature.post.data.repository.PostRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PostModule {

    @Provides
    @Singleton
    fun providePostRepository(
        application: Application,
        postService: PostService,
        authManager: AuthManager
    ): PostRepository {
        return PostRepositoryImpl(application, postService, authManager)
    }
}