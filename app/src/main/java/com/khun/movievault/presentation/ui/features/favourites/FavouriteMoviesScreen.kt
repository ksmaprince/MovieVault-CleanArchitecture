package com.khun.movievault.presentation.ui.features.favourites

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.khun.movievault.R
import com.khun.movievault.data.model.Movie
import com.khun.movievault.presentation.contracts.BaseContract
import com.khun.movievault.presentation.contracts.FavouriteMovieContract
import com.khun.movievault.presentation.ui.components.ShowLoadingDialog
import com.khun.movievault.presentation.ui.components.showToastMessage
import com.khun.movievault.presentation.ui.features.movie.YoutubePlayer
import com.khun.movievault.presentation.ui.components.nav.BottomNavigationBar
import com.khun.movievault.presentation.ui.features.login.Logo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

@Composable
fun FavouriteMoviesScreen(
    state: FavouriteMovieContract.State,
    effectFlow: Flow<BaseContract.Effect>,
    navController: NavController
) {

    val context = LocalContext.current

    LaunchedEffect(effectFlow) {
        effectFlow.onEach {
            if (it is BaseContract.Effect.DataWasLoaded) {
//                showToastMessage(context, "Data loaded")
                Log.i("Data Loaded", "Favourite")
            }
        }.collect {
            if (it is BaseContract.Effect.Error) {
                showToastMessage(context, it.errorMessage)
            }
        }
    }
//    if (state.isLoading) {
//        ShowLoadingDialog(message = "Fetching data ...") {
//
//        }
//    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(15.dp)) {
            LazyColumn {
                items(state.favouriteMovies) {
                    MovieItem(it)
                }
            }
        }
    }

}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MovieItem(movie: Movie) {
    var showYouTube by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        onClick = {
            showYouTube = !showYouTube
        }
    ) {
        if (showYouTube) {
            Column(modifier = Modifier.fillMaxWidth()) {
                showYoutubeVideo(url = movie.trailer)
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
                Spacer(modifier = Modifier.height(5.dp))
                TextButton(onClick = { showYouTube = false }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_keyboard_double_arrow_up_24),
                        contentDescription = "Up"
                    )
                }
            }

        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                GlideImage(
                    model = "https://image.tmdb.org/t/p/w500${movie.poster}",
                    contentDescription = "PosterPath",
                    Modifier
                        .size(100.dp)
                        .padding(10.dp),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = movie.movieTitle)
                    Text(text = movie.releaseDate)
                    Text(text = movie.rating.toString())
                }
                TextButton(
                    onClick = { showYouTube = true },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_keyboard_double_arrow_down_24),
                        contentDescription = "Up"
                    )
                }
            }
        }
    }
}

@Composable
fun showYoutubeVideo(url: String) {
    YoutubePlayer(
        youtubeVideoId = url,
        lifecycleOwner = LocalLifecycleOwner.current
    )
}