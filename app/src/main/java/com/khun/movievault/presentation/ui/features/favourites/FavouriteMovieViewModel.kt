package com.khun.movievault.presentation.ui.features.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khun.movievault.data.DataResult
import com.khun.movievault.domain.usecases.GetAllFavouriteMovieUseCase
import com.khun.movievault.presentation.contracts.BaseContract
import com.khun.movievault.presentation.contracts.FavouriteMovieContract
import com.khun.movievault.utils.ErrorsMessage
import com.khun.movievault.utils.userProfileId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteMovieViewModel @Inject constructor(
    private val getAllFavouriteMovieUseCase: GetAllFavouriteMovieUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(
        FavouriteMovieContract.State(
            favouriteMovies = listOf(),
            isLoading = false
        )
    )
    val state: StateFlow<FavouriteMovieContract.State> = _state
    var effects = Channel<BaseContract.Effect>(Channel.UNLIMITED)
        private set

    init {
        fetchAllFavouriteMovies()
    }

    fun fetchAllFavouriteMovies() {
        viewModelScope.launch {
            getAllFavouriteMovieUseCase.execute(userProfileId).collect {
                when (it) {
                    is DataResult.Success -> {
                        _state.value = state.value.copy(
                            favouriteMovies = it.data!!,
                            isLoading = false
                        )
                        effects.send(BaseContract.Effect.DataWasLoaded)
                    }

                    is DataResult.Error -> {
                        _state.value = state.value.copy(isLoading = false)
                        effects.send(
                            BaseContract.Effect.Error(
                                it.message ?: ErrorsMessage.gotApiCallError
                            )
                        )
                    }

                    is DataResult.Loading -> {
                        if (!state.value.isLoading) {
                            _state.value = state.value.copy(isLoading = true)
                        }
                    }
                }
            }
        }
    }
}