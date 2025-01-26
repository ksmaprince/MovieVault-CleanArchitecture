package com.khun.movievault.domain.usecases

import com.khun.movievault.data.model.Movie
import com.khun.movievault.data.DataResult
import com.khun.movievault.domain.repositories.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetAllFavouriteMovieUseCaseImpl(private val profileRepository: ProfileRepository) :
    GetAllFavouriteMovieUseCase {
    override fun execute(profileId: Long): Flow<DataResult<List<Movie>>> =
        flow {
            profileRepository.getAllFavouriteMovie(profileId).collect {
                when (it) {
                    is DataResult.Success -> {
                        emit(DataResult.Success(it.data))
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