package com.khun.movievault.di

import com.khun.movievault.domain.repositories.MovieRepository
import com.khun.movievault.domain.repositories.ProfileRepository
import com.khun.movievault.domain.repositories.UserRepository
import com.khun.movievault.domain.usecases.AddFavouriteMovieUseCase
import com.khun.movievault.domain.usecases.AddFavouriteMovieUseCaseImpl
import com.khun.movievault.domain.usecases.CreateUserUseCase
import com.khun.movievault.domain.usecases.CreateUserUseCaseImpl
import com.khun.movievault.domain.usecases.GetAllFavouriteMovieUseCase
import com.khun.movievault.domain.usecases.GetAllFavouriteMovieUseCaseImpl
import com.khun.movievault.domain.usecases.GetAllMoviesUseCase
import com.khun.movievault.domain.usecases.GetAllMoviesUseCaseImpl
import com.khun.movievault.domain.usecases.GetProfileByIdUseCase
import com.khun.movievault.domain.usecases.GetProfileByIdUseCaseImpl
import com.khun.movievault.domain.usecases.RetrieveLoginUserInfoUseCase
import com.khun.movievault.domain.usecases.RetrieveLoginUserInfoUseCaseImpl
import com.khun.movievault.domain.usecases.UpdatePasswordUseCase
import com.khun.movievault.domain.usecases.UpdatePasswordUseCaseImpl
import com.khun.movievault.domain.usecases.UpdateProfileByIdUseCase
import com.khun.movievault.domain.usecases.UpdateProfileByIdUseCaseImpl
import com.khun.movievault.domain.usecases.UpdateProfileImageUseCase
import com.khun.movievault.domain.usecases.UpdateProfileImageUseCaseImpl
import com.khun.movievault.domain.usecases.UserLoginUseCase
import com.khun.movievault.domain.usecases.UserLoginUseCaseImpl
import com.khun.movievault.domain.usecases.UserLogoutUseCase
import com.khun.movievault.domain.usecases.UserLogoutUseCaseImpl
import com.khun.movievault.domain.usecases.ValidateTokenUseCase
import com.khun.movievault.domain.usecases.ValidateTokenUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideValidTokenUseCase(userRepository: UserRepository): ValidateTokenUseCase =
        ValidateTokenUseCaseImpl(userRepository)

    @Provides
    fun provideGetAllMovieUseCase(movieRepository: MovieRepository): GetAllMoviesUseCase =
        GetAllMoviesUseCaseImpl(movieRepository)

    @Provides
    fun provideGetProfileByIdUseCase(profileRepository: ProfileRepository): GetProfileByIdUseCase =
        GetProfileByIdUseCaseImpl(profileRepository)

    @Provides
    fun provideAddFavouriteMovieUseCase(profileRepository: ProfileRepository): AddFavouriteMovieUseCase =
        AddFavouriteMovieUseCaseImpl(profileRepository)

    @Provides
    fun provideGetAllFavouriteMoviesUseCase(profileRepository: ProfileRepository): GetAllFavouriteMovieUseCase =
        GetAllFavouriteMovieUseCaseImpl(profileRepository)

    @Provides
    fun provideUpdateProfileByIdUseCase(profileRepository: ProfileRepository): UpdateProfileByIdUseCase =
        UpdateProfileByIdUseCaseImpl(profileRepository)

    @Provides
    fun provideUpdatePasswordUseCase(profileRepository: ProfileRepository): UpdatePasswordUseCase =
        UpdatePasswordUseCaseImpl(profileRepository)

    @Provides
    fun provideUpdateProfileImageUseCase(profileRepository: ProfileRepository): UpdateProfileImageUseCase =
        UpdateProfileImageUseCaseImpl(profileRepository)

    @Provides
    fun provideCreateUserUseCase(userRepository: UserRepository): CreateUserUseCase =
        CreateUserUseCaseImpl(userRepository)

    @Provides
    fun provideUserLoginUseCase(userRepository: UserRepository): UserLoginUseCase =
        UserLoginUseCaseImpl(userRepository)

    @Provides
    fun provideRetriveLoginUserInfoUseCase(userRepository: UserRepository): RetrieveLoginUserInfoUseCase =
        RetrieveLoginUserInfoUseCaseImpl(userRepository)

    @Provides
    fun provideUserLogoutUseCase(userRepository: UserRepository): UserLogoutUseCase =
        UserLogoutUseCaseImpl(userRepository)
}