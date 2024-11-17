package com.khun.movievault.domain.usecases

import com.khun.movievault.data.DataResult
import com.khun.movievault.data.model.UserLoginResponse
import kotlinx.coroutines.flow.Flow

interface RetrieveLoginUserInfoUseCase {
    suspend fun execute(): Flow<DataResult<UserLoginResponse>>
}