package com.khun.movievault.data.remote.services

import com.khun.movievault.data.model.Movie
import retrofit2.Response
import retrofit2.http.GET

interface MovieService {

    @GET("movie/list")
    suspend fun getAllMovies() : Response<List<Movie>>
}