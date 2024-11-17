package com.khun.movievault.domain.repositories

import com.khun.movievault.data.model.Movie
import com.khun.movievault.data.DataResult
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getAllMovies(): Flow<DataResult<List<Movie>>>
}