package com.khun.movievault.data.remote.helpers

import com.khun.movievault.data.model.Movie
import com.khun.movievault.data.remote.services.MovieService
import retrofit2.Response

class MovieServiceHelperImpl(private val movieService: MovieService) : MovieServiceHelper {
    override suspend fun getAllMovies(): Response<List<Movie>> =
        movieService.getAllMovies()
}