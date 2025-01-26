package com.khun.movievault.presentation.ui.features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khun.movievault.data.DataResult
import com.khun.movievault.data.model.Movie
import com.khun.movievault.domain.mappers.Movies
import com.khun.movievault.domain.usecases.GetAllMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(getAllMoviesUseCase: GetAllMoviesUseCase) :
    ViewModel() {
    val homeScreenUISate = getAllMoviesUseCase.execute().map {
        when (it) {
            is DataResult.Loading -> HomeScreenUISate.Loading
            is DataResult.Success -> {
                if (!it.data.isNullOrEmpty()) {
                    HomeScreenUISate.Ready(it.data)
                } else {
                    HomeScreenUISate.Error("No Data From Server")
                }
            }

            is DataResult.Error -> {

            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = HomeScreenUISate.Loading
    )

    var movie : Movie? = null
    fun setMovieInfo(movie: Movie) {
       this.movie = movie
    }

}

sealed interface HomeScreenUISate {
    data object Loading : HomeScreenUISate
    data class Ready(val movies: Movies) : HomeScreenUISate
    data class Error(val message: String) : HomeScreenUISate
}