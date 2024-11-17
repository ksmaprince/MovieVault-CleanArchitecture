package com.khun.movievault.data.repositories

import com.google.gson.Gson
import com.khun.movievault.data.model.Movie
import com.khun.movievault.data.model.ResponseException
import com.khun.movievault.data.DataResult
import com.khun.movievault.data.remote.helpers.MovieServiceHelper
import com.khun.movievault.domain.repositories.MovieRepository
import com.khun.movievault.utils.ErrorsMessage
import com.khun.movievault.utils.savedMovies
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class MovieRepositoryImpl(private val movieServiceHelper: MovieServiceHelper) : MovieRepository {
    override suspend fun getAllMovies(): Flow<DataResult<List<Movie>>> =
        flow<DataResult<List<Movie>>> {
            emit(DataResult.Loading())
            with(movieServiceHelper.getAllMovies()) {
                if (isSuccessful) {
                    savedMovies = this.body()?: listOf()
                    emit(DataResult.Success(this.body()))
                }
                else {
                    if (this.code() in 400..499) {
                        emit(DataResult.Error(ErrorsMessage.sessionExpiredError))
                    } else if (this.code() in 500..599) {
                        emit(DataResult.Error(ErrorsMessage.serverError))
                    } else {
                        val error = Gson().fromJson(
                            this.errorBody()?.charStream(),
                            ResponseException::class.java
                        )
                        emit(DataResult.Error(error.ErrorMessage))
                    }
                }
            }
        }.catch {
            emit(DataResult.Error(it.localizedMessage))
        }
}