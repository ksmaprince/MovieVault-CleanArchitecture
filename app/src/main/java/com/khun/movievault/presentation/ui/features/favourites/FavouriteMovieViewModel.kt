package com.khun.movievault.presentation.ui.features.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khun.movievault.data.DataResult
import com.khun.movievault.domain.mappers.Movies
import com.khun.movievault.domain.usecases.GetAllFavouriteMovieUseCase
import com.khun.movievault.utils.ErrorsMessage
import com.khun.movievault.utils.userProfileId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavouriteMovieViewModel @Inject constructor(
    private val getAllFavouriteMovieUseCase: GetAllFavouriteMovieUseCase,
) : ViewModel() {

    val favouriteScreenUIState = getAllFavouriteMovieUseCase.execute(userProfileId).map {
        when (it) {
            is DataResult.Loading -> FavouriteScreenUIState.Loading
            is DataResult.Success -> {
                if (!it.data.isNullOrEmpty()) {
                    FavouriteScreenUIState.Ready(it.data)
                } else {
                    FavouriteScreenUIState.Error("No Favourite Data")
                }
            }

            is DataResult.Error -> FavouriteScreenUIState.Error(
                it.message ?: ErrorsMessage.gotApiCallError
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = FavouriteScreenUIState.Loading
    )
}

sealed interface FavouriteScreenUIState {
    data object Loading : FavouriteScreenUIState
    data class Error(val message: String) : FavouriteScreenUIState
    data class Ready(val movies: Movies) : FavouriteScreenUIState
}