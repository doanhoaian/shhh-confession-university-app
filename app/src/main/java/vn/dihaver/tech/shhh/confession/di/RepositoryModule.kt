package vn.dihaver.tech.shhh.confession.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.dihaver.tech.shhh.confession.core.domain.auth.AuthRepository
import vn.dihaver.tech.shhh.confession.feature.auth.data.repository.AuthRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    // Sau này thêm các repository khác:
    // @Binds
    // abstract fun bindPostRepository(
    //     impl: PostRepositoryImpl
    // ): PostRepository
}
