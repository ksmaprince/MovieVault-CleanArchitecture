package com.khun.movievault.domain.usecases

import com.khun.movievault.data.model.Movie
import com.khun.movievault.data.DataResult
import kotlinx.coroutines.flow.Flow

interface GetAllMoviesUseCase {
    suspend fun execute(): Flow<DataResult<List<Movie>>>
}