package com.khun.movievault.di

import com.khun.movievault.data.local.PreferencesManager
import com.khun.movievault.data.remote.helpers.MovieServiceHelper
import com.khun.movievault.data.remote.helpers.ProfileServiceHelper
import com.khun.movievault.data.remote.helpers.UserServiceHelper
import com.khun.movievault.data.repositories.MovieRepositoryImpl
import com.khun.movievault.data.repositories.ProfileRepositoryImpl
import com.khun.movievault.data.repositories.UserRepositoryImpl
import com.khun.movievault.domain.repositories.MovieRepository
import com.khun.movievault.domain.repositories.ProfileRepository
import com.khun.movievault.domain.repositories.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    fun provideMovieRepository(movieServiceHelper: MovieServiceHelper): MovieRepository =
        MovieRepositoryImpl(movieServiceHelper)

    @Provides
    fun provideProfileRepository(profileServiceHelper: ProfileServiceHelper): ProfileRepository =
        ProfileRepositoryImpl(profileServiceHelper)

    @Provides
    fun provideUserRepository(
        preferencesManager: PreferencesManager,
        userServiceHelper: UserServiceHelper
    ): UserRepository =
        UserRepositoryImpl(preferencesManager, userServiceHelper)
}