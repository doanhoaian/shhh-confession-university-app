package vn.dihaver.tech.shhh.confession.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import vn.dihaver.tech.shhh.confession.core.data.local.database.AppDatabase
import vn.dihaver.tech.shhh.confession.feature.auth.data.local.AliasDao
import vn.dihaver.tech.shhh.confession.feature.auth.data.local.SchoolDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideAliasDao(database: AppDatabase): AliasDao {
        return database.aliasDao()
    }

    @Provides
    fun provideSchoolDao(database: AppDatabase): SchoolDao {
        return database.schoolDao()
    }
}