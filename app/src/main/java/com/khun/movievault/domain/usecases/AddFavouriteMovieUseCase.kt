package com.khun.movievault.domain.usecases

import com.khun.movievault.data.DataResult
import kotlinx.coroutines.flow.Flow

interface AddFavouriteMovieUseCase {
    suspend fun execute(profileId: Long, movieId: Long): Flow<DataResult<Long>>
}