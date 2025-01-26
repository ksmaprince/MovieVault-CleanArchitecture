package com.khun.movievault.presentation.ui.components.nav

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.khun.movievault.presentation.ui.components.nav.Destinations.APP_MAIN_ROUTE
import com.khun.movievault.presentation.ui.components.nav.Destinations.LOGIN_ROUTE
import com.khun.movievault.presentation.ui.components.nav.Destinations.MOVIE_DETAIL_ROUTE
import com.khun.movievault.presentation.ui.components.nav.Destinations.PASSWORD_EDIT_ROUTE
import com.khun.movievault.presentation.ui.components.nav.Destinations.PROFILE_EDIT_ROUTE
import com.khun.movievault.presentation.ui.components.nav.Destinations.REGISTER_ROUTE
import com.khun.movievault.presentation.ui.features.favourites.FavouriteMoviesScreen
import com.khun.movievault.presentation.ui.features.home.HomeScreen
import com.khun.movievault.presentation.ui.features.home.MovieViewModel
import com.khun.movievault.presentation.ui.features.login.LoginScreen
import com.khun.movievault.presentation.ui.features.movie.MovieDetailScreen
import com.khun.movievault.presentation.ui.features.profile.ChangePasswordScreen
import com.khun.movievault.presentation.ui.features.profile.EditProfileScreen
import com.khun.movievault.presentation.ui.features.profile.ProfileScreen
import com.khun.movievault.presentation.ui.features.profile.ProfileViewModel
import com.khun.movievault.presentation.ui.features.register.RegisterScreen
import com.khun.movievault.presentation.ui.features.search.SearchScreen

object Destinations {
    const val APP_MAIN_ROUTE = "main"
    const val LOGIN_ROUTE = "login"
    const val REGISTER_ROUTE = "register"
    const val MOVIE_HOME_ROUTE = "home"
    const val MOVIE_DETAIL_ROUTE = "moviedetail"
    const val PROFILE_EDIT_ROUTE = "editprofile"
    const val PASSWORD_EDIT_ROUTE = "passwordedit"
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MovieVaultNavHost(
    navController: NavHostController = rememberNavController(),
    onLogout: () -> Unit
) {

    NavHost(navController = navController, startDestination = LOGIN_ROUTE) {
        composable(LOGIN_ROUTE) {
            LoginScreen(
                onClickRegister = {
                    navController.navigate(REGISTER_ROUTE)
                }
            ) {
                navController.navigate(APP_MAIN_ROUTE)
            }
        }
        composable(REGISTER_ROUTE) {
            RegisterScreen {
                navController.popBackStack()
            }
        }
        composable(APP_MAIN_ROUTE) {
            BottomNavigationBar {
                onLogout()
            }
        }
    }
}


@SuppressLint("NewApi")
@Composable
fun BottomNavigationBar(
    movieViewModel: MovieViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
    onLogout: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val navItems = listOf(NavItem.Home, NavItem.Search, NavItem.List, NavItem.Profile)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItems.forEachIndexed { _, navigationItem ->
                    NavigationBarItem(
                        selected = navigationItem.path == currentDestination?.route,
                        label = {
                            Text(navigationItem.title)
                        },
                        icon = {
                            Icon(
                                navigationItem.icon,
                                contentDescription = navigationItem.title
                            )
                        },
                        onClick = {
                            navController.navigate(navigationItem.path) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavItem.Home.path,
            modifier = Modifier.padding(paddingValues)
        ) {

            composable(NavItem.Home.path) {
                HomeScreen(homeScreenViewModel = movieViewModel) {
                    navController.navigate(MOVIE_DETAIL_ROUTE)
                }
            }

            composable(NavItem.Search.path) {
                SearchScreen(movieViewModel = movieViewModel) {
                    navController.navigate(MOVIE_DETAIL_ROUTE)
                }
            }

            composable(NavItem.List.path) {
                FavouriteMoviesScreen()
            }

            composable(NavItem.Profile.path) {
                ProfileScreen(
                    profileViewModel = profileViewModel,
                    onNavigateToEditProfile = {
                        navController.navigate(PROFILE_EDIT_ROUTE)
                    },
                    onNavigateToEditPassword = {
                        navController.navigate(PASSWORD_EDIT_ROUTE)
                    },
                    onLogoutSuccess = onLogout
                )
            }

            composable(MOVIE_DETAIL_ROUTE) {
                movieViewModel.movie?.let {
                    MovieDetailScreen(movie = it) {
                        navController.popBackStack()
                    }
                }
            }

            composable(PROFILE_EDIT_ROUTE) {
                EditProfileScreen(profile = profileViewModel.profile.value!!) {
                    navController.popBackStack()
                }
            }

            composable(PASSWORD_EDIT_ROUTE) {
                ChangePasswordScreen {
                    navController.popBackStack()
                }
            }
        }
    }
}


