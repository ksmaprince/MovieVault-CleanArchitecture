package com.khun.movievault.presentation.contracts

class LoginContract {
    data class State(
        val isUserLoginSuccess: Boolean = false,
        val isLoading: Boolean = false
    )
}