package com.khun.movievault.presentation.ui.features.movie

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.khun.movievault.data.model.Movie
import com.khun.movievault.presentation.contracts.BaseContract
import com.khun.movievault.presentation.contracts.MovieDetailContract
import com.khun.movievault.presentation.ui.components.AppTopAppBar
import com.khun.movievault.presentation.ui.components.ShowLoadingDialog
import com.khun.movievault.presentation.ui.components.showToastMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

@Composable
fun MovieDetailScreen(
    state: MovieDetailContract.State,
    effectFlow: Flow<BaseContract.Effect>?,
    movie: Movie,
    navController: NavController,
    onAddFavourite: (movieId: Long) -> Unit,
    onFavouriteAdded: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(effectFlow) {
        effectFlow?.onEach {
            if (it is BaseContract.Effect.DataWasLoaded) {
                showToastMessage(context, "Add Favourite Success")
                onFavouriteAdded()
            }
        }?.collect {
            if (it is BaseContract.Effect.Error) {
                showToastMessage(context, it.errorMessage)
            }
        }
    }
    Scaffold(
        topBar = {
            AppTopAppBar(
                topAppBarText = movie.movieTitle,
                onNavUp = {
                    navController.popBackStack()
                },
            )
        },
        content = { contentPadding ->
            Column(
                Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                YoutubePlayer(
                    youtubeVideoId = movie.trailer,
                    lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
                )
                Text(
                    text = movie.movieTitle,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(8.dp)
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(text = "Rating: ${movie.rating} Release Date: ${movie.releaseDate}")
                }
                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                TextButton(
                    onClick = {
                        onAddFavourite(movie.movieId)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Add To Favourite")
                }

                Spacer(modifier = Modifier.height(24.dp))
                if (state.isLoading) {
                    ShowLoadingDialog(message = "Adding data ...") {
                        // Do Something
                    }
                }
            }
        }
    )

}