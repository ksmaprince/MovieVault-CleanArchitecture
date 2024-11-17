package com.khun.movievault.di

import com.khun.movievault.data.remote.helpers.MovieServiceHelper
import com.khun.movievault.data.remote.helpers.MovieServiceHelperImpl
import com.khun.movievault.data.remote.helpers.ProfileServiceHelper
import com.khun.movievault.data.remote.helpers.ProfileServiceHelperImpl
import com.khun.movievault.data.remote.helpers.UserServiceHelper
import com.khun.movievault.data.remote.helpers.UserServiceHelperImpl
import com.khun.movievault.data.remote.services.MovieService
import com.khun.movievault.data.remote.services.ProfileService
import com.khun.movievault.data.remote.services.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object HelperModule {
    @Provides
    fun provideMovieServiceHelper(movieService: MovieService): MovieServiceHelper =
        MovieServiceHelperImpl(movieService)

    @Provides
    fun provideProvideServiceHelper(profileService: ProfileService): ProfileServiceHelper =
        ProfileServiceHelperImpl(profileService)

    @Provides
    fun provideUserServiceHelper(userService: UserService): UserServiceHelper =
        UserServiceHelperImpl(userService)
}