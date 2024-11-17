package com.khun.movievault.presentation.contracts

import com.khun.movievault.domain.mappers.CallSuccessModel

class MovieDetailContract {
    data class State(
        val addFavouriteSuccess: CallSuccessModel? = null,
        val isLoading: Boolean = false
    )
}