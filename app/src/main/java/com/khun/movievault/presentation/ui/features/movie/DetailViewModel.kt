package com.khun.movievault.presentation.ui.features.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khun.movievault.data.DataResult
import com.khun.movievault.domain.usecases.AddFavouriteMovieUseCase
import com.khun.movievault.utils.ErrorsMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val addFavouriteMovieUseCase: AddFavouriteMovieUseCase) :
    ViewModel() {
    private val _detailUIState = MutableStateFlow<DetailScreenUIState>(
        DetailScreenUIState.Idle
    )
    val detailScreenUIState get() = _detailUIState

    fun addFavourite(profileId: Long, movieId: Long){
        viewModelScope.launch {
            addFavouriteMovieUseCase.execute(profileId = profileId, movieId = movieId).collect{
                when(it){
                    is DataResult.Loading -> _detailUIState.value = DetailScreenUIState.Loading
                    is DataResult.Success -> _detailUIState.value = DetailScreenUIState.FavouriteAdded
                    is DataResult.Error -> _detailUIState.value = DetailScreenUIState.Error(it.message?: ErrorsMessage.gotApiCallError)
                }
            }
        }
    }
}

sealed interface DetailScreenUIState {
    data object Idle : DetailScreenUIState
    data object Loading : DetailScreenUIState
    data class Error(val message: String) : DetailScreenUIState
    data object FavouriteAdded : DetailScreenUIState
}