package vn.dihaver.tech.shhh.confession.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import vn.dihaver.tech.shhh.confession.core.data.local.datastore.SessionManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ComponentModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideUserDsm(@ApplicationContext context: Context): SessionManager {
        return SessionManager(context)
    }
}