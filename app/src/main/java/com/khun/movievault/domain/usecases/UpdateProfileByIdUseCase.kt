package com.khun.movievault.domain.usecases

import com.khun.movievault.data.model.Profile
import com.khun.movievault.data.DataResult
import kotlinx.coroutines.flow.Flow

interface UpdateProfileByIdUseCase {
    suspend fun execute(profile: Profile): Flow<DataResult<Profile>>
}