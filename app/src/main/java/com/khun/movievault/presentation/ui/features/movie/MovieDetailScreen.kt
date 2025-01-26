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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.khun.movievault.data.model.Movie
import com.khun.movievault.presentation.ui.components.AppTopAppBar
import com.khun.movievault.presentation.ui.components.ShowLoading
import com.khun.movievault.presentation.ui.components.showToastMessage
import com.khun.movievault.utils.userProfileId

@Composable
fun MovieDetailScreen(
    detailViewModel: DetailViewModel = hiltViewModel(),
    movie: Movie,
    popBack: () -> Unit
) {
    val context = LocalContext.current
    val detailUiState by detailViewModel.detailScreenUIState.collectAsStateWithLifecycle()
    var isLoading by remember {
        mutableStateOf(false)
    }
    when (val currentState = detailUiState) {
        is DetailScreenUIState.Loading -> isLoading = true
        is DetailScreenUIState.Error -> {
            isLoading = false
            showToastMessage(context = context, message = currentState.message)
        }

        is DetailScreenUIState.FavouriteAdded -> {
            isLoading = false
            showToastMessage(context = context, "Favourite Added")
        }

        else -> isLoading = false
    }

    Scaffold(
        topBar = {
            AppTopAppBar(
                topAppBarText = movie.movieTitle,
                onNavUp = popBack
            )
        },
        content = { contentPadding ->
            if (isLoading) ShowLoading()
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
                        detailViewModel.addFavourite(
                            profileId = userProfileId,
                            movieId = movie.movieId
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Add To Favourite")
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    )

}