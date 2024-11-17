package com.khun.movievault.presentation.ui.features.home

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.khun.movievault.data.model.Movie
import com.khun.movievault.presentation.contracts.BaseContract
import com.khun.movievault.presentation.contracts.MovieContract
import com.khun.movievault.presentation.ui.components.ShowLoadingDialog
import com.khun.movievault.presentation.ui.components.showToastMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    state: MovieContract.State,
    effectFlow: Flow<BaseContract.Effect>?,
    onMovieClick: (movie: Movie) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var firstBackPressed by remember {
        mutableStateOf(false)
    }

    if (state.isLoading) {
        ShowLoadingDialog(message = "Fetching data ...") {

        }
    }

    LaunchedEffect(effectFlow) {
        effectFlow?.onEach {
            if (it is BaseContract.Effect.DataWasLoaded) {
                //showToastMessage(context, "Movies loaded")
                Log.i("Data Loaded", "Home")
            }
        }?.collect {
            if (it is BaseContract.Effect.Error) {
                showToastMessage(context, it.errorMessage)
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)) {
            HomeScreenItem(
                state = state,
                onMovieClick = onMovieClick
            )
            Text("Home Screen")
        }
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
    state: MovieContract.State,
    onMovieClick: (movie: Movie) -> Unit
) {

    Column(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            Modifier.padding(4.dp)
        ) {
            items(state.movies) {
                MovieItem(
                    movie = it,
                    onMovieClick = onMovieClick
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieItem(movie: Movie, onMovieClick: (movie: Movie) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        onClick = { onMovieClick(movie) }
    ) {
        GlideImage(
            model = "https://image.tmdb.org/t/p/w500${movie.poster}",
            contentDescription = "Poster",
            Modifier.fillMaxWidth(),
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
