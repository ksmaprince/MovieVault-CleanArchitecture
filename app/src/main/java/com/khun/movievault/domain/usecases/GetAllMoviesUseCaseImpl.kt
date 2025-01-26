package com.khun.movievault.domain.usecases

import com.khun.movievault.data.DataResult
import com.khun.movievault.data.model.Movie
import com.khun.movievault.domain.repositories.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetAllMoviesUseCaseImpl(private val movieRepository: MovieRepository) : GetAllMoviesUseCase {
    override fun execute(): Flow<DataResult<List<Movie>>> =
        flow {
            movieRepository.getAllMovies().collect {
                when (it) {
                    is DataResult.Success -> {
                        val movies = it.data
                            ?: listOf() // When we have specifice kind of model filter and map it here
                        emit(DataResult.Success(movies))
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