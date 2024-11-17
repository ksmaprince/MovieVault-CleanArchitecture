package com.khun.movievault.domain.usecases

import com.khun.movievault.data.model.Profile
import com.khun.movievault.data.DataResult
import kotlinx.coroutines.flow.Flow

interface GetProfileByIdUseCase {
    suspend fun execute(profileId: Long): Flow<DataResult<Profile>>
}