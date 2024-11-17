package com.khun.movievault.presentation.ui.features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khun.movievault.data.model.Movie
import com.khun.movievault.data.DataResult
import com.khun.movievault.domain.usecases.GetAllMoviesUseCase
import com.khun.movievault.presentation.contracts.BaseContract
import com.khun.movievault.presentation.contracts.MovieContract
import com.khun.movievault.utils.ErrorsMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val getAllMoviesUseCase: GetAllMoviesUseCase) :
    ViewModel() {

    private val _movieState = MutableStateFlow(
        MovieContract.State(
            movies = listOf(),
            isLoading = false
        )
    )
    val movieState: StateFlow<MovieContract.State> get() = _movieState
    var effects = Channel<BaseContract.Effect>(Channel.UNLIMITED)

    init {
        fetchAllMovie()
    }

    private fun fetchAllMovie() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllMoviesUseCase.execute().collect {
                when (it) {
                    is DataResult.Success -> {
                        _movieState.value = movieState.value.copy(
                            movies = it.data ?: listOf(),
                            isLoading = false
                        )
                        effects.send(BaseContract.Effect.DataWasLoaded)
                    }

                    is DataResult.Error -> {
                        _movieState.value = movieState.value.copy(
                            isLoading = false
                        )
                        effects.send(
                            BaseContract.Effect.Error(
                                it.message ?: ErrorsMessage.gotApiCallError
                            )
                        )
                    }

                    is DataResult.Loading -> {
                        if (!movieState.value.isLoading) {
                            _movieState.value = movieState.value.copy(
                                isLoading = true
                            )
                        }
                    }
                }
            }
        }
    }

    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie>
        get() = _movie

    fun setMovieInfo(movie: Movie) {
        _movie.value = movie
    }

//    fun setMovieStateEmpty() {
//        _movieState.value = MovieState.Empty
//    }
}