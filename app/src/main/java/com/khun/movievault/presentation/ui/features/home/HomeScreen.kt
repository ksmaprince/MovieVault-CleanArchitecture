package com.khun.movievault.presentation.ui.features.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.khun.movievault.data.model.Movie
import com.khun.movievault.domain.mappers.Movies
import com.khun.movievault.domain.mappers.toMovieUrl
import com.khun.movievault.presentation.ui.components.ShowLoading
import com.khun.movievault.presentation.ui.components.showToastMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    homeScreenViewModel: MovieViewModel,
    onMovieClick: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var firstBackPressed by remember {
        mutableStateOf(false)
    }
    val homeScreenUISate by homeScreenViewModel.homeScreenUISate.collectAsStateWithLifecycle()
    var isLoading by remember {
        mutableStateOf(false)
    }
    var movies by remember {
        mutableStateOf<Movies>(listOf())
    }

    when (val currentState = homeScreenUISate) {
        is HomeScreenUISate.Loading -> isLoading = true
        is HomeScreenUISate.Ready -> {
            isLoading = false
            movies = currentState.movies
        }

        is HomeScreenUISate.Error -> {
            isLoading = false
            showToastMessage(context = context, message = currentState.message)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            HomeScreenItem(
                movies = movies,
                onMovieClick = {
                    homeScreenViewModel.setMovieInfo(it)
                    onMovieClick()
                }
            )
            Text("Home Screen")
        }
        if (isLoading) ShowLoading()
    }

    BackHandler {
        if (firstBackPressed) {
            val activity = context as Activity
            activity.finish()
        } else {
            firstBackPressed = true
            showToastMessage(context = context, message = "Press again to exit")
            coroutineScope.launch {
                delay(2000L)
                firstBackPressed = false
            }
        }
    }
}

@Composable
fun HomeScreenItem(
    movies: Movies,
    onMovieClick: (movie: Movie) -> Unit
) {

    Column(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            Modifier.padding(4.dp)
        ) {
            items(movies) {
                MovieItem(
                    movie = it,
                    onMovieClick = onMovieClick
                )
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, onMovieClick: (movie: Movie) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        onClick = { onMovieClick(movie) }
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier.fillMaxWidth(),
            model = ImageRequest.Builder(LocalContext.current)
                .crossfade(true)
                .data(movie.poster.toMovieUrl())
                .build(),
            loading = {
                CircularProgressIndicator(modifier = Modifier.requiredSize(24.dp))
            },
            contentDescription = movie.movieTitle,
            contentScale = ContentScale.Crop
        )

        Column(
            Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(4.dp)
        ) {
            Text(
                text = movie.movieTitle, style = MaterialTheme.typography.titleMedium
            )
            Text(text = movie.releaseDate)
        }
    }
}

