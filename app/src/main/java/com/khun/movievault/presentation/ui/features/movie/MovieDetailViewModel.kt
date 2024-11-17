package com.khun.movievault.presentation.ui.features.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khun.movievault.data.DataResult
import com.khun.movievault.domain.mappers.CallSuccessModel
import com.khun.movievault.domain.usecases.AddFavouriteMovieUseCase
import com.khun.movievault.presentation.contracts.BaseContract
import com.khun.movievault.presentation.contracts.MovieDetailContract
import com.khun.movievault.utils.ErrorsMessage
import com.khun.movievault.utils.userProfileId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(private val addFavouriteMovieUseCase: AddFavouriteMovieUseCase) :
    ViewModel() {
    private val _state = MutableStateFlow(
        MovieDetailContract.State(
            addFavouriteSuccess = null,
            isLoading = false
        )
    )
    val state: StateFlow<MovieDetailContract.State> = _state
    var effects = Channel<BaseContract.Effect>(Channel.UNLIMITED)
        private set

    var favId: Long = 0L
    fun addFavoriteMovie(movieId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            addFavouriteMovieUseCase.execute(userProfileId, movieId).collect {
                when (it) {
                    is DataResult.Success -> {
                        favId = it.data ?: favId
                        _state.value = state.value.copy(
                            addFavouriteSuccess = CallSuccessModel(
                                "Add Favourite Success",
                                favId
                            ),
                            isLoading = false
                        )
                        effects.send(BaseContract.Effect.DataWasLoaded)
                    }

                    is DataResult.Error -> {
                        _state.value = state.value.copy(isLoading = false)
                        effects.send(
                            BaseContract.Effect.Error(it.message ?: ErrorsMessage.gotApiCallError)
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