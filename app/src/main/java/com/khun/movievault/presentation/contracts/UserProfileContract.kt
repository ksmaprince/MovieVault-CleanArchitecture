package com.khun.movievault.presentation.contracts

import com.khun.movievault.data.model.Profile
import com.khun.movievault.domain.mappers.CallSuccessModel

class UserProfileContract {
    data class State(
        val userProfile: Profile? = null,
        val updateImage: CallSuccessModel? = null,
        val isLogout: Boolean = false,
        val logoutSuccessMessage: String = "",
        val isLoading: Boolean = false
    )
}