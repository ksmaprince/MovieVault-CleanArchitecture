package com.khun.movievault.presentation.contracts

import com.khun.movievault.data.model.Movie

class MovieContract {
    data class State(
        val movies: List<Movie> = listOf(),
        val isLoading: Boolean = false
    )
}