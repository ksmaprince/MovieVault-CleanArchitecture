package com.khun.movievault.domain.usecases

import com.khun.movievault.data.DataResult
import com.khun.movievault.data.model.CommonResponse
import kotlinx.coroutines.flow.Flow

interface UserLogoutUseCase {
    suspend fun execute(): Flow<DataResult<CommonResponse>>
}