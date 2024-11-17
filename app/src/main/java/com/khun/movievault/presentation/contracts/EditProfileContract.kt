package com.khun.movievault.presentation.contracts

import com.khun.movievault.domain.mappers.CallSuccessModel

class EditProfileContract {
    data class State(
        val updatedProfile: CallSuccessModel? = null,
        val updatePassword: CallSuccessModel? = null,
        val isLoading: Boolean = false
    )
}