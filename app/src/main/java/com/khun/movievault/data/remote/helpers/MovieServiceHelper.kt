package com.khun.movievault.data.remote.helpers

import com.khun.movievault.data.model.Movie
import retrofit2.Response

interface MovieServiceHelper {
    suspend fun getAllMovies() : Response<List<Movie>>
}