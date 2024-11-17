package com.khun.movievault.domain.usecases

import com.khun.movievault.data.DataResult
import com.khun.movievault.domain.repositories.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddFavouriteMovieUseCaseImpl(private val profileRepository: ProfileRepository) :
    AddFavouriteMovieUseCase {
    override suspend fun execute(profileId: Long, movieId: Long): Flow<DataResult<Long>> =
        flow<DataResult<Long>> {
            profileRepository.addFavouriteMovie(profileId, movieId).collect {
                when (it) {
                    is DataResult.Success -> {
                        emit(DataResult.Success(it.data ?: 0))
                    }

                    is DataResult.Error -> {
                        emit(DataResult.Error(it.message))
                    }

                    is DataResult.Loading -> {
                        emit(DataResult.Loading())
                    }
                }
            }
        }
}