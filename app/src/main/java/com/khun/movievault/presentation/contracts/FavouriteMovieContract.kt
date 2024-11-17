package com.khun.movievault.presentation.contracts

import com.khun.movievault.data.model.Movie

class FavouriteMovieContract {
    data class State(
        val favouriteMovies: List<Movie> = listOf(),
        val isLoading: Boolean = false
    )
}