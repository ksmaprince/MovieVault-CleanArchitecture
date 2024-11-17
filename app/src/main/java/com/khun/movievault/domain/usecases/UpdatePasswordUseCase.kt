package com.khun.movievault.domain.usecases

import com.khun.movievault.data.model.UpdatePasswordRequest
import com.khun.movievault.data.model.UpdatePasswordResponse
import com.khun.movievault.data.DataResult
import kotlinx.coroutines.flow.Flow

interface UpdatePasswordUseCase {
    suspend fun execute(passwordRequest: UpdatePasswordRequest): Flow<DataResult<UpdatePasswordResponse>>
}