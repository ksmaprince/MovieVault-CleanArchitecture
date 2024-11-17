package com.khun.movievault.presentation.contracts

import com.khun.movievault.data.model.RegisterUserResponse

class RegisterUserContract {
    data class State(
        val user: RegisterUserResponse?=null,
        val isLoading: Boolean = false
    )
}